package com.zorent.backend.common.entities;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.zorent.backend.common.coupons.DiscountType;
import com.zorent.backend.common.coupons.UsageType;

import org.joda.time.Duration;

import java.util.EnumSet;

/**
 * Created by dexter on 02/06/2016.
 */

@Entity
@Cache
@Index
public class Coupon {

    @Id
    public Long id;

    public Duration couponDuration; //when activated ? when expires ?

    public float discountRatio;
    public int minimumQuantity = -1; // > 0 if applicable

    public EnumSet<UsageType> usageRestrictions;

    public EnumSet<DiscountType> discounts;
}
