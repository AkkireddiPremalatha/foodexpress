import React, { useState, useEffect } from 'react';
import { Container, Card, Alert, Button, Badge, Row, Col, ProgressBar, Navbar, Offcanvas } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
 
// Initialize pdfMake with fonts
pdfMake.vfs = pdfFonts.pdfMake ? pdfFonts.pdfMake.vfs : pdfFonts.vfs;
 
const PastOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showMenu, setShowMenu] = useState(false);
  const userId = localStorage.getItem('userId');
  const token = localStorage.getItem('token');
  const navigate = useNavigate();
 
  const handleMenuClick = (path) => {
    setShowMenu(false);
    if (path === 'home') {
      navigate(`/customer/${userId}`);
    } else {
      navigate(path);
    }
  };
 
  // Get status badge variant
  const getStatusBadgeVariant = (status) => {
    return status === 'DELIVERED' ? 'success' :
           status === 'CANCELLED' ? 'danger' : 'secondary';
  };
 
  // Get payment badge variant
  const getPaymentBadgeVariant = (paymentStatus) => {
    return paymentStatus === 'SUCCESS' ? 'success' : 'warning';
  };
 
  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        const response = await axios.get(
          `http://localhost:8070/api/v1/order/customer/getPastOrders/${userId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          }
        );
        const allOrders = Array.isArray(response.data) ? response.data : [response.data].filter(Boolean);
        const pastOrders = allOrders.filter(order =>
          ['DELIVERED', 'COMPLETED'].includes(order.status)
        );
        setOrders(pastOrders);
      } catch (error) {
        console.error('Error fetching orders:', error);
        setOrders([]);
        toast.error('Failed to fetch past orders');
      } finally {
        setLoading(false);
      }
    };
 
    if (userId && token) {
      fetchOrders();
    } else {
      setOrders([]);
      setLoading(false);
    }
  }, [userId, token]);
 
  //Function to fetch restaurant details by ID
  const fetchRestaurantDetailsById = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:8083/api/v1/customer/restaurants1/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching restaurant details:', error);
      return null;
    }
  };
 
  // Function to fetch customer details
  const fetchCustomerDetails = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8093/api/v1/auth/customers/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching customer details:', error);
      return null;
    }
  };
 
  // Function to generate PDF invoice
  const generateInvoicePdf = async (order) => {
    try {
      const restaurantDetails = await fetchRestaurantDetailsById(order.restaurantId);
      const customerDetails = await fetchCustomerDetails();
     
      if (  !customerDetails) {
        toast.error('Could not fetch all required details for the invoice');
        return;
      }
 
      const createdAt = order.createdAt ? new Date(order.createdAt) : new Date();
      const items = order.items ? (typeof order.items === 'string' ? [{ itemName: order.items,  price: order.totalAmount }] : order.items) : [];
     
      const docDefinition = {
        content: [
          { text: 'FoodExpress', style: 'header' },
          { text: 'INVOICE', style: 'subheader' },
          { text: '\n' },
          {
            columns: [
              {
                width: '*',
                text: [
                  { text: 'Invoice Number: ', bold: true },
                  `INV-${order.orderId}\n`,
                  { text: 'Date: ', bold: true },
                  `${createdAt.toLocaleString()}\n`,
                  { text: 'Order ID: ', bold: true },
                  `${order.orderId}\n`,
                ],
              },
              {
                width: '*',
                text: [
                  { text: 'Customer Details\n', bold: true },
                  `Name: ${customerDetails.firstName || 'N/A'}\n`,
                  `Email: ${customerDetails.email || 'N/A'}\n`,
                  `Phone: ${customerDetails.phone || 'N/A'}\n`,
                ],
              },
            ],
          },
          { text: '\n' },
          {
            text: 'Restaurant Details',
            style: 'sectionHeader',
          },
          {
            text: [
              `Name: ${restaurantDetails.restaurantName || 'N/A'}\n`,
              `Location: ${restaurantDetails.restaurantLocation || 'N/A'}\n`,
              `Contact: ${restaurantDetails.contactNumber || 'N/A'}\n`,
            ],
          },
          { text: '\n' },
          {
            text: 'Order Details',
            style: 'sectionHeader',
          },
          {
            table: {
              headerRows: 1,
              widths: ['*',  'auto'],
              body: [
                ['Item', 'Amount'],
                ...items.map(item => [
                  item.itemName,
                  `₹${item.price || 0}`,
                ]),
              ],
            },
          },
          { text: '\n' },
          {
            text: [
              `Total Amount: ₹${order.totalAmount}\n`,
              `Payment Method: ${order.paymentMethod || 'N/A'}\n`,
              `Payment Status: ${order.paymentStatus || 'N/A'}\n`,
            ],
            style: 'total',
          },
          { text: '\n\n' },
          {
            text: 'Thank you for ordering with FoodExpress!',
            style: 'footer',
          },
        ],
        styles: {
          header: {
            fontSize: 24,
            bold: true,
            alignment: 'center',
            margin: [0, 0, 0, 10],
          },
          subheader: {
            fontSize: 18,
            bold: true,
            alignment: 'center',
            margin: [0, 10, 0, 10],
          },
          sectionHeader: {
            fontSize: 14,
            bold: true,
            margin: [0, 10, 0, 5],
          },
          total: {
            fontSize: 12,
            bold: true,
            alignment: 'right',
          },
          footer: {
            alignment: 'center',
            italics: true,
            fontSize: 12,
          },
        },
      };
 
      const pdfDoc = pdfMake.createPdf(docDefinition);
      pdfDoc.download(`invoice_${order.orderId}.pdf`);
      toast.success('Invoice generated successfully!');
    } catch (error) {
      console.error('Error generating invoice:', error);
      toast.error('Failed to generate invoice');
    }
  };
 
  if (loading) {
    return <Alert variant="info" className="text-center">Loading orders...</Alert>;
  }
 
  return (
    <div>
      <Navbar bg="light" expand="lg" className="mb-3">
        <Container>
          <Button variant="outline-primary" onClick={() => setShowMenu(true)}>
            ☰
          </Button>
          <h4 className="mb-0 mx-auto">Past Orders</h4>
        </Container>
      </Navbar>
 
      <Offcanvas show={showMenu} onHide={() => setShowMenu(false)} backdrop={true}>
        <Offcanvas.Header closeButton>
          <Offcanvas.Title>Menu</Offcanvas.Title>
        </Offcanvas.Header>
        <Offcanvas.Body>
          <div className="d-grid gap-2">
            <Button
              variant="outline-primary"
              size="lg"
              onClick={() => handleMenuClick('home')}
              className="text-start"
            >
              Home
            </Button>
            <Button
              variant="outline-primary"
              size="lg"
              onClick={() => handleMenuClick(`/customer/${userId}/restaurants`)}
              className="text-start"
            >
              Available Restaurants
            </Button>
            <Button
              variant="outline-primary"
              size="lg"
              onClick={() => handleMenuClick(`/customer/${userId}/current-orders`)}
              className="text-start"
            >
              Current Orders
            </Button>
            <Button
              variant="outline-primary"
              size="lg"
              onClick={() => handleMenuClick(`/customer/${userId}/past-orders`)}
              className="text-start"
            >
              Past Orders
            </Button>
          </div>
        </Offcanvas.Body>
      </Offcanvas>
 
      <Container className="py-4">
        {orders.length === 0 ? (
          <Alert variant="info" className="text-center">No past orders found</Alert>
        ) : (
          <Row>
            {orders.map(order => (
              <Col xs={12} key={order.orderId} className="mb-4">
                <Card className="shadow-sm h-100">
                  <Card.Header className="d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Order #{order.orderId}</h5>
                    <Badge bg={getStatusBadgeVariant(order.status)}>{order.status}</Badge>
                  </Card.Header>
                  <Card.Body>
                    <Row className="mb-3">
                      <Col md={6}>
                        <h6>{order.restaurantName}</h6>
                        <p className="text-muted mb-2">{order.items}</p>
                        <p className="mb-2">
                          <strong>Total Amount:</strong> ₹{order.totalAmount}
                        </p>
                        <p className="mb-2">
                          {/* <strong>Delivery Agent:</strong> {order.deliveryAgent} */}
                        </p>
                      </Col>
                      <Col md={6} className="text-md-end">
                        <p className="mb-2">
                          <strong>Ordered on:</strong>{' '}
                          {new Date(order.createdAt).toLocaleDateString('en-IN', {
                            day: 'numeric',
                            month: 'short',
                            year: 'numeric',
                            hour: '2-digit',
                            minute: '2-digit'
                          })}
                        </p>
                        <p className="mb-2">
                          <strong>Payment Method:</strong> {order.paymentMethod}
                        </p>
                        <p className="mb-2">
                          <Badge bg={getPaymentBadgeVariant(order.paymentStatus)}>
                            Payment {order.paymentStatus}
                          </Badge>
                        </p>
                        {order.rating && (
                          <p className="mb-0">
                            <strong>Rating:</strong>{' '}
                            {'⭐'.repeat(order.rating)}
                          </p>
                        )}
                        <Button
                          variant="outline-primary"
                          size="sm"
                          className="mt-2"
                          onClick={() => generateInvoicePdf(order)}
                        >
                          Download Invoice
                        </Button>
                      </Col>
                    </Row>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        )}
      </Container>
    </div>
  );
};
 
export default PastOrders;
 
 
 
 
 
 
 
 