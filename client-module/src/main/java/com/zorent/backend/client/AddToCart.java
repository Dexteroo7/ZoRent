package com.zorent.backend.client;

import com.zorent.backend.common.Tags;

import org.joda.time.Days;

/**
 * Created by dexter on 03/06/2016.
 */
public class AddToCart {

    Tags productTag;
    Days rentalDuration;
    int quantity;

    public Tags getProductTag() {
        return productTag;
    }

    public void setProductTag(Tags productTag) {
        this.productTag = productTag;
    }

    public Days getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(Days rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
