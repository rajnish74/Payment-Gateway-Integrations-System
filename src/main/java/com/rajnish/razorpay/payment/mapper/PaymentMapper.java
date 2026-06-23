package com.rajnish.razorpay.payment.mapper;

import com.rajnish.razorpay.payment.dto.response.PaymentResponse;
import com.rajnish.razorpay.payment.entity.Payments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

   @Mapping(target="orderId",source="order.id")
   //@Mapping(target="merchant_id",source="merchantId")
    PaymentResponse toResponse(Payments payments);

    @Mapping(target="orderId",source="order.id")
   List<PaymentResponse> toResponseList(List<Payments> paymentsList);
}
