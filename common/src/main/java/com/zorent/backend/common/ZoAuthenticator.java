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

    @Override
    public User authenticate(HttpServletRequest request) {

        final String token = request.getHeader("Authorization");
        if (TextUtils.isEmpty(token))
            throw new IllegalAccessError("Authorization token not found");

        final String[] splitter = token.split(" ");
        if (splitter.length != 2 || TextUtils.isEmpty(splitter[0]) || TextUtils.isEmpty(splitter[1]))
            throw new IllegalAccessError("Invalid Authorization token");

        final String userId;

        try {

            final URL url = new URL("https://1-dot-oauth-module-dot-user-thinks.appspot.com/OAuthServlet?accessToken=" + splitter[0] + "&provider=" + splitter[1]);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            userId = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (TextUtils.isEmpty(userId))
            throw new IllegalAccessError("Could not authorize");

        logger.info("Found user " + userId);
        //userId is known here
        return new User(userId, "");
    }
}