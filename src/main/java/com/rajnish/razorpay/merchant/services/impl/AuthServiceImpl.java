package com.rajnish.razorpay.merchant.services.impl;

import com.rajnish.razorpay.common.enums.MerchantStatus;
import com.rajnish.razorpay.common.enums.UserRole;
import com.rajnish.razorpay.common.exceptions.DuplicateResourceException;
import com.rajnish.razorpay.common.exceptions.ResourceNotFoundException;
import com.rajnish.razorpay.merchant.dto.request.LoginRequest;
import com.rajnish.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.rajnish.razorpay.merchant.dto.response.LoginResponse;
import com.rajnish.razorpay.merchant.dto.response.MerchantResponse;
import com.rajnish.razorpay.merchant.entity.AppUser;
import com.rajnish.razorpay.merchant.entity.Merchant;
import com.rajnish.razorpay.merchant.mapper.MerchantMapper;
import com.rajnish.razorpay.merchant.repository.ApiKeyRepository;
import com.rajnish.razorpay.merchant.repository.AppUserRepository;
import com.rajnish.razorpay.merchant.repository.MerchantRepository;
import com.rajnish.razorpay.merchant.security.JwtUtils;
import com.rajnish.razorpay.merchant.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository  merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils  jwtUtils;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if (merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","Merchant with email "+request.email()+" already exists");
        }

        Merchant merchant=merchantMapper.toEntity(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);

        merchant=merchantRepository.save(merchant);

        AppUser appUser=AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);


        return merchantMapper.toResponse(merchant);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),request.password())
        );

        AppUser appUser=appUserRepository.findByEmail(request.email())
                .orElseThrow(()->new ResourceNotFoundException("USER_NOT_FOUND","User with email "+request.email()+" not found"));

        String token= jwtUtils.generateAccessToken(
                request.email(),
                appUser.getMerchant().getId(),
                appUser.getRole().toString()
        );
        return new LoginResponse(token);
    }
}
