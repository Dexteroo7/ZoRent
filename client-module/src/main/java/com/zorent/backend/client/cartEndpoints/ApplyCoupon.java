package com.zorent.backend.client.cartEndpoints;

import com.zorent.backend.common.coupons.PaymentMethod;
import com.zorent.backend.common.OrderPlacedUsing;

/**
 * Created by dexter on 03/06/2016.
 */
public class ApplyCoupon {

    public String couponId;
    public PaymentMethod paymentMethod;
    public OrderPlacedUsing orderPlacedUsing;
}
