package com.zorent.backend.common.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import java.util.Collections;
import java.util.Set;

/**
 * Created by dexter on 02/06/2016.
 */

@Entity
@Cache
@Index
public class Customer implements FirebaseUser {

    public enum LoadWithUsedCoupons {}
    public enum LoadWithOrderHistory {}

    @Id
    public String id;
}
