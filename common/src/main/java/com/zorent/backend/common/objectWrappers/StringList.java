package com.zorent.backend.common.objectWrappers;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dexter on 03/11/15.
 */
public class StringList implements Serializable {

    private static final long serialVersionUID = 354678354678345678L;

    public List<String> stringList;

    public StringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public StringList() {
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringList)) return false;

        StringList that = (StringList) o;

        return stringList != null ? stringList.equals(that.stringList) : that.stringList == null;

    }

    @Override
    public int hashCode() {
        return stringList != null ? stringList.hashCode() : 0;
    }
}
