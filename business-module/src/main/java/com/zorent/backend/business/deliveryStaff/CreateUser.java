package com.zorent.backend.business.deliveryStaff;

import com.google.appengine.api.datastore.PhoneNumber;
import com.zorent.backend.common.entities.DeliveryStaff;

/**
 * Created by dexter on 06/06/2016.
 */
public class CreateUser {

    public DeliveryStaff.StaffTag tag;
    public PhoneNumber contactNumber;
    public String fullName;

    public DeliveryStaff.StaffTag getTag() {
        return tag;
    }

    public void setTag(DeliveryStaff.StaffTag tag) {
        this.tag = tag;
    }

    public PhoneNumber getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(PhoneNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
