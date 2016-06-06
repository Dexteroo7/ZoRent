package com.zorent.backend.common.coupons;

import com.zorent.backend.common.entities.Order;

/**
 * Created by dexter on 02/06/2016.
 */
public enum DiscountType implements Transform<Order> {

    /**
     * Add the delivery fee as 1.0 discount (100%)
     */
    FREE_DELIVERY {
        @Override
        public void transform(Order order) {
            order.costing.addDiscount(FREE_DELIVERY, order.calculateDeliveryCharge());
        }
    },

    /**
     * Get discount value from coupon and multiply by
     */
    STRAIGHT_DISCOUNT {
        @Override
        public void transform(Order order) {
            order.costing.addDiscount(STRAIGHT_DISCOUNT, order.costing.originalCost *= order.coupon.discountRatio);
        }
    }

}