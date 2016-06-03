package com.zorent.backend.common.entities;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Link;

/**
 * Created by dexter on 30/04/16.
 */
interface FirebaseUser {

    /**
     * Firebase will allot a unique identifier to every generated user
     *
     * @return the unique firebase identifier
     */
    String getId();

    /**
     * A user will be logged in using an account
     *
     * @return the account email
     */
    Email getEmail();

    /**
     * The email will be associated with a provider
     *
     * @return the provider for the current account / email
     */
    String getProvider();

    /**
     * Customer must have a name
     * @return the full name
     */
    String getFullName();

    /**
     * Customer should have a profile pic
     * @return the link to profile pic
     */
    Link getProfilePic();
}