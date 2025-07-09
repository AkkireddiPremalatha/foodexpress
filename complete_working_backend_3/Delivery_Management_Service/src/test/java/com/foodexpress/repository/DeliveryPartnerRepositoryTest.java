package com.foodexpress.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.foodexpress.entity.AssignedDeliveryPartner;

class AssignedDeliveryPartnerRepositoryTest {

    @Mock
    private AssignedDeliveryPartnerRepository repository;

    private AssignedDeliveryPartner mockPartner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockPartner = new AssignedDeliveryPartner();
        mockPartner.setPartnerId(1);
        mockPartner.setName("Anika Sharma");
        mockPartner.setPhoneNumber("9876543210");
        mockPartner.setVehicleNumber("TN12ZZ7890");
        mockPartner.setServicePincode("600001");
        mockPartner.setDeliveryPartnerStatus(true);
    }

    @Test
    void testFindByServicePincodeAndDeliveryPartnerStatus_MatchFound() {
        when(repository.findByServicePincodeAndDeliveryPartnerStatus("600001", true))
                .thenReturn(List.of(mockPartner));

        List<AssignedDeliveryPartner> result = repository.findByServicePincodeAndDeliveryPartnerStatus("600001", true);
        assertEquals(1, result.size());
        assertEquals("Anika Sharma", result.get(0).getName());
    }

    @Test
    void testFindByServicePincodeAndDeliveryPartnerStatus_NoResults() {
        when(repository.findByServicePincodeAndDeliveryPartnerStatus("600002", true))
                .thenReturn(Collections.emptyList());

        List<AssignedDeliveryPartner> result = repository.findByServicePincodeAndDeliveryPartnerStatus("600002", true);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindFirstByStatus_ActiveAvailable() {
        when(repository.findFirstByStatus()).thenReturn(List.of(mockPartner));

        List<AssignedDeliveryPartner> result = repository.findFirstByStatus();
        assertEquals(1, result.size());
        assertEquals(true, result.get(0).getDeliveryPartnerStatus());
    }

    @Test
    void testFindFirstByStatus_NoAvailablePartners() {
        when(repository.findFirstByStatus()).thenReturn(Collections.emptyList());

        List<AssignedDeliveryPartner> result = repository.findFirstByStatus();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByCustomerPincodeAndStatus_ExactMatch() {
        when(repository.findByCustomerPincodeAndStatus("600001"))
                .thenReturn(List.of(mockPartner));

        List<AssignedDeliveryPartner> result = repository.findByCustomerPincodeAndStatus("600001");
        assertEquals(1, result.size());
        assertEquals("TN12ZZ7890", result.get(0).getVehicleNumber());
    }

    @Test
    void testFindByCustomerPincodeAndStatus_InvalidPincode() {
        when(repository.findByCustomerPincodeAndStatus("999999"))
                .thenReturn(Collections.emptyList());

        List<AssignedDeliveryPartner> result = repository.findByCustomerPincodeAndStatus("999999");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByServicePincodeAndDeliveryPartnerStatus_InactiveStatus() {
        when(repository.findByServicePincodeAndDeliveryPartnerStatus("600001", false))
                .thenReturn(Collections.emptyList());

        List<AssignedDeliveryPartner> result = repository.findByServicePincodeAndDeliveryPartnerStatus("600001", false);
        assertTrue(result.isEmpty());
    }
}
