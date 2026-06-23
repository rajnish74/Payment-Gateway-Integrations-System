package com.rajnish.razorpay.payment.mapper;

import com.rajnish.razorpay.payment.dto.response.OrderResponse;
import com.rajnish.razorpay.payment.entity.OrderRecord;
import jakarta.persistence.criteria.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toOrderResponse(OrderRecord orderRecord);
}
