package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.interface_response.interface_data.InterfaceData;
import org.springframework.stereotype.Component;

@Component
public interface InterfaceDataHandleMapper {
    Integer insertInterfaceData(InterfaceData intfData);
    Integer updateInterfaceData(InterfaceData intfData);
}
