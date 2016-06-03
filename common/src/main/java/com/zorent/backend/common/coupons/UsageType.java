package com.zorent.backend.common.coupons;

import com.google.common.base.Predicate;
import com.zorent.backend.common.Tags;
import com.zorent.backend.common.entities.Order;
import com.zorent.backend.common.entities.OrderPlacedUsing;

import javax.annotation.Nullable;

import static com.zorent.backend.common.OfyService.ofy;

;

/**
 * Each enum defines a singular condition for a coupon,
 * complex conditions will be derived from these.
 * <p/>
 * Since we don't have too many products, or scope for too many products,
 * this way will work.
 * <p/>
 * Also we don't explicitly maintain a table of products,
 * <p/>
 * Created by dexter on 02/06/2016.
 */
public enum UsageType implements Predicate<Order>, ErrorMessage {

    /**
     * No previous orders should have been placed
     * by the user
     */
    FIRST_ORDER {
        @Override
        public String getMessage() {
            return "This coupon can only be applied on first time orders";
        }

        @Override
        public boolean apply(@Nullable Order order) {
            return ofy().load().type(Order.class)
                    .ancestor(order.customer.getKey())
                    .limit(1).count() == 0;
        }
    },

    /**
     * Applied coupon can only be used once,
     * we verify this from order history of user
     */
    ONE_TIME_USE {
        @Override
        public String getMessage() {
            return "This coupon can only be used once";
        }

        @Override
        public boolean apply(@Nullable Order order) {
            return ofy().load().type(Order.class)
                    .ancestor(order.customer.getKey())
                    .filter("coupon", order.coupon)
                    .limit(1).count() == 0;
        }
    },

    /**
     * Applied coupon can only be used if
     * it is a corporate order
     */
    CORPORATE_ONLY {
        @Override
        public String getMessage() {
            return "Invalid coupon";
        }

        @Override
        public boolean apply(@Nullable Order order) {
            return order.orderPlacedUsing == OrderPlacedUsing.CORPORATE;
        }
    },

    /**
     * Applied coupon can only be used if
     * order was placed from app
     */
    APP_ONLY {
        @Override
        public String getMessage() {
            return "Coupon valid for app orders only";
        }

        @Override
        public boolean apply(@Nullable Order input) {
            return input.orderPlacedUsing == OrderPlacedUsing.APP;
        }
    },

    MINIMUM_QUANTITY {
        @Override
        public boolean apply(@Nullable Order order) {
            return order.quantity >= order.coupon.minimumQuantity;
        }

        @Override
        public String getMessage() {
            return "Coupon requires a minimum of x items";
        }
    },

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    /**
     * Applied coupon can only be used
     * on electronic items
     */
    ONLY_ON_ELECTRONICS {
        @Override
        public String getMessage() {
            return "Coupon valid only for electronics";
        }

        @Override
        public boolean apply(@Nullable Order input) {
            return Tags.ELECTRONICS.isParent(input.productTag);
        }
    },

    /**
     * Applied coupon can only be used
     * on AC's
     */
    ONLY_ON_AC {
        @Override
        public String getMessage() {
            return "Coupon valid only for AC's";
        }

        @Override
        public boolean apply(@Nullable Order input) {
            return Tags.AC.isParent(input.productTag);
        }
    },

    /**
     * Applied coupon can only be used
     * on Laptop's
     */
    ONLY_ON_LAPTOP {
        @Override
        public String getMessage() {
            return "Coupon valid only for Laptop's";
        }

        @Override
        public boolean apply(@Nullable Order input) {
            return Tags.LAPTOP.isParent(input.productTag);
        }
    },

    /**
     * Applied coupon can only be used
     * on Laptop's
     */
    ONLY_ON_MACBOOK_MF839HN {
        @Override
        public String getMessage() {
            return "Coupon valid only for MACBOOK_MF839HN";
        }

        @Override
        public boolean apply(@Nullable Order input) {
            return input.productTag == Tags.MACBOOK_MF839HN;
        }
    }
}