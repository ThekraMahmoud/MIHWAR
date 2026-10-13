package com.example.expo.Controller;

import com.example.expo.Api.ApiResponse;
import com.example.expo.DTO.SellerRequest;
import com.example.expo.Model.Merchants;
import com.example.expo.Service.SellerersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/merchant")
public class MerchantController {

    private final SellerersService sellerersService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllMerchant(){
        List<Merchants> merchantList= sellerersService.getAll();
        return ResponseEntity.status(200).body(merchantList);
    }
    @PostMapping("/add")
    public ResponseEntity<?>addMerchant(@RequestBody @Valid SellerRequest merchant){
        sellerersService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("added done"));
    }

    @PutMapping("/update/{merchantId}")
    public ResponseEntity<?>updateMerchant(@PathVariable Integer merchantId,@RequestBody @Valid SellerRequest merchant){
        sellerersService.updateMerchant(merchantId,merchant);
        return ResponseEntity.status(200).body(new ApiResponse("updated done"));
    }
    @DeleteMapping("/delete/{merchantId}")
    public ResponseEntity<?>deleteMerchant(@PathVariable Integer merchantId){
        sellerersService.deleteMerchant(merchantId);
        return ResponseEntity.status(200).body(new ApiResponse("deleted done"));
    }


}