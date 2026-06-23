package com.rajnish.razorpay.merchant.mapper;

import com.rajnish.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.rajnish.razorpay.merchant.dto.response.MerchantResponse;
import com.rajnish.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntity(MerchantSignupRequest request);

    MerchantResponse toResponse(Merchant merchant);
}
