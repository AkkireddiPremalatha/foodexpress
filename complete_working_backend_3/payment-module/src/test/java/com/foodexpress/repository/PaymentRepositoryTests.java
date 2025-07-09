package com.foodexpress.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.foodexpress.entity.Payment;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class PaymentRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
     void setUp() {
        paymentRepository.deleteAll(); // Ensure clean state before each test
        payment = new Payment();
        payment.setOrderId(101L);
        payment.setUserId(201L);
        payment.setAmount(500.0);
        payment.setPaymentMethod("UPI");
        payment.setStatus("SUCCESS");
        payment.setTimestamp(LocalDateTime.now());
        payment.setPaymentDetails("UPI ID: jo***@upi");
    }

    @AfterEach
     void tearDown() {
        paymentRepository.deleteAll();
        payment = null;
    }

    @Test
     void givenPaymentToAddShouldReturnAddedPayment() {
        paymentRepository.save(payment);
        Payment fetchedPayment = paymentRepository.findById(payment.getId()).get();
        assertEquals(101L, fetchedPayment.getOrderId());
        assertEquals(201L, fetchedPayment.getUserId());
    }

    @Test
     void givenGetAllPaymentsShouldReturnListOfAllPayments() {
        paymentRepository.deleteAll(); // Clean up before test
        Payment payment1 = new Payment();
        payment1.setOrderId(102L);
        payment1.setUserId(202L);
        payment1.setAmount(1000.0);
        payment1.setPaymentMethod("CREDIT_CARD");
        payment1.setStatus("SUCCESS");
        payment1.setTimestamp(LocalDateTime.now());
        payment1.setPaymentDetails("Card: XXXX-XXXX-XXXX-1234");

        Payment payment2 = new Payment();
        payment2.setOrderId(103L);
        payment2.setUserId(203L);
        payment2.setAmount(1500.0);
        payment2.setPaymentMethod("NET_BANKING");
        payment2.setStatus("SUCCESS");
        payment2.setTimestamp(LocalDateTime.now());
        payment2.setPaymentDetails("Bank: HDFC, Account: XXXXXX5678");

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> paymentList = paymentRepository.findAll();
        assertEquals(2, paymentList.size());
    }

    @Test
     void givenUserIdShouldReturnPaymentsOfThatUser() {
        paymentRepository.save(payment);
        List<Payment> userPayments = paymentRepository.findByUserId(201L);
        assertEquals(1, userPayments.size());
        assertEquals(201L, userPayments.get(0).getUserId());
    }

    @Test
     void givenOrderIdShouldReturnPaymentOfThatOrder() {
        paymentRepository.save(payment);
        Optional<Payment> optional = paymentRepository.findByOrderId(101L);
        assertTrue(optional.isPresent());
        assertEquals(101L, optional.get().getOrderId());
    }

    @Test
   void givenIdToDeleteShouldDeleteThePayment() {
        paymentRepository.save(payment);
        paymentRepository.deleteById(payment.getId());
        Optional<Payment> optional = paymentRepository.findById(payment.getId());
        assertEquals(Optional.empty(), optional);
    }
}