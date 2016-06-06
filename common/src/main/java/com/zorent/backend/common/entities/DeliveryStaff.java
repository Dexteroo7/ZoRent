package com.zorent.backend.common.entities;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.PhoneNumber;
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
public class DeliveryStaff {

    @Id
    public Long id;

    public StaffTag tag;

    public PhoneNumber contactNumber;

    public Email contactEmail;

    public String name;

    public Email googleRegistration;

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StaffTag getTag() {
        return tag;
    }

    public void setTag(StaffTag tag) {
        this.tag = tag;
    }

    public PhoneNumber getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(PhoneNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Email getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(Email contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Email getGoogleRegistration() {
        return googleRegistration;
    }

    public void setGoogleRegistration(Email googleRegistration) {
        this.googleRegistration = googleRegistration;
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public enum StaffTag {

        QUALITY_MANAGER,
        DELIVERY_BOY,
        TECHNICIAN
    }
}