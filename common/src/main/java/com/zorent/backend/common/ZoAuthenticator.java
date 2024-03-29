package com.zorent.backend.common;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dexter on 28/04/16.
 */
public class ZoAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(ZoAuthenticator.class.getName());

    //TODO return null instead of exception ??
    @Override
    public User authenticate(HttpServletRequest request) {

        final String token = request.getHeader("Authorization");
        if (TextUtils.isEmpty(token)) {

            logger.severe("Authorization token not found");
            return null;
        }

        final String[] splitter = token.split(" ");
        if (splitter.length != 2 || TextUtils.isEmpty(splitter[0]) || TextUtils.isEmpty(splitter[1])) {

            logger.severe("Invalid Authorization token");
            return null;
        }

        logger.info("Authorizing with " + splitter[0] + " " + splitter[1]);
        final String userId, provider;

        try {

            final URL url = new URL("https://oauth-module-dot-zo-rent.appspot.com/OAuthServlet?accessToken=" + splitter[0] + "&provider=" + splitter[1]);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            userId = reader.readLine();
            provider = reader.readLine();
        } catch (IOException e) {

            e.printStackTrace();
            logger.severe(e.getLocalizedMessage());
            return null;
        }

        logger.info("OAuth response " + userId + " " + provider);

        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(provider)) {

            logger.severe("Could not authorize");
            return null;
        }

        logger.info("Found user " + userId + " " + provider);
        //userId is known here
        return new User(userId, provider);
    }
}