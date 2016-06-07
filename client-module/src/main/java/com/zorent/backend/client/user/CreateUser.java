package com.zorent.backend.client.user;

import com.google.appengine.api.datastore.Email;

/**
 * Created by dexter on 06/06/2016.
 */
public class CreateUser {

    Email email;
    String fullName;

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
