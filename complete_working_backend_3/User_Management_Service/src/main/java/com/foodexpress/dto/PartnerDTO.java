package com.foodexpress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {
    private int userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String vehicleNumber;
    private String address;
    private String servicePincode;
    private Boolean deliveryPartnerStatus;
}

