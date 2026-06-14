package com.rajnish.razorpay.common.enums;

import jakarta.persistence.Entity;

public enum RefundStatus {
    PENDING,
    PROCESSING,
    PROCESSED,
    FAILED,
}
