package com.zorent.backend.client.cart;

import com.zorent.backend.common.coupons.PaymentMethod;
import com.zorent.backend.common.OrderPlacedUsing;

/**
 * Created by dexter on 03/06/2016.
 */
final class ApplyCoupon {

    String couponId;
    PaymentMethod paymentMethod;
    OrderPlacedUsing orderPlacedUsing;

    private ApplyCoupon() {
    }

    private String getCouponId() {
        return couponId;
    }

    private void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    private PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private OrderPlacedUsing getOrderPlacedUsing() {
        return orderPlacedUsing;
    }

    private void setOrderPlacedUsing(OrderPlacedUsing orderPlacedUsing) {
        this.orderPlacedUsing = orderPlacedUsing;
    }
}
