package com.ztgeo.general.entity.extend;


public enum StateEnum{


    NORMAL(1),
    FORBID(2);

    public final int value;


    StateEnum(int value) {
        this.value = value;
    }
    public  int getValue(){
        return value;
    }


}
