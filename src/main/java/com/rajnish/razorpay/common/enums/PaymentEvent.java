package com.rajnish.razorpay.common.enums;

public enum PaymentEvent {
    AUTHORIZE_ATTEMPT,
    AUTHORIZE_SUCCESS,
    AUTHORIZE_FAILURE,
    CAPTURE_SUCCESS,
    CAPTURE_REQUEST,
    CAPTURE_FAILURE,
    REFUND_SUCCESS,
    REFUND_FAILURE,
    SETTLE,
    CANCEL,
    CAPTURE_TIMEOUT,

}
