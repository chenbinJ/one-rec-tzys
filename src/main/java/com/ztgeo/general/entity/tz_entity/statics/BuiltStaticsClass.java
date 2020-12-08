package com.ztgeo.general.entity.tz_entity.statics;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Bdc_Fw_Info;
import com.ztgeo.general.entity.tz_entity.TZBuilt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuiltStaticsClass {
    public static List<TZBuilt> tzBuilt;

    static{
        tzBuilt = new ArrayList<TZBuilt>();
        TZBuilt built = new TZBuilt();
        List<SJ_Bdc_Fw_Info> bdcInfo = new ArrayList<SJ_Bdc_Fw_Info>();
        String projectName = "开元四季新城(测试)";
        String baseUnitId = "3xxxxx007031GB00013F";
        for(int i=1;i<4;i++) {
            for (int j=1;j<7;j++){
                if(i%2==1){
                    SJ_Bdc_Fw_Info b01 = new  SJ_Bdc_Fw_Info(
                            "ID-IMMOVABLE-000"+i+"-"+j+"01",
                            null,
                            "测试房屋统一标识码"+"000"+i+"-"+j+"01",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "B0295ED4-E35A-4A71-E044-013F",
                            "0013",
                            "000"+i+"-"+j+"01",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "市场化商品房",
                            "（测试地址）测试路xx号，"+projectName+"13幢"+i+"单元"+j+"01室",
                            baseUnitId+"0013"+i+j+"01",
                            null,
                            "0013",
                            ""+i+j+"01",
                            ""+j+"01",
                            ""+i,
                            "6",
                            ""+j,
                            projectName,
                            new BigDecimal(152.0),
                            new BigDecimal(124.0),
                            new BigDecimal(28.0),
                            projectName+"13幢",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    SJ_Bdc_Fw_Info b02 = new  SJ_Bdc_Fw_Info(
                            "ID-IMMOVABLE-000"+i+"-"+j+"02",
                            null,
                            "测试房屋统一标识码"+"000"+i+"-"+j+"02",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "B0295ED4-E35A-4A71-E044-013F",
                            "0013",
                            "000"+i+"-"+j+"02",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "市场化商品房",
                            "（测试地址）测试路xx号，"+projectName+"13幢"+i+"单元"+j+"02室",
                            baseUnitId+"0013"+i+j+"02",
                            null,
                            "0013",
                            ""+i+j+"02",
                            ""+j+"02",
                            ""+i,
                            "6",
                            ""+j,
                            projectName,
                            new BigDecimal(152.0),
                            new BigDecimal(124.0),
                            new BigDecimal(28.0),
                            projectName+"13幢",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    bdcInfo.add(b01);
                    bdcInfo.add(b02);
                }else{
                    SJ_Bdc_Fw_Info b01 = new  SJ_Bdc_Fw_Info(
                            "ID-IMMOVABLE-000"+i+"-"+j+"01",
                            null,
                            "测试房屋统一标识码"+"000"+i+"-"+j+"01",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "B0295ED4-E35A-4A71-E044-013F",
                            "0013",
                            "000"+i+"-"+j+"01",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "市场化商品房",
                            "（测试地址）测试路xx号，"+projectName+"13幢"+i+"单元"+j+"01室",
                            baseUnitId+"0013"+i+j+"01",
                            null,
                            "0013",
                            ""+i+j+"01",
                            ""+j+"01",
                            ""+i,
                            "6",
                            ""+j,
                            projectName,
                            new BigDecimal(114.0),
                            new BigDecimal(91.0),
                            new BigDecimal(23.0),
                            projectName+"13幢",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    SJ_Bdc_Fw_Info b02 = new  SJ_Bdc_Fw_Info(
                            "ID-IMMOVABLE-000"+i+"-"+j+"02",
                            null,
                            "测试房屋统一标识码"+"000"+i+"-"+j+"02",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "B0295ED4-E35A-4A71-E044-013F",
                            "0013",
                            "000"+i+"-"+j+"02",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "市场化商品房",
                            "（测试地址）测试路xx号，"+projectName+"13幢"+i+"单元"+j+"02室",
                            baseUnitId+"0013"+i+j+"02",
                            null,
                            "0013",
                            ""+i+j+"02",
                            ""+j+"02",
                            ""+i,
                            "6",
                            ""+j,
                            projectName,
                            new BigDecimal(88.0),
                            new BigDecimal(72.0),
                            new BigDecimal(16.0),
                            projectName+"13幢",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    SJ_Bdc_Fw_Info b03 = new  SJ_Bdc_Fw_Info(
                            "ID-IMMOVABLE-000"+i+"-"+j+"03",
                            null,
                            "测试房屋统一标识码"+"000"+i+"-"+j+"03",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "B0295ED4-E35A-4A71-E044-013F",
                            "0013",
                            "000"+i+"-"+j+"03",
                            "B0295ED4-E35A-4A71-E044-013F"+ UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                            "市场化商品房",
                            "（测试地址）测试路xx号，"+projectName+"13幢"+i+"单元"+j+"03室",
                            baseUnitId+"0013"+i+j+"03",
                            null,
                            "0013",
                            ""+i+j+"03",
                            ""+j+"03",
                            ""+i,
                            "6",
                            ""+j,
                            projectName,
                            new BigDecimal(114.0),
                            new BigDecimal(91.0),
                            new BigDecimal(23.0),
                            projectName+"13幢",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    bdcInfo.add(b01);
                    bdcInfo.add(b02);
                    bdcInfo.add(b03);
                }
            }
        }
        built.setBdcInfo(bdcInfo);
        built.setZh("13");
        built.setLpmc(projectName+"13幢");
        built.setXmmc(projectName);
        built.setLpzl("（测试地址）测试路xx号13幢");
        built.setKfqy("xxx房地产开发有限公司");
        tzBuilt.add(built);
    }
}
