package com.ztgeo.general.entity.extend;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import tk.mybatis.mapper.MapperException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ServerEncoder implements Encoder.Text<NumberDisplay> {
    @Override
    public String encode(NumberDisplay numberDisplay) throws EncodeException {
        try {
            JSONObject jsonObject= JSONObject.fromObject(numberDisplay);
            return jsonObject.toString();
        } catch (MapperException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
