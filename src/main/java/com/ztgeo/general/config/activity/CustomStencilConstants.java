package com.ztgeo.general.config.activity;

import jdk.nashorn.internal.ir.ObjectNode;
import org.activiti.bpmn.model.BaseElement;

public interface CustomStencilConstants {

    void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement);



}
