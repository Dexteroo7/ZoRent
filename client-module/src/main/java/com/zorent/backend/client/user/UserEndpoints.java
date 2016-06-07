package com.zorent.backend.client.user;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.UnauthorizedException;
import com.zorent.backend.common.MiscUtils;
import com.zorent.backend.common.ZoAuthenticator;
import com.zorent.backend.common.entities.Customer;
import com.zorent.backend.common.objectWrappers.ZoString;

import java.util.logging.Logger;

import static com.zorent.backend.common.OfyService.ofy;

/**
 * Created by dexter on 06/06/2016.
 */

@Api(
        name = "user",
        version = "v1",
        authenticators = {ZoAuthenticator.class},
        namespace = @ApiNamespace(
                ownerDomain = "user.client.backend.zorent.com",
                ownerName = "user.client.backend.zorent.com",
                packagePath = ""
        )
)
public class UserEndpoints {

    private static final Logger LOGGER = Logger.getLogger(UserEndpoints.class.getName());

    @SuppressWarnings("ResourceParameter")
    @ApiMethod(
            name = "createUser",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ZoString createUser(@Nullable User user,
                               CreateUser createUser) throws UnauthorizedException {

        final String customerId = MiscUtils.getCustomerId(user);
        final String provider = MiscUtils.getProvider(user);

        final Customer customer = new Customer();
        customer.setEmail(createUser.email);
        customer.setFullName(createUser.fullName);
        customer.setId(customerId);
        customer.setProvider(provider);

        LOGGER.info("Registering user " + createUser.fullName + " " + createUser.email);

        return ZoString.of(ofy().save().entity(createUser).now().getString());
    }
}