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

    public enum StaffTag {

        QUALITY_MANAGER,
        DELIVERY_BOY,
        TECHNICIAN
    }
}