package com.zorent.backend.common;

import com.zorent.backend.common.coupons.DiscountType;
import com.zorent.backend.common.coupons.OtherCharges;
import com.zorent.backend.common.coupons.Taxes;

import java.util.EnumMap;

/**
 * Created by dexter on 03/06/2016.
 */
public final class Costing {

    public static Costing ZERO() {
        return new Costing(); //mutable instance
    }

    public double originalCost;
    public EnumMap<DiscountType, Double> appliedReductions = new EnumMap<>(DiscountType.class);
    public EnumMap<Taxes, Double> taxes = new EnumMap<>(Taxes.class);
    public EnumMap<OtherCharges, Double> otherCharges = new EnumMap<>(OtherCharges.class);

    public double getOriginalCost() {
        return originalCost;
    }

    public void setOriginalCost(double originalCost) {
        this.originalCost = originalCost;
    }

    public void addDiscount(DiscountType type, double discount) {
        appliedReductions.put(type, discount);
    }

    public void addTax(Taxes tax, double value) {
        taxes.put(tax, value);
    }

    public void addCharge(OtherCharges charge, double value) {
        otherCharges.put(charge, value);
    }

    private EnumMap<DiscountType, Double> getAppliedReductions() {
        return appliedReductions;
    }

    private void setAppliedReductions(EnumMap<DiscountType, Double> appliedReductions) {
        this.appliedReductions = appliedReductions;
    }

    private EnumMap<Taxes, Double> getTaxes() {
        return taxes;
    }

    private void setTaxes(EnumMap<Taxes, Double> taxes) {
        this.taxes = taxes;
    }

    private EnumMap<OtherCharges, Double> getOtherCharges() {
        return otherCharges;
    }

    private void setOtherCharges(EnumMap<OtherCharges, Double> otherCharges) {
        this.otherCharges = otherCharges;
    }
}
