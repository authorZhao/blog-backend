package com.git.blog.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;


public class VerificationCode {
    private static final int width = 100;// 生成验证码图片的宽度
    private static final int height = 40;// 生成验证码图片的高度
    private static final String[] fontNames = { "宋体", "楷体", "隶书", "微软雅黑" };
    private static final Color WHITE_COLOR = new Color(255, 255, 255);// 定义验证码图片的背景颜色为白色
    private static final Color BLACK_COLOR = new Color(0, 253, 229);// 定义验证码图片的背景颜色为白色
    private static final Random random = new Random();
    private static final String codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String NUMBERS = "①②③④⑤⑥⑦⑧⑨⑩";

    /**
     * 获取一个随意颜色
     *
     * @return
     */
    private static Color randomColor() {
        int red = random.nextInt(150);
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        return new Color(red, green, blue);
    }

    /**
     * 获取一个随机字体
     *
     * @return
     */
    private static Font randomFont() {
        String name = fontNames[random.nextInt(fontNames.length)];
        int style = random.nextInt(4);
        int size = random.nextInt(5) + 24;
        return new Font(name, style, size);
    }

    /**
     * 获取一个随机字符
     *
     * @return
     */
    private char randomChar() {
        return codes.charAt(random.nextInt(codes.length()));
    }

    /**
     * 创建一个空白的BufferedImage对象
     *
     * @return
     */
    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(WHITE_COLOR);// 设置验证码图片的背景颜色
        g2.fillRect(0, 0, width, height);
        return image;
    }

    public BufferedImage getImage() {
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            g2.setColor(randomColor());
            g2.setFont(randomFont());
            float x = i * width * 1.0f / 4;
            g2.drawString(s, x, height - 8);
        }
        drawLine(image);
        return image;
    }

    public static BufferedImage genCode(String text) {
       return genCode(text, width, height);
    }

     ///    @param text 验证码
     ///    @param width 长
     ///    @param height 宽
     ///    @return
     ///
    public static BufferedImage genCode(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(WHITE_COLOR);// 设置验证码图片的背景颜色
        g2.fillRect(0, 0, width, height);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            g2.setColor(randomColor());
            g2.setFont(randomFont());
            float x = i * width * 1.0f / 4;
            g2.drawString(String.valueOf(c), x, height - 8);
        }
        drawLine(image);
        return image;
    }


    /**
     * 绘制干扰线
     *
     * @param image
     */
    private static void drawLine(BufferedImage image) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        int num = 5;
        for (int i = 0; i < num; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2.setColor(randomColor());
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(x1, y1, x2, y2);
        }
    }


    public static void output(BufferedImage image, OutputStream out) throws IOException {
        ImageIO.write(image, "JPEG", out);
    }

    public static String output(BufferedImage image) throws IOException {
        var out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        var bytes = Base64.encodeBase64(out.toByteArray());
        return "data:image/png;base64," + new String(bytes, StandardCharsets.UTF_8);

    }

    public static String genRandomStr(){
        var sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            var index = random.nextInt(codes.length()-1);
            sb.append(codes.charAt(index));
        }
        return sb.toString();
    }

}

