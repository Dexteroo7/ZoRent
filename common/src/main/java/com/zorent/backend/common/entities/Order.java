package com.zorent.backend.common.entities;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.Rating;
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
import com.zorent.backend.common.Costing;
import com.zorent.backend.common.MiscUtils;
import com.zorent.backend.common.OrderPlacedUsing;
import com.zorent.backend.common.OrderStatus;
import com.zorent.backend.common.Tags;
import com.zorent.backend.common.TextUtils;
import com.zorent.backend.common.coupons.OtherCharges;
import com.zorent.backend.common.coupons.PaymentMethod;
import com.zorent.backend.common.coupons.Taxes;

import org.joda.time.DateTime;
import org.joda.time.Days;

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

    private static final double DELIVERY_CHARGE_RATE = 20;

    private enum LoadWithUserInfo {}

    private enum LoadWithAssignedStaff {}

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    @Id
    public Long id;

    public Tags productTag; //all costing information should be derived using the product tag

    public Days rentalDuration; //from x to y days

    public int quantity;

    public OrderStatus orderStatus;

    @Nonnull
    public EnumMap<OrderStatus, DateTime> timing = new EnumMap<>(OrderStatus.class); //track time

    public GeoPt deliveryLocation;

    @Nonnull
    public EnumMap<AddressTags, String> deliveryAddress = new EnumMap<>(AddressTags.class); //exact address where to deliver

    public GeoPt dispatchLocation;

    public PhoneNumber contactNumber; //delivery specific

    public Email contactEmail; //delivery specific

    public OrderPlacedUsing orderPlacedUsing; //order was placed by using this platform

    public PaymentMethod paymentMethod;

    public Rating deliveryRating;

    public String feedback;

    public Costing costing;

    public Coupon coupon;

    //order was placed by this user, becomes the parent
    @Parent
    @Load(LoadWithUserInfo.class)
    public Ref<Customer> customer;

    @Nonnull
    @Ignore
    public Set<DeliveryStaff> assignedStaff = new HashSet<>(); //will be empty initially

    /**
     * TODO should index this
     * optionally load the information of assigned delivery staff
     */
    @Nonnull
    @Load(LoadWithAssignedStaff.class)
    private Set<Ref<DeliveryStaff>> assignedStaffRef = new HashSet<>(); //will be empty initially

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    //add item to cart
    public static final class NewBuilder {

        private String customerId;
        private Tags productTag;
        private int quantity;
        private Days durationInDays;

        public NewBuilder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public NewBuilder setProductTag(Tags productTag) {
            this.productTag = productTag;
            return this;
        }

        public NewBuilder setDurationInDays(int durationInDays) {
            this.durationInDays = Days.days(durationInDays);
            return this;
        }

        public NewBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Order build() {

            if (TextUtils.isEmpty(customerId) ||
                    productTag == null ||
                    durationInDays == null || durationInDays.getDays() <= 0 ||
                    quantity <= 0)
                throw new IllegalArgumentException("Minimum required parameters not found");

            final Order order = new Order();
            order.customer = Ref.create(Key.create(Customer.class, customerId));
            order.productTag = productTag;
            order.quantity = quantity;
            order.rentalDuration = durationInDays;

            //add costing to the order
            final Costing costing = order.costing = new Costing();
            costing.originalCost = 0; //TODO

            costing.addCharge(OtherCharges.DELIVERY_FEE, order.calculateDeliveryCharge());
            costing.addCharge(OtherCharges.INSTALLATION_FEE, order.calculateDeliveryCharge()); //TODO
            costing.addCharge(OtherCharges.SERVICE_FEE, order.calculateDeliveryCharge()); //TODO

            costing.addTax(Taxes.SERVICE_TAX, 0); //TODO
            costing.addTax(Taxes.SERVICE_CHARGE, 0); //TODO
            costing.addTax(Taxes.VAT, 0); //TODO

            return order;
        }
    }

    //builder for order checkout
    public static final class CheckoutUpdater {

        private final Order toUpdate;

        public CheckoutUpdater(Order toUpdate) {
            this.toUpdate = toUpdate;
            toUpdate.orderStatus = OrderStatus.CHECKED_OUT;
        }

        public CheckoutUpdater setCurrentTime(DateTime currentTime) {
            toUpdate.timing.put(OrderStatus.CHECKED_OUT, currentTime);
            return this;
        }

        public CheckoutUpdater setDeliveryLocation(GeoPt deliveryLocation) {
            toUpdate.deliveryLocation = deliveryLocation;
            return this;
        }

        public CheckoutUpdater setDeliveryAddress(EnumMap<AddressTags, String> deliveryAddress) {
            toUpdate.deliveryAddress = deliveryAddress;
            return this;
        }

        public CheckoutUpdater setContactNumber(PhoneNumber contactNumber) {
            toUpdate.contactNumber = contactNumber;
            return this;
        }

        public CheckoutUpdater setContactEmail(Email contactEmail) {
            toUpdate.contactEmail = contactEmail;
            return this;
        }

        public CheckoutUpdater setOrderPlacedUsing(OrderPlacedUsing orderPlacedUsing) {
            toUpdate.orderPlacedUsing = orderPlacedUsing;
            return this;
        }

        public CheckoutUpdater setCoupon(Coupon coupon) {
            toUpdate.coupon = coupon;
            return this;
        }

        public CheckoutUpdater setPaymentMethod(PaymentMethod paymentMethod) {
            toUpdate.paymentMethod = paymentMethod;
            return this;
        }
    }

    //builder for order dispatched
    private static final class DispatchedUpdater {

        private final Order toUpdate;

        private DispatchedUpdater(Order toUpdate) {
            this.toUpdate = toUpdate;
            toUpdate.orderStatus = OrderStatus.DISPATCHED;
        }

        private DispatchedUpdater setAssignedStaff(Set<DeliveryStaff> assignedStaff) {
            toUpdate.assignedStaff = assignedStaff;
            return this;
        }

        private DispatchedUpdater setCurrentTime(DateTime currentTime) {
            toUpdate.timing.put(OrderStatus.DISPATCHED, currentTime);
            return this;
        }
    }

    //builder for order delivered
    private static final class DeliveredUpdater {

        private final Order toUpdate;

        private DeliveredUpdater(Order toUpdate) {
            this.toUpdate = toUpdate;
            toUpdate.orderStatus = OrderStatus.DELIVERED;
        }

        private DeliveredUpdater setCurrentTime(DateTime currentTime) {
            this.toUpdate.timing.put(OrderStatus.DELIVERED, currentTime);
            return this;
        }

        private DeliveredUpdater setDeliveryRating(Rating deliveryRating) {
            this.toUpdate.deliveryRating = deliveryRating;
            return this;
        }

        private DeliveredUpdater setFeedback(String feedback) {
            this.toUpdate.feedback = feedback;
            return this;
        }
    }


    private Order() {
        //do not use
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public double calculateDeliveryCharge() {

        return MiscUtils.distance(dispatchLocation, deliveryLocation) * DELIVERY_CHARGE_RATE;
    }

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private Tags getProductTag() {
        return productTag;
    }

    private void setProductTag(Tags productTag) {
        this.productTag = productTag;
    }

    private Days getRentalDuration() {
        return rentalDuration;
    }

    private int getQuantity() {
        return quantity;
    }

    private void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private OrderStatus getOrderStatus() {
        return orderStatus;
    }

    private void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    private EnumMap<OrderStatus, DateTime> getTiming() {
        return timing;
    }

    private void setTiming(EnumMap<OrderStatus, DateTime> timing) {
        this.timing = timing;
    }

    private void setRentalDuration(Days rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    private GeoPt getDeliveryLocation() {
        return deliveryLocation;
    }

    private void setDeliveryLocation(GeoPt deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    private EnumMap<AddressTags, String> getDeliveryAddress() {
        return deliveryAddress;
    }

    private void setDeliveryAddress(EnumMap<AddressTags, String> deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    private GeoPt getDispatchLocation() {
        return dispatchLocation;
    }

    private void setDispatchLocation(GeoPt dispatchLocation) {
        this.dispatchLocation = dispatchLocation;
    }

    private PhoneNumber getContactNumber() {
        return contactNumber;
    }

    private void setContactNumber(PhoneNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    private Email getContactEmail() {
        return contactEmail;
    }

    private void setContactEmail(Email contactEmail) {
        this.contactEmail = contactEmail;
    }

    private OrderPlacedUsing getOrderPlacedUsing() {
        return orderPlacedUsing;
    }

    private void setOrderPlacedUsing(OrderPlacedUsing orderPlacedUsing) {
        this.orderPlacedUsing = orderPlacedUsing;
    }

    private PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private Rating getDeliveryRating() {
        return deliveryRating;
    }

    private void setDeliveryRating(Rating deliveryRating) {
        this.deliveryRating = deliveryRating;
    }

    private String getFeedback() {
        return feedback;
    }

    private void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    private Costing getCosting() {
        return costing;
    }

    private void setCosting(Costing costing) {
        this.costing = costing;
    }

    private Customer getCustomer() {
        return customer.get();
    }

    private void setCustomer(Customer customer) {
        this.customer = Ref.create(customer);
    }
    
    private Coupon getCoupon() {
        return coupon;
    }

    private void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    private Set<Ref<DeliveryStaff>> getAssignedStaffRef() {
        return assignedStaffRef;
    }

    private void setAssignedStaffRef(Set<Ref<DeliveryStaff>> assignedStaffRef) {
        this.assignedStaffRef = assignedStaffRef;
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

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

    public void addDeliveryStaff(DeliveryStaff deliveryStaff) {

        final Ref<DeliveryStaff> ref = Ref.create(Key.create(deliveryStaff));
        assignedStaffRef.add(ref);
    }

    public void addDeliveryStaff(long id) {

        final Ref<DeliveryStaff> ref = Ref.create(Key.create(DeliveryStaff.class, id));
        assignedStaffRef.add(ref);
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////
}