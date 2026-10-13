package com.example.expo.Service;

import com.example.expo.Api.ApiException;
import com.example.expo.DTO.MerchantWalletResponse;
import com.example.expo.Model.MerchantWallets;
import com.example.expo.Model.Merchants;
import com.example.expo.Repository.MerchantRepository;
import com.example.expo.Repository.SellersWalletsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerersWalletsService {

    private final SellersWalletsRepository merchantWalletsRepository;
    private final MerchantRepository merchantRepository;

//get all
    public List<MerchantWalletResponse> getAll() {
        List<MerchantWallets> wallets = merchantWalletsRepository.findAll();
        List<MerchantWalletResponse> responses = new ArrayList<>();

        for (int i = 0; i < wallets.size(); i++) {//مشينا ع كل الميرشنت ولت واضفناها داخل الريسبونس واعدنا فقط القيم الي نحتاجها في الرسيبونس
            responses.add(toResponse(wallets.get(i)));
        }

        return responses;
    }

//اسندت القيم الي بالمحفظة الى القيم بالرسيبونس
    private MerchantWalletResponse toResponse(MerchantWallets wallet) {
        return new MerchantWalletResponse(
                wallet.getId(),
                wallet.getMerchant().getId(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getStatus()
        );
    }

    @Transactional
    public void deleteWallet(Integer walletId) {
        MerchantWallets wallet = merchantWalletsRepository.findMerchantWalletsById(walletId);

        if (wallet == null) {
            throw new ApiException("wallet id not found");
        }
        //bigdecimal.zero يقارن حتى بعدد المنازل وليس فقط الرقم
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ApiException("cannot delete wallet with a non-zero balance");
        }
        wallet.getMerchant().setMerchantWallets(null);//حذفنا المحفظة من البائع
        merchantWalletsRepository.delete(wallet);
    }


    @Transactional
    public void createWallet(Integer merchantId) {
        Merchants merchant = merchantRepository.findMerchantById(merchantId);//البائع موجود
        if(merchant==null){
            throw new ApiException("merchant id not found");
        }

        if (merchantWalletsRepository.existsByMerchant_Id(merchantId)) {//اذا كان عنده بطاقة سابقة ممنوع يسجل من جديد
            throw new ApiException("merchant already has a wallet");
        }

        LocalDateTime now = LocalDateTime.now();

        MerchantWallets wallet = new MerchantWallets();//انشاء المحفظة


        wallet.setMerchant(merchant);//اضفناها للبائع
        wallet.setBalance(BigDecimal.ZERO);//لازم نسند قيمة للبلنس ونبدأ بصفر والتعديل هيكون بالشراء والايداع
        wallet.setCurrency("SAR");
        wallet.setStatus("ACTIVE");
        wallet.setCreated_at(now);
        wallet.setUpdated_at(now);

        merchantWalletsRepository.save(wallet);
        merchant.setMerchantWallets(wallet);
    }

//العملة - وقت الانشاء - الاي دي - الرصيد , ما ينفع نعدلها بالابديت لازم تتعدل لحالها
    @Transactional
    public void updateWallet(Integer walletId, String status) {
        MerchantWallets wallet = merchantWalletsRepository.findMerchantWalletsById(walletId);

        if (wallet == null) {//نتأكد ان المحفظة موجودة
            throw new ApiException("wallet id not found");
        }

        if (status == null) {
            throw new ApiException("status is required");
        }

        if (!status.equals("ACTIVE") && !status.equals("SUSPENDED")) {//مجبور يدخل فقط هالخيارين بالسسيتم
            throw new ApiException("status must be ACTIVE or SUSPENDED");
        }

        wallet.setStatus(status);//نعدل حالة المحفظة
        wallet.setUpdated_at(LocalDateTime.now());//نحدث وقت التعديل

        merchantWalletsRepository.save(wallet);
    }







}