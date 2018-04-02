package com;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sun.swing.SwingUtilities2;

import javax.xml.bind.DatatypeConverter;

public class LgoInfo {

    public static String SoftName, CopyrightName;

    static {
        intiDefault();
    }


    private static void intiDefault() {
        SoftName = "220KV GIL在线检测系统";
        CopyrightName = "江苏南瑞恒驰电气装备有限公司";
    }


    /**
     * 获取字体
     */
    public static Font getTitleFont(String fontname, int style, String title, int width, int height) {
        int size, w = 0;
        Font font = null;
        if (height > 0) {
            size = height;
            while (size >= 12) {
                font = new Font(fontname, style, size);
                GlyphVector v = font.createGlyphVector(SwingUtilities2
                                .getFontMetrics(null, font).getFontRenderContext(),
                        title);
                Shape shape = v.getOutline();
                Rectangle bounds = shape.getBounds();
                w = bounds.x + bounds.width + 2;
                if (w > width) {
                    size--;
                } else {
                    break;
                }
            }
        } else {
            size = 12;
            while (width > w) {
                font = new Font(fontname, style, size);
                GlyphVector v = font.createGlyphVector(SwingUtilities2
                                .getFontMetrics(null, font).getFontRenderContext(),
                        title);
                Shape shape = v.getOutline();
                Rectangle bounds = shape.getBounds();
                w = bounds.x + bounds.width + 2;
                size++;
            }
            size = font.getSize();
            if (size > 12) {
                size--;
            }
            font = new Font(fontname, style, size);
        }
        return font;
    }
}
