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

    public String fullName;

    private DeliveryStaff() {
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private StaffTag getTag() {
        return tag;
    }

    private void setTag(StaffTag tag) {
        this.tag = tag;
    }

    private PhoneNumber getContactNumber() {
        return contactNumber;
    }

    private void setContactNumber(PhoneNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    private Email getContactEmail() {
        return contactEmail;
    }

    private void setContactEmail(Email contactEmail) {
        this.contactEmail = contactEmail;
    }

    private String getFullName() {
        return fullName;
    }

    private void setFullName(String name) {
        this.fullName = name;
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public static final class Builder {

        private final DeliveryStaff deliveryStaff = new DeliveryStaff();

        public Builder setTag(StaffTag tag) {
            deliveryStaff.setTag(tag);
            return this;
        }

        public Builder setContactNumber(PhoneNumber contactNumber) {
            deliveryStaff.setContactNumber(contactNumber);
            return this;
        }

        public Builder setContactEmail(String contactEmail) {
            deliveryStaff.setContactEmail(new Email(contactEmail));
            return this;
        }

        public Builder setName(String fullName) {
            deliveryStaff.setFullName(fullName);
            return this;
        }

        public DeliveryStaff build() {
            return deliveryStaff;
        }
    }

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public enum StaffTag {

        QUALITY_MANAGER,
        DELIVERY_BOY,
        TECHNICIAN
    }
}