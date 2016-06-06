package com.zorent.backend.client.cartEndpoints;

import com.zorent.backend.common.Tags;

/**
 * Created by dexter on 03/06/2016.
 */
final class AddToCart {

    Tags productTag;
    int durationInDays;
    int quantity;

    /**
     * @param productTag     which item is being added to cart
     * @param durationInDays rental duration
     * @param quantity       how many items
     */
    AddToCart(Tags productTag, int durationInDays, int quantity) {
        this.productTag = productTag;
        this.durationInDays = durationInDays;
        this.quantity = quantity;
    }

    private Tags getProductTag() {
        return productTag;
    }

    private void setProductTag(Tags productTag) {
        this.productTag = productTag;
    }

    private int getDurationInDays() {
        return durationInDays;
    }

    private void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    private int getQuantity() {
        return quantity;
    }

    private void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
