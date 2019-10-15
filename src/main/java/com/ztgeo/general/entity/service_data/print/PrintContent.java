package com.ztgeo.general.entity.service_data.print;

import java.io.Serializable;
import java.util.List;

public class PrintContent<T extends PrintEntity> implements Serializable {
    private T single;
    private List<T> array;

    public T getSingle() {
        return single;
    }

    public void setSingle(T single) {
        this.single = single;
    }

    public List<T> getArray() {
        return array;
    }

    public void setArray(List<T> array) {
        this.array = array;
    }
}
