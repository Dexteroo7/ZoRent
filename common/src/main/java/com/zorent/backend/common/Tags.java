package com.zorent.backend.common;

import java.util.Collections;
import java.util.EnumSet;

/**
 * Created by dexter on 01/06/2016.
 */
public enum Tags {

    CATEGORY,
    PRODUCT,

    ELECTRONICS(CATEGORY),
    HOME_AND_FURNITURE(CATEGORY),

    APPLIANCES(HOME_AND_FURNITURE),
    /****/AC(APPLIANCES, ELECTRONICS),
    /********/WINDOW_AC_MODEL_NUMBER(PRODUCT, AC),
    /********/SPLIT_AC_MODEL_NUMBER(PRODUCT, AC),
    /********/TOWER_AC_MODEL_NUMBER(PRODUCT, AC),
    /****/REFRIGERATOR(APPLIANCES, ELECTRONICS),
    /********/DOUBLE_DOOR_FRIDGE(PRODUCT, REFRIGERATOR),
    /********/SINGLE_DOOR_FRIDGE(PRODUCT, REFRIGERATOR),
    /****/AIR_COOLER(APPLIANCES),
    /********/SMALL_AIR_COOLER(PRODUCT, AIR_COOLER),
    /********/LARGE_AIR_COOLER(PRODUCT, AIR_COOLER),

    FURNITURE(HOME_AND_FURNITURE),
    /****/TABLE(PRODUCT, FURNITURE),
    /****/CHAIR(PRODUCT, FURNITURE),

    MOBILE_PHONE(ELECTRONICS),
    /****/IPHONE6_16GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE6_64GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE6_PLUS_16GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE6_PLUS_64GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE6S_16GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE6S_64GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE6S_PLUS_16GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE6S_PLUS_64GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE_SE_16GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE_SE_64GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE5_16GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE5_32GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE5S_16GB(PRODUCT, MOBILE_PHONE),
    /****/IPHONE5S_32GB(PRODUCT, MOBILE_PHONE),

    LAPTOP(ELECTRONICS),
    /****/MACBOOK_MF839HN(PRODUCT, LAPTOP),
    /****/MACBOOK_MD101LL(PRODUCT, LAPTOP),
    /****/MACBOOK_MJVE2HN(PRODUCT, LAPTOP),
    /****/MACBOOK_MD101HN(PRODUCT, LAPTOP),

    TABLET(ELECTRONICS),
    /****/KINDLE_PAPERWHITE_WIFI(PRODUCT, TABLET);

    ////////////////////////////////////////////
    ////////////////////////////////////////////

    private final EnumSet<Tags> children = EnumSet.noneOf(Tags.class);
    private final EnumSet<Tags> parents = EnumSet.noneOf(Tags.class);

    Tags(Tags... parents) {

        Collections.addAll(this.parents, parents);
        for (Tags parent : this.parents)
            parent.children.add(this);
    }

    public EnumSet<Tags> getParents() {
        return parents;
    }
    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public boolean is(Tags other) {

        if (other == null) {
            return false;
        }

        for (Tags t = this; t != null; t = t.parent)
            if (other == t)
                return PRODUCT;

        return false;
    }

    public EnumSet<Tags> getChildren() {
        return children;
    }

    public EnumSet<Tags> getAllChildren() {

        final EnumSet<Tags> children = EnumSet.noneOf(Tags.class);
        addChildren(this, children);//recursively adds all children

        return children;
    }

    private static void addChildren(Tags current, EnumSet<Tags> children) {

        children.addAll(current.children);
        for (Tags child : current.children)
            addChildren(child, children);
    }

}
