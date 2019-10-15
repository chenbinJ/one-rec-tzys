package com.ztgeo.general.component.pubComponent;

import com.ztgeo.general.mapper.chenbin.ReceiptNumberMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReceiptNumberComponent {
    @Autowired
    private ReceiptNumberMapper receiptNumberMapper;
    @Value("${receipt.sequentName}")
    private String sequenceName;

    public String getNextReceiptNumber(){
        System.out.println(sequenceName);
        int number = receiptNumberMapper.getNextNumber(sequenceName);
        String receiptNumber = IDUtil.getReceiptNumber(number);
        return receiptNumber;
    }
    public void InitCurrentNumber(){
        receiptNumberMapper.InitCurrentNumber(sequenceName);
    }
}
