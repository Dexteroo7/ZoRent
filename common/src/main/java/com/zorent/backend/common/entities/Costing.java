package com.zorent.backend.common.entities;

import com.zorent.backend.common.coupons.DiscountType;
import com.zorent.backend.common.coupons.Taxes;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by dexter on 03/06/2016.
 */
public final class Costing {

    public static final Costing ZERO = new Costing();

    public double originalCost;
    public Map<DiscountType, Double> appliedReductions = Collections.emptyMap();
    public Map<Taxes, Double> taxes = Collections.emptyMap();
    public Map<String, Double> otherCharges = Collections.emptyMap();

    public double getOriginalCost() {
        return originalCost;
    }

    public void setOriginalCost(double originalCost) {
        this.originalCost = originalCost;
    }

    public void addDiscount(DiscountType type, double discount) {

        if (appliedReductions.size() == 0)
            appliedReductions = new EnumMap<>(DiscountType.class);
        appliedReductions.put(type, discount);
    }

    public void addTax(Taxes tax, double value) {

        if (taxes.size() == 0)
            taxes = new EnumMap<>(Taxes.class);
        taxes.put(tax, value);
    }
}
