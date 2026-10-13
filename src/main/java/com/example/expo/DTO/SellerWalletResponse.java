package com.example.expo.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor

//للعرض
public class MerchantWalletResponse {

    private Integer id;
    private Integer merchant_id;
    private BigDecimal balance;
    private String currency;
    private String status;


}