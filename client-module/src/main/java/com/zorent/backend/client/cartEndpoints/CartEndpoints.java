package com.zorent.backend.client.cartEndpoints;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.Key;
import com.zorent.backend.common.Costing;
import com.zorent.backend.common.MiscUtils;
import com.zorent.backend.common.OrderStatus;
import com.zorent.backend.common.TextUtils;
import com.zorent.backend.common.ZoAuthenticator;
import com.zorent.backend.common.entities.Coupon;
import com.zorent.backend.common.entities.Customer;
import com.zorent.backend.common.entities.Order;
import com.zorent.backend.common.objectWrappers.ZoString;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.zorent.backend.common.OfyService.ofy;

/**
 * Created by dexter on 03/06/2016.
 */

@Api(
        name = "cart",
        version = "v1",
        authenticators = {ZoAuthenticator.class},
        namespace = @ApiNamespace(
                ownerDomain = "cartEndpoints.client.backend.zorent.com",
                ownerName = "cartEndpoints.client.backend.zorent.com",
                packagePath = ""
        )
)
public class CartEndpoints {

    private static final Logger LOGGER = Logger.getLogger(CartEndpoints.class.getName());

    /**
     * We create a new cart entry using this endpoint.
     * Essentially a new order is generated and saved in the DB.
     * Status of this order is set to {@code OrderStatus.NEW}
     *
     * @param user which user has called the endpoint
     * @return the orderID
     */
    @SuppressWarnings({"ResourceParameter"})
    @ApiMethod(name = "addToCart")
    public ZoString addToCart(@Nullable User user,
                              AddToCart addToCart) throws UnauthorizedException {

        if (addToCart == null || addToCart.productTag == null || addToCart.durationInDays <= 0 || addToCart.quantity <= 0)
            throw new IllegalArgumentException("Insufficient parameters provided");

        final String customerId = MiscUtils.getCustomerId(user);

        //load current cart
        final Iterable<Order> cart = ofy().load().type(Order.class)
                .ancestor(Key.create(Customer.class, customerId)) //orders belong to this user
                .filter("deliveryInfo.orderStatus", OrderStatus.NEW) //current cart
                .iterable();

        //check if the order is overlapping
        for (Order order : cart) {
            if (order.productTag == addToCart.productTag && addToCart.durationInDays == order.rentalDuration.getDays()) {
                order.quantity += addToCart.quantity;
                return ZoString.of(order.id);
            }
        }

        final Order newCartEntry = new Order.NewBuilder()
                .setCustomerId(customerId)
                .setDurationInDays(addToCart.durationInDays)
                .setProductTag(addToCart.productTag)
                .setQuantity(addToCart.quantity).build();

        //return order id
        return ZoString.of(ofy().save().entity(newCartEntry).now().getString());
    }

    /**
     * Apply given coupon code to the cart
     *
     * @param user        the user who applied
     * @param applyCoupon the coupon details
     * @return new mapping of
     */
    @SuppressWarnings("ResourceParameter")
    @ApiMethod(name = "applyCouponToCart")
    public Map<Long, Costing> applyCouponToCart(@Nullable User user,
                                                ApplyCoupon applyCoupon) throws UnauthorizedException {

        final String customerId = MiscUtils.getCustomerId(user);

        //check if coupon is valid
        final Coupon coupon = ofy().load().type(Coupon.class).id(applyCoupon.couponId).now();
        if (coupon == null)
            throw new Coupon.InvalidCouponException();
        if (coupon.expiryDate.isBeforeNow())
            throw new Coupon.ExpiredCouponException();

        //load current cart
        final Iterable<Order> cart = ofy().load().type(Order.class)
                .ancestor(Key.create(Customer.class, customerId)) //orders belong to this user
                .filter("deliveryInfo.orderStatus", OrderStatus.NEW) //current cart
                .iterable();

        final Map<Long, Costing> toReturn = new HashMap<>();

        //apply the coupon to all orders
        for (Order order : cart) {

            order.orderPlacedUsing = applyCoupon.orderPlacedUsing;
            order.paymentMethod = applyCoupon.paymentMethod;

            coupon.applyCoupon(order);
            toReturn.put(order.id, order.costing);
        }

        //return the costing after applying coupon
        return toReturn;
    }

    @SuppressWarnings("ResourceParameter")
    @ApiMethod(name = "checkOutCurrentCart")
    public Map<Long, Costing> checkOutCurrentCart(@Nullable User user,
                                                  CheckOutCart checkOutCart) throws UnauthorizedException {

        final String customerId = MiscUtils.getCustomerId(user);

        //check if coupon is valid
        final Coupon coupon;

        if (TextUtils.isEmpty(checkOutCart.couponId))
            coupon = null;
        else {

            coupon = ofy().load().type(Coupon.class).id(checkOutCart.couponId).now();
            if (coupon == null)
                throw new Coupon.InvalidCouponException();
            if (coupon.expiryDate.isBeforeNow())
                throw new Coupon.ExpiredCouponException();

        }
        //load current cart
        final Iterable<Order> cart = ofy().load().type(Order.class)
                .ancestor(Key.create(Customer.class, customerId)) //orders belong to this user
                .filter("deliveryInfo.orderStatus", OrderStatus.NEW) //current cart
                .iterable();

        //apply the coupon to all orders
        if (coupon != null)
            for (Order order : cart)
                coupon.applyCoupon(order);

        final Map<Long, Costing> toReturn = new HashMap<>();

        for (Order order : cart) {

            new Order.CheckoutUpdater(order)
                    .setContactEmail(checkOutCart.contactEmail)
                    .setContactNumber(checkOutCart.contactNumber)
                    .setCoupon(coupon)
                    .setCurrentTime(DateTime.now())
                    .setDeliveryAddress(checkOutCart.deliveryAddress)
                    .setDeliveryLocation(checkOutCart.deliveryLocation)
                    .setOrderPlacedUsing(checkOutCart.orderPlacedUsing)
                    .setPaymentMethod(checkOutCart.paymentMethod);

            toReturn.put(order.id, order.costing);
        }

        //return the costing after checking out
        return toReturn;
    }

}
