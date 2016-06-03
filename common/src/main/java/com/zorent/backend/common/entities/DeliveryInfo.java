package com.zorent.backend.common.entities;

import com.google.appengine.api.datastore.Rating;
import com.googlecode.objectify.annotation.Index;

import org.joda.time.DateTime;

/**
 * Created by dexter on 03/06/2016.
 */ //TODO add helper methods for evolving DeliveryInfo
@Index
public final class DeliveryInfo {

    public static final DeliveryInfo NEW = new DeliveryInfo();

    public OrderStatus orderStatus = OrderStatus.NEW;

    //confirmed stage
    public DateTime confirmedWhen;

    //dispatched stage
    public DateTime dispatchedWhen; //person who confirms is also the one who dispatches

    //delivered stage
    public DateTime deliveredWhen;
    public Rating deliveryRating;
    public String feedback;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public DateTime getConfirmedWhen() {
        return confirmedWhen;
    }

    public void setConfirmedWhen(DateTime confirmedWhen) {
        this.confirmedWhen = confirmedWhen;
    }

    public DateTime getDispatchedWhen() {
        return dispatchedWhen;
    }

    public void setDispatchedWhen(DateTime dispatchedWhen) {
        this.dispatchedWhen = dispatchedWhen;
    }

    public DateTime getDeliveredWhen() {
        return deliveredWhen;
    }

    public void setDeliveredWhen(DateTime deliveredWhen) {
        this.deliveredWhen = deliveredWhen;
    }

    public Rating getDeliveryRating() {
        return deliveryRating;
    }

    public void setDeliveryRating(Rating deliveryRating) {
        this.deliveryRating = deliveryRating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    //TODO what should be the logic for equals ?
    @Override
    public boolean equals(Object other) {
        return other instanceof DeliveryInfo;
    }

    //TODO what should be the logic for hashcode ?
    @Override
    public int hashCode() {
        return Integer.MAX_VALUE;
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

}
