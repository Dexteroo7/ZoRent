package com.zorent.backend.common.entities;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Link;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by dexter on 02/06/2016.
 */

@Entity
@Cache
@Index
public class Customer implements FirebaseUser {

    public enum LoadWithUsedCoupons {}
    public enum LoadWithOrderHistory {}

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    @Id
    public String id;

    public Email email;

    public String provider;

    public String fullName;

    public Link profilePic;

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Email getEmail() {
        return null;
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String getFullName() {
        return null;
    }

    @Override
    public Link getProfilePic() {
        return null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setProfilePic(Link profilePic) {
        this.profilePic = profilePic;
    }
}
