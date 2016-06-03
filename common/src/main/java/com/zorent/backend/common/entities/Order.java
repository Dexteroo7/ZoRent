package com.zorent.backend.common.entities;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.PhoneNumber;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.Parent;
import com.zorent.backend.common.AddressTags;
import com.zorent.backend.common.MiscUtils;
import com.zorent.backend.common.Tags;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Created by dexter on 02/06/2016.
 */

@Entity
@Cache
@Index
public class Order {

    public static final double DELIVERY_CHARGE_RATE = 20;

    public enum LoadWithUserInfo {}

    public enum LoadWithAssignedStaff {}

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    @Id
    public Long id;

    public Tags productTag; //all costing information should be derived using the product tag

    public DateTime placedWhen;

    public DateTime deliveredWhen;

    public Days rentalDuration; //from x to y days

    public GeoPt deliveryLocation;

    public EnumMap<AddressTags, String> deliveryAddress; //exact address where to deliver

    public GeoPt dispatchLocation;

    public PhoneNumber contactNumber; //delivery specific

    public Email contactEmail; //delivery specific

    public OrderPlacedUsing orderPlacedUsing; //order was placed by using this platform

    public DeliveryInfo deliveryInfo = DeliveryInfo.NEW; //initially not delivered

    public Costing costing = Costing.ZERO;

    public int quantity;

    //order was placed by this user, becomes the parent
    @Parent
    @Load(LoadWithUserInfo.class)
    public Ref<Customer> customer;

    public Coupon coupon;

    @Nonnull
    @Ignore
    public Set<DeliveryStaff> assignedStaff = Collections.emptySet(); //will be empty initially

    /**
     * TODO should index this
     * optionally load the information of assigned delivery staff
     */
    @Load(LoadWithAssignedStaff.class)
    private Set<Ref<DeliveryStaff>> assignedStaffRef = Collections.emptySet(); //will be empty initially

    private Order() {
        //do not use
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public double calculateDeliveryCharge() {

        return MiscUtils.distance(dispatchLocation, deliveryLocation) * DELIVERY_CHARGE_RATE;
    }

    public Customer getCustomer() {
        return customer.get();
    }

    public void setCustomer(Customer customer) {
        this.customer = Ref.create(customer);
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    private Set<Ref<DeliveryStaff>> getAssignedStaffRef() {
        return assignedStaffRef;
    }

    private void setAssignedStaffRef(Set<Ref<DeliveryStaff>> assignedStaffRef) {
        this.assignedStaffRef = assignedStaffRef;
    }

//    @OnSave
//    @OnLoad
//    private void checker() {
//
//        //order should be verified before storing
//
//        if (!productTag.getParents().contains(Tags.PRODUCT))
//            throw new IllegalArgumentException("A product is expected");
//
//        if (placedWhen == null || placedWhen.isAfter(DateTime.now()))
//            throw new IllegalArgumentException("Enter date of order");
//    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    @OnLoad
    private void deRef() {

        for (Ref<DeliveryStaff> deliveryStaffRef : assignedStaffRef)
            if (deliveryStaffRef.isLoaded()) {

                //initialize assignedStaff now, compiler should optimize
                if (assignedStaff.size() == 0)
                    assignedStaff = new HashSet<>(assignedStaffRef.size());
                assignedStaff.add(deliveryStaffRef.get());
            }
    }

    public void addSubmission(DeliveryStaff deliveryStaff) {

        final Ref<DeliveryStaff> ref = Ref.create(Key.create(deliveryStaff));
        assignedStaffRef.add(ref);
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

}