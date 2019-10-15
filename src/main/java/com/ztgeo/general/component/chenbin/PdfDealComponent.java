package com.ztgeo.general.component.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.FolderDealUtil;
import com.ztgeo.general.util.chenbin.MyPdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PdfDealComponent {
    @Value("${receipt.folderPath}")
    private String basePath;

    public String initCommonQueryResultPdf(Sj_Inquiry_Record record) throws IOException, DocumentException {

        String pdfPath = "";
        String returnData = record.getReturnData();
        JSONObject serviceData = JSONObject.parseObject(returnData);
        JSONArray serviceDataInfos = serviceData.getJSONArray("serviceDataInfos");
        List<JSONObject> QLS = null;
        List<JSONObject> DYS = null;
        List<JSONObject> CFS = null;
        List<JSONObject> YYS = null;
        //遍历该JSONArray
        if(serviceDataInfos.size()>0) {
            QLS = new ArrayList<JSONObject>();
            DYS = new ArrayList<JSONObject>();
            CFS = new ArrayList<JSONObject>();
            YYS = new ArrayList<JSONObject>();
            for (int i = 0; i < serviceDataInfos.size(); i++) {
                JSONObject serviceDataInfo = serviceDataInfos.getJSONObject(i);
                String informationType = serviceDataInfo.getString("informationType");
                if(informationType.equals(BizOrBizExceptionConstant.IMMOVABLE_RIGHT_INQUIRY_CODE)){
                    QLS.add(serviceDataInfo);
                }else if(informationType.equals(BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_INQUIRY_CODE)){
                    DYS.add(serviceDataInfo);
                }else if(informationType.equals(BizOrBizExceptionConstant.IMMOVABLE_SEQUESTRATION_INQUIRY_CODE)){
                    CFS.add(serviceDataInfo);
                }else if(informationType.equals(BizOrBizExceptionConstant.IMMOVABLE_OBJECTION_INQUIRY_CODE)){
                    YYS.add(serviceDataInfo);
                }else{
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_RIGHT_OUT);
                }
            }

        }
        //获取PdfWriter对象
        String pdfFolderPath = FolderDealUtil.getPDFFileFolder(basePath);
        if (pdfFolderPath.lastIndexOf("/") < (pdfFolderPath.length() - 1)) {
            pdfFolderPath = pdfFolderPath + "/";
        }
        if(StringUtils.isNotBlank(pdfFolderPath)){
            File folder = new File(pdfFolderPath);
            if(!folder.exists() || !folder.isDirectory()){
                folder.mkdirs();
            }
        }

        //1、创建文档对象实例
        Document document = new Document();
        //PDF路径
        pdfPath = pdfFolderPath + record.getRecordId() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
        //2、打开文档
        document.open();
        //解决中文字体不显示的问题
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);


        record.setPdfPath(pdfPath);
            /*
             *   获取PDF需要使用的基本样式对象
             *   1.标题
             *   2.2级标题
             *   3.正文
             *   4.加重描述性文字
             */
            //标题样式字体
            Font titleFont = MyPdfUtil.getTextFont(bfChinese, Float.valueOf(18), 1, "宋体");
            //正文字体
            Font zwFont = MyPdfUtil.getTextFont(bfChinese, Float.valueOf(12), null, "宋体");
            //留空的字体 比如 申请人 查询时间等等
            Font paramFont = MyPdfUtil.getTextFont(bfChinese, Float.valueOf(13), 1 | 4, "宋体");
            //二级标题 可用于免责声明
            Font secondTitleFont = MyPdfUtil.getTextFont(bfChinese, Float.valueOf(14), Font.BOLD, null);

            //排版通用产权查询的PDF样式
            document.add(MyPdfUtil.getTextParagraph(
                    titleFont,
                    "不动产利害关系人产权情况查询",
                    Float.valueOf(50),
                    Element.ALIGN_CENTER,
                    null, null, null, null
            )); //添加标题

            Chunk chunk0_1 = new Chunk("编号:                ", zwFont);
            Chunk chunk0_2 = new Chunk(record.getRecordId(), zwFont);
            Chunk[] chunk0 = new Chunk[]{chunk0_1,chunk0_2};
            Phrase ph0_1 = MyPdfUtil.getTextPhrase(null, null, chunk0);
            Phrase[] ph0 = new Phrase[]{ph0_1};
            document.add(MyPdfUtil.getTextParagraph(
                    null, null, null,
                    Element.ALIGN_RIGHT,
                    Float.valueOf(30),
                    Float.valueOf(0),
                    null,
                    ph0
            ));//添加查询编号正文内容

            document.add(MyPdfUtil.getCodeQrImage(record.getRecordId()));//添加查询编号对应的二维码信息

            Chunk chunk1_1 = new Chunk("依据申请人", zwFont);
            Chunk chunk1_2 = new Chunk(record.getApplicant(), paramFont);
            Chunk chunk1_3 = new Chunk("提出的有关自身住房权利状况信息查询申请,经“一窗受理”平台使用“利害关系人查询服务”进行查询，查询执行时间", zwFont);
            Chunk chunk1_4 = new Chunk(record.getExecutionTime(), paramFont);
            Chunk chunk1_5 = new Chunk("止,查询其不动产权利状况信息如下:", zwFont);
            Chunk[] chunk1 = new Chunk[]{chunk1_1,chunk1_2,chunk1_3,chunk1_4,chunk1_5};
            document.add(MyPdfUtil.getTextParagraph(
                    null, null, null,
                    null,
                    Float.valueOf(30),
                    Float.valueOf(0),
                    Float.valueOf(20),
                    chunk1
            ));//段落 1

            Chunk chunk2_1 = new Chunk("姓名:  ", zwFont);
            Chunk chunk2_2 = new Chunk(record.getApplicant(), zwFont);
            Chunk chunk2_3 = new Chunk("    身份证号:  ", zwFont);
            Chunk chunk2_4 = new Chunk(record.getIDNumber(), zwFont);
            document.add(MyPdfUtil.getTextParagraph(
                    null, null, Float.valueOf(50),
                    null,
                    null,
                    null,
                    Float.valueOf(20),
                    chunk2_1,
                    chunk2_2,
                    chunk2_3,
                    chunk2_4
            ));//段落 2

            Chunk chunk3_1 = new Chunk("经查询，现在登记在申请人名下的不动产权利状况如下: ", zwFont);
            Phrase ph3_1 = MyPdfUtil.getTextPhrase(null, null, chunk3_1);
            document.add(getPjswz(ph3_1, zwFont));//段落 3

            PdfPTable table3_1 = MyPdfUtil.getPdfPTable(
                    7,
                    new float[]{30, 130, 130, 80, 50, 50, 50},
                    new String[]{"序号", "不动产证号", "坐落", "权利人", "抵押状况", "查封状况", "异议状况"},
                    true,
                    zwFont
            );//生成表格

            if (QLS == null || QLS.size() <= 0) {
                for (int i = 0; i < 7; i++) {
                    PdfPCell cell = MyPdfUtil.getPdfPCell(
                            "无",
                            zwFont,
                            Float.valueOf(1),
                            Float.valueOf(1),
                            Float.valueOf(30),
                            true,
                            Element.ALIGN_CENTER,
                            Element.ALIGN_MIDDLE
                    );
                    table3_1.addCell(cell);
                }
            } else {
                for (int j = 0; j < QLS.size(); j++) {
                    JSONObject QL = QLS.get(j);
                    //序号
                    table3_1.addCell(getCommonCell((j + 1) + "", zwFont));
                    //不动产证号
                    table3_1.addCell(getCommonCell(QL.getString("immovableCertificateNo"), zwFont));
                    //坐落
                    table3_1.addCell(getCommonCell(QL.getString("immovableSite"), zwFont));
                    //权利人
                    JSONArray qlrs = QL.getJSONArray("obligeeVoList");
                    table3_1.addCell(getCommonCell(getCommonQlrStr(qlrs), zwFont));
                    //抵押状况
                    table3_1.addCell(getCommonCell(QL.getBooleanValue("isMortgage") ? "是" : "否", zwFont));
                    //查封状况
                    table3_1.addCell(getCommonCell(QL.getBooleanValue("isSequestration") ? "是" : "否", zwFont));
                    //异议状况
                    table3_1.addCell(getCommonCell(QL.getBooleanValue("isObjection") ? "是" : "否", zwFont));
                }
            }//填充表格
            document.add(table3_1);

            if (DYS != null && DYS.size() > 0) {
                Chunk chunk3_2 = new Chunk("产权抵押详情信息列表: ", zwFont);
                Phrase ph3_2 = MyPdfUtil.getTextPhrase(null, null, chunk3_2);
                document.add(getPjswz(ph3_2, zwFont));//段落 3
                PdfPTable table3_2 = MyPdfUtil.getPdfPTable(
                        5,
                        new float[]{30, 135, 135, 135, 85},
                        new String[]{"序号", "抵押证明号", "相关证号", "坐落", "抵押权人"},
                        true,
                        zwFont
                );//生成表格
                for (int k = 0; k < QLS.size(); k++) {
                    JSONObject DY = DYS.get(k);
                    //序号
                    table3_2.addCell(getCommonCell((k + 1) + "", zwFont));
                    //抵押证明号
                    table3_2.addCell(getCommonCell(DY.getString("mortgageCertificateNo"), zwFont));
                    //相关证号
                    table3_2.addCell(getCommonCell(DY.getString("relevantCertificateNo"), zwFont));
                    //坐落
                    table3_2.addCell(getCommonCell(DY.getString("immovableSite"), zwFont));
                    //抵押权人
                    JSONArray dyqrs = DY.getJSONArray("mortgageHolderVoList");
                    table3_2.addCell(getCommonCell(getCommonQlrStr(dyqrs), zwFont));
                }
                document.add(table3_2);
            }

            //4.6免责声明
            document.add(MyPdfUtil.getTextParagraph(
                    secondTitleFont,
                    "免责声明:",
                    null, null,
                    Float.valueOf(3),
                    Float.valueOf(3),
                    Float.valueOf(20),
                    null
            ));

            //免责声明第一条
            Chunk chunk5_1 = new Chunk("1、申请人请核实以上身份信息和结果信息，如信息有误请及时联系xxx不动产登记中心，联系电话:xxxx-8xxxxxxx", zwFont);
            Phrase ph5_1 = MyPdfUtil.getTextPhrase(null, zwFont, chunk5_1);
            document.add(getPmzsm(ph5_1, zwFont));

            //免责声明第二条
            Chunk chunk5_2 = new Chunk("2、若不动产权属登记信息证明与不动产登记簿记载不一致的，以不动产登记簿为准。", zwFont);
            Phrase ph5_2 = MyPdfUtil.getTextPhrase(null, zwFont, chunk5_2);
            document.add(getPmzsm(ph5_2, zwFont));

            //免责声明第三条
            Chunk chunk5_3 = new Chunk("3、此次查询仅限于xx市区范围内不动产登记信息，不包括下属xx县、xx县、...管辖范围。", zwFont);
            Phrase ph5_3 = MyPdfUtil.getTextPhrase(null, zwFont, chunk5_3);
            document.add(getPmzsm(ph5_3, zwFont));

            //免责声明第四条
            Chunk chunk5_4 = new Chunk("4、以上内容仅供参考。", zwFont);
            Phrase ph5_4 = MyPdfUtil.getTextPhrase(null, zwFont, chunk5_4);
            document.add(getPmzsm(ph5_4, zwFont));

            //6查询单位和时间
            Chunk chunk6_2 = new Chunk("查询时间: ", zwFont);
            Chunk chunk6_3 = new Chunk(record.getExecutionTime(), zwFont);
            document.add(MyPdfUtil.getTextParagraph(
                    zwFont,
                    null,
                    null,
                    Element.ALIGN_RIGHT,
                    Float.valueOf(80),
                    Float.valueOf(10),
                    null,
                    chunk6_2, chunk6_3
            ));
            // 设置文档大小 （A4）
            Rectangle rectangle = new Rectangle(PageSize.A4.rotate());
            document.setPageSize(rectangle);
            //设置页面留白
            document.setMargins(10, 10, 20, 20);

            document.close();
        return pdfPath;
    }

    //免责声明
    private Paragraph getPmzsm(Phrase ph,Font font){
        return MyPdfUtil.getTextParagraph(
                font,
                null,
                null,null,
                Float.valueOf(0),
                Float.valueOf(0),
                Float.valueOf(20),
                ph
        );
    }

    //介绍文字
    private Paragraph getPjswz(Phrase ph,Font font){
        return MyPdfUtil.getTextParagraph(
                font,null,null,
                null,
                Float.valueOf(30),
                Float.valueOf(15),
                Float.valueOf(20),
                ph
        );
    }

    //表的列
    private PdfPCell getCommonCell(String cellText,Font font){
        return MyPdfUtil.getPdfPCell(
                cellText,
                font,
                Float.valueOf(1),
                Float.valueOf(1),
                Float.valueOf(30),
                true,
                Element.ALIGN_CENTER,
                Element.ALIGN_MIDDLE
        );
    }

    //权利人Str
    private String getCommonQlrStr(JSONArray qlrs){
        String qlr_str = "";
        if(qlrs!=null && qlrs.size()>0){
            for(int index=0;index<qlrs.size();index++){
                JSONObject qlr = qlrs.getJSONObject(index);
                if(index==qlrs.size()-1){
                    qlr_str = qlr_str+qlr.getString("obligeeName");
                }else{
                    qlr_str = qlr_str+qlr.getString("obligeeName")+",";
                }
            }
        }
        return qlr_str;
    }

    public boolean removeFile(String pdfPath){
        System.out.println("进入删除");
        boolean isSuccess = false;
        File file = new File(pdfPath);
        if(file.exists() && file.isFile()){
            isSuccess = file.delete();
        }
        return isSuccess;
    }
}
