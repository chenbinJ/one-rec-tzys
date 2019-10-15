package com.ztgeo.general.util.chenbin;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyPdfUtil {
    public static void main(String[] args) throws IOException, DocumentException {
        //1、创建文档对象实例
        Document document = new Document();

        //2、建立书写器（Writer）与文档对象（document）关联，通过书写器将文档写入磁盘
        PdfWriter.getInstance(document, new FileOutputStream("d:/test.pdf"));

        //3、打开文档
        document.open();

        //解决中文字体不显示的问题
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

        Font titleFont = new Font(bfChinese);
        titleFont.setSize(18);
        titleFont.setStyle(1);//设置字体样式  加粗
        titleFont.setFamily("宋体");

        //4、向文档中添加内容
        document.add(new Paragraph("PDF demo 我",titleFont));

        //5、关闭文档
        document.close();
    }

    public static Document initDocument(){
        //1、创建文档对象实例
        Document document = new Document();

        //2、打开文档
        document.open();

        //3、返回
        return document;
    }

    public static PdfWriter initPdfWriter(Document document, OutputStream os)throws IOException, DocumentException {
        //建立书写器（Writer）与文档对象（document）关联，通过书写器将文档写入磁盘
        PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
        return pdfWriter;
    }

    public static BaseFont getChineseSupport() throws IOException, DocumentException {
        return BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
    }

    //获取段落样式对象
    public static Font getTextFont(BaseFont languageFont, Float fontSize, Integer fontStyle, String fontFamily){
        Font textFont = new Font(languageFont);
        if(fontSize!=null) {
            textFont.setSize(fontSize);
        }
        if(fontStyle!=null) {
            textFont.setStyle(fontStyle);//设置字体样式  加粗
        }
        if(StringUtils.isNotBlank(fontFamily)) {
            textFont.setFamily(fontFamily);
        }
        return textFont;
    }

    //获取段落对象
    public static Paragraph getTextParagraph(
            Font textFont,                      //文本样式
            String paragraphText,               //文本内容
            Float fixedLeading,                 //固定领先
            Integer alignment,                  //居中情况
            Float spacingBefore,                //段前设置
            Float spacingAfter,                 //段后设置
            Float firstLineIndent,              //首行缩进
            Element... es                          //短语或Chunk
    ){
        Paragraph p = null;
        p = StringUtils.isBlank(paragraphText) ? new Paragraph() : new Paragraph(paragraphText,textFont);
        if(fixedLeading!=null){
            p.setLeading(fixedLeading);
        }
        if(alignment!=null){
            p.setAlignment(alignment);
        }
        if(spacingBefore!=null){
            p.setSpacingBefore(spacingBefore);
        }
        if(spacingAfter!=null){
            p.setSpacingAfter(spacingAfter);
        }
        if(firstLineIndent!=null){
            p.setFirstLineIndent(firstLineIndent);
        }
        if(es!=null && es.length>0){
            for(Element e:es) {
                p.add(e);
            }
        }
        return p;
    }

    //获取短语对象
    public static Phrase getTextPhrase(
            String phraseText,
            Font textFont,
            Chunk... chunks
    ){
        Phrase ph = StringUtils.isBlank(phraseText)?new Phrase():new Phrase(phraseText,textFont);
        if(chunks!=null && chunks.length>0){
            for(Chunk chunk:chunks){
                ph.add(chunk);
            }
        }
        return ph;
    }

    //获取表格
    public static PdfPTable getPdfPTable(int numColumns,float[] totalWidth,String[] columnNames,boolean isLockWidth,Font font) throws DocumentException {
        PdfPTable table = new PdfPTable(numColumns);
        table.setTotalWidth(totalWidth);
        for(String columnName:columnNames){
            PdfPCell cell =  getPdfPCell(columnName,font,Float.valueOf(1),Float.valueOf(1),Float.valueOf(30),true,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
        table.setLockedWidth(isLockWidth); //锁定列宽
        return table;
    }

    //获取表格列
    public static PdfPCell getPdfPCell(
            String cellText,                    //列文字
            Font font,                          //使用的字体设置
            Float borderWidthLeft,              //左边框粗细
            Float borderWidthTop,               //上边框粗细
            Float minimumHeight,                //单元格高度
            boolean useAscender,                //是否可以居中
            int horizontalAlignment,            //水平居中
            int verticalAlignment               //垂直居中
    ){
        PdfPCell cell = new PdfPCell(new Phrase(cellText,font));
        if(borderWidthLeft!=null){
            cell.setBorderWidthLeft(borderWidthLeft);
        }
        if(borderWidthTop!=null){
            cell.setBorderWidthTop(borderWidthTop);
        }
        if(minimumHeight!=null){
            cell.setMinimumHeight(minimumHeight);
        }
        if(useAscender){
            cell.setUseAscender(true); //设置可以居中
            cell.setHorizontalAlignment(horizontalAlignment); //设置水平居中
            cell.setVerticalAlignment(verticalAlignment); //设置垂直居中
        }
        return cell;
    }

    //获取二维码
    public static Image getCodeQrImage(String cxbh) throws BadElementException {
        //生成二维码
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(cxbh, 110, 110, null);
        Image codeQrImage = barcodeQRCode.getImage();
        codeQrImage.scaleAbsolute(110, 110);
        codeQrImage.setAbsolutePosition(467,715);

        return codeQrImage;
    }
}
