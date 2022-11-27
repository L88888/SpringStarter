package com.sit.client.tests.doc;

import com.aspose.words.*;
import com.aspose.words.Shape;
import lombok.extern.slf4j.Slf4j;
import sun.awt.FontConfiguration;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @program: spring-starter
 * @description: PDF转换工具类
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-12-08 13:54:
 **/
@Slf4j
public class Word2PdfUtilt {

    public static void main(String[] args) {
        log.info("开始将DOC文件转换为PDF文件");
        doc2pdf("F:\\150\\yysz\\协查报告模板.docx", "F:\\150\\yysz\\2021-12-16xcbgsy"+new Random().nextInt()+".pdf");
        log.info("DOC文件转换为PDF文件结束");
    }

    public static void doc2pdf(String inPath, String outPath) {
        log.info("待转化的文件路径:>{},待输出的问文件路径:>{}", inPath, outPath);
        FileOutputStream os = null;
        FontConfiguration fontConfiguration;
        try {
            File file = new File(outPath); // 新建一个空白pdf文档
            os = new FileOutputStream(file);
            Document doc = new Document(inPath); // Address是将要被转化的word文档
            insertWatermarkText(doc, "张三  湖州市公安分局禁毒大队\n" +
                    "          IP地址：172.20.1.21  MAC地址：5E-36-54-7F-9C-BE");
            doc.save(os, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Title: insertWatermarkText
     * @Description: PDF生成水印
     * @author mzl
     * @param doc
     * @param watermarkText
     * @throws Exception
     * @throws
     */
    private static void insertWatermarkText(Document doc, String watermarkText) throws Exception
    {
        // 头部
        Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);
        //水印内容
        watermark.getTextPath().setText(watermarkText);
        //水印字体
        watermark.getTextPath().setFontFamily("宋体");
        //水印宽度
        watermark.setWidth(550);
        //水印高度
        watermark.setHeight(100);
        //旋转水印
        watermark.setRotation(-20);
        //水印颜色
        watermark.getFill().setColor(Color.lightGray);
        watermark.setStrokeColor(Color.lightGray);
        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.MARGIN);
        watermark.setWrapType(WrapType.NONE);
        watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // 中部
        Shape watermark2 = getShape(doc, watermarkText, VerticalAlignment.CENTER);
        // 底部
        Shape watermark3 = getShape(doc, watermarkText, VerticalAlignment.BOTTOM);

        Paragraph watermarkPara = new Paragraph(doc);
        watermarkPara.appendChild(watermark);
        watermarkPara.appendChild(watermark2);
        watermarkPara.appendChild(watermark3);

        for (Section sect : doc.getSections())
        {
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
        }
        System.out.println("Watermark Set");
    }

    /**
     * 定义水印形状
     * @param doc 文档对象
     * @param watermarkText 水印文字
     * @param bottom 水印位置
     * @return
     */
    private static Shape getShape(Document doc, String watermarkText, int bottom) {
        Shape watermark3 = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);
        //水印内容
        watermark3.getTextPath().setText(watermarkText);
        //水印字体
        watermark3.getTextPath().setFontFamily("宋体");
        //水印宽度
        watermark3.setWidth(550);
        //水印高度
        watermark3.setHeight(100);
        //旋转水印
        watermark3.setRotation(-20);
        //水印颜色
        watermark3.getFill().setColor(Color.lightGray);
        watermark3.setStrokeColor(Color.lightGray);
        watermark3.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
        watermark3.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
        watermark3.setWrapType(WrapType.NONE);
        watermark3.setVerticalAlignment(bottom);
        watermark3.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return watermark3;
    }

    /**
     * 给文档添加水印
     * @param watermarkPara 水印对象
     * @param sect 部分
     * @param headerType 头部类型
     * @throws Exception
     */
    private static void insertWatermarkIntoHeader(Paragraph watermarkPara,
                                                  Section sect,
                                                  int headerType) throws Exception
    {
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);

        if (header == null)
        {
            header = new HeaderFooter(sect.getDocument(), headerType);
            sect.getHeadersFooters().add(header);
        }

        header.appendChild(watermarkPara.deepClone(true));
    }
}
