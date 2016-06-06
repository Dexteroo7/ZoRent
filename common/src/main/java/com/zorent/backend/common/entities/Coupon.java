package com.zorent.backend.common.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.zorent.backend.common.Costing;
import com.zorent.backend.common.coupons.DiscountType;
import com.zorent.backend.common.coupons.UsageType;

import org.joda.time.DateTime;

import java.util.EnumSet;

/**
 * Created by dexter on 02/06/2016.
 */

@Entity
@Cache
@Index
public class Coupon {

    @Id
    public String id;

    public DateTime expiryDate;

    public float discountRatio;
    public int minimumQuantity = -1; // > 0 if applicable

    public EnumSet<UsageType> usageRestrictions = EnumSet.noneOf(UsageType.class);

    public EnumSet<DiscountType> discounts = EnumSet.noneOf(DiscountType.class);

    ////////////////////////////////////////////
    ////////////////////////////////////////////


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(DateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public float getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(float discountRatio) {
        this.discountRatio = discountRatio;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public EnumSet<UsageType> getUsageRestrictions() {
        return usageRestrictions;
    }

    public void setUsageRestrictions(EnumSet<UsageType> usageRestrictions) {
        this.usageRestrictions = usageRestrictions;
    }

    public EnumSet<DiscountType> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(EnumSet<DiscountType> discounts) {
        this.discounts = discounts;
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public Costing applyCoupon(Order order) {

        for (UsageType usageType : usageRestrictions)
            if (!usageType.apply(order))
                return order.costing; //coupon not applicable

        for (DiscountType discountType : discounts)
            discountType.transform(order);

        return order.costing;
    }

    public static final class InvalidCouponException extends IllegalArgumentException {

        public InvalidCouponException() {
            super("Coupon code does not exist");
        }
    }

    public static final class ExpiredCouponException extends IllegalArgumentException {

        public ExpiredCouponException() {
            super("Coupon has expired");
        }
    }
}