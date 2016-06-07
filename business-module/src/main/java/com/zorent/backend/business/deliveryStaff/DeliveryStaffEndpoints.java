package com.zorent.backend.business.deliveryStaff;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.zorent.backend.common.Constants;
import com.zorent.backend.common.MiscUtils;
import com.zorent.backend.common.entities.DeliveryStaff;
import com.zorent.backend.common.objectWrappers.ZoString;

import java.util.logging.Logger;

import static com.zorent.backend.common.OfyService.ofy;

/**
 * Created by dexter on 07/06/2016.
 */

@Api(
        name = "deliveryStaff",
        version = "v1",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {
                Constants.WEB_CLIENT_ID,
                Constants.ANDROID_CLIENT_ID,
                Constants.ASHISH_DEBUG_KEY,
                Constants.AYUSH_DEBUG_KEY},
        audiences = {Constants.ANDROID_AUDIENCE},
        namespace = @ApiNamespace(
                ownerDomain = "deliveryStaff.business.backend.zorent.com",
                ownerName = "deliveryStaff.business.backend.zorent.com",
                packagePath = ""
        )
)
public class DeliveryStaffEndpoints {

    private static final Logger LOGGER = Logger.getLogger(DeliveryStaffEndpoints.class.getName());

    @SuppressWarnings("ResourceParameter")
    @ApiMethod(
            name = "registerStaff",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ZoString createUser(@Nullable User user,
                               CreateUser userData) throws UnauthorizedException, ConflictException {

        MiscUtils.checkUser(user);

        final boolean alreadyPresent = ofy().load().type(DeliveryStaff.class)
                .filter("contactEmail", user.getEmail())
                .limit(1).count() > 0;

        if (alreadyPresent)
            throw new ConflictException("This email is already registered");

        final DeliveryStaff staff = new DeliveryStaff.Builder()
                .setTag(userData.tag)
                .setContactNumber(userData.contactNumber)
                .setName(userData.fullName)
                .setContactEmail(user.getEmail()).build();

        LOGGER.info("Registering staff " + staff.fullName + " " + staff.contactEmail);

        return ZoString.of(ofy().save().entity(staff).now().getString());
    }
}