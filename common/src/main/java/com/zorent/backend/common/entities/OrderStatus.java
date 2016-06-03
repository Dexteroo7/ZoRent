package com.zorent.backend.common.entities;

/**
 * Created by dexter on 03/06/2016.
 */
public enum OrderStatus {

    NEW, //order added to cart
    CHECKED_OUT, //order placed by user
    DISPATCHED, //order dispatched by zorent
    DELIVERED //order completed successfully
}
