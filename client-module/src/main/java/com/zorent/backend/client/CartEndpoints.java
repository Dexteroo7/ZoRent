package com.zorent.backend.client;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.googlecode.objectify.Key;
import com.zorent.backend.common.MiscUtils;
import com.zorent.backend.common.Tags;
import com.zorent.backend.common.ZoAuthenticator;
import com.zorent.backend.common.entities.Customer;
import com.zorent.backend.common.entities.Order;
import com.zorent.backend.common.entities.OrderStatus;
import com.zorent.backend.common.objectWrappers.ZoString;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.logging.Logger;

import static com.zorent.backend.client.OfyService.ofy;

/**
 * Created by dexter on 03/06/2016.
 */

@Api(
        name = "cart",
        version = "v1",
        authenticators = {ZoAuthenticator.class},
        namespace = @ApiNamespace(
                ownerDomain = "client.backend.zorent.com",
                ownerName = "client.backend.zorent.com",
                packagePath = ""
        )
)
public class CartEndpoints {

    private static final Logger LOGGER = Logger.getLogger(CartEndpoints.class.getName());

    @ApiMethod(name = "addToCart")
    public ZoString addToCart(@Nullable User user,

                              @Named("productTag") Tags productTag,
                              @Named("durationInDays") int durationInDays,
                              @Named("quantity") int quantity) {

        if (productTag == null || durationInDays == 0 || quantity == 0)
            throw new IllegalArgumentException("Insufficient parameters provided");

        final String customerId = MiscUtils.getCustomerId(user);

        final Iterable<Order> cart = ofy().load().type(Order.class)
                .ancestor(Key.create(Customer.class, user.getId())) //orders belong to this user
                .filter("deliveryInfo.orderStatus", OrderStatus.NEW) //current cart
                .iterable();

        //check if an orders are overlapping
        for (Order order : cart) {
            if (order.productTag == productTag && durationInDays == order.rentalDuration.getDays()) {
                order.quantity += quantity;
                return ZoString.TRUE;
            }
        }

        final Order order = new Order();
        order.productTag = productTag;
        order.placedWhen = DateTime.now();
        order.rentalDuration = Days.days(durationInDays);



    }

}
