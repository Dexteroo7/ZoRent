package com.zorent.backend.client.cart;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.PhoneNumber;
import com.zorent.backend.common.AddressTags;
import com.zorent.backend.common.coupons.PaymentMethod;
import com.zorent.backend.common.OrderPlacedUsing;

import java.util.EnumMap;

/**
 * Created by dexter on 03/06/2016.
 */
final class CheckOutCart {

    PaymentMethod paymentMethod;
    GeoPt deliveryLocation;
    EnumMap<AddressTags, String> deliveryAddress;
    PhoneNumber contactNumber;
    Email contactEmail;
    OrderPlacedUsing orderPlacedUsing;
    String couponId;

    private PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private GeoPt getDeliveryLocation() {
        return deliveryLocation;
    }

    private void setDeliveryLocation(GeoPt deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    private EnumMap<AddressTags, String> getDeliveryAddress() {
        return deliveryAddress;
    }

    private void setDeliveryAddress(EnumMap<AddressTags, String> deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    private PhoneNumber getContactNumber() {
        return contactNumber;
    }

    private void setContactNumber(PhoneNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    private Email getContactEmail() {
        return contactEmail;
    }

    private void setContactEmail(Email contactEmail) {
        this.contactEmail = contactEmail;
    }

    private OrderPlacedUsing getOrderPlacedUsing() {
        return orderPlacedUsing;
    }

    private void setOrderPlacedUsing(OrderPlacedUsing orderPlacedUsing) {
        this.orderPlacedUsing = orderPlacedUsing;
    }

    private String getCouponId() {
        return couponId;
    }

    private void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
