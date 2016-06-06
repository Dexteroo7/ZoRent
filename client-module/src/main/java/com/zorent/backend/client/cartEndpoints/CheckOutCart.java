package com.zorent.backend.client.cartEndpoints;

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

}
