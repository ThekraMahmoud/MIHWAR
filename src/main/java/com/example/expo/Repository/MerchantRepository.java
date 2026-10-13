package com.example.expo.Repository;

import com.example.expo.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Seller, Integer> {
    Seller findMerchantById(Integer id);
}