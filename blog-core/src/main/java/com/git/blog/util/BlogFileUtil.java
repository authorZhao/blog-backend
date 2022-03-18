package com.git.blog.util;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author authorZhao
 */
public class BlogFileUtil {
    public static final Logger logger = LoggerFactory.getLogger(BlogFileUtil.class);
    /**
     * 路径直接生成文件
     * @param path f:/aaa/ccc/a.txt
     *             /asd/cdv/ass
     * @return 文件
     */
    public static File getFile(String path){
        path = pathFormat(path);
        String[] str = path.split("/");
        String index = str[str.length-1];
        String dirPath = path.substring(0,path.lastIndexOf(index));
        File dir = new File(dirPath);
        if(!dir.exists())dir.mkdirs();
        File file = new File(dirPath+"/"+index);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!file.isFile())throw new RuntimeException("this is not a file");
        logger.info("文件正在生成，路径为：    "+ file.getPath());
        return file;
    }

    public static Boolean existDir(String path){
        path = pathFormat(path);
        File file = new File(path);
        if(file.exists()&&file.isDirectory())return true;
        if(!file.exists()||!file.isDirectory())file.mkdirs();
        return false;
    }


    /**
     * 通过文件后缀获取文件类型
     * @param path
     * @return txt等
     */
    public static String getFileType(String path){
        return path.substring(path.lastIndexOf(".")+1);
    }

    /**
     * 格式化路径
     * @param path a\\c\\c//v///v
     * @return a/c/c/v/v
     */
    public static String pathFormat(String path) {
        path = path.replaceAll("\\\\", "/");
        if(path.contains("//")) {
            path = path.replaceAll("//", "/");
            return pathFormat(path);
        }else {
            return path;
        }
    }

    /**
     * 判断文件是否是图片
     * @param picture
     * @return
     */
    public static boolean isPicture(MultipartFile picture){
        InputStream in = null;
        try {
            in = picture.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isPicture(in);
    }
    public static boolean isPicture(File file){
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return isPicture(in);
    }
    public static boolean isPicture(InputStream in){
        if (in == null) {
            return false;
        }
        Image img;
        try {
            img = ImageIO.read(in);
            return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 通过文件路径获得文件名，不含后缀
     * @param path f:////asdf\\asd\\asdf//a.txt
     * @return a
     */
    public static String getFileName(String path){
        path = pathFormat(path);
        path = path.substring(path.lastIndexOf("/")+1);
        return path.substring(0,path.lastIndexOf("."));
    }



    /**
     * 保证文件的MD5值为32位
     * @param filePath  文件路径
     * @return
     * @throws FileNotFoundException
     */
    public static String md5HashCode32(String filePath) throws FileNotFoundException{
        FileInputStream fis = new FileInputStream(filePath);
        return md5HashCode32(fis);
    }

    public static String md5HashCode32(File file){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return md5HashCode32(fis);
    }


    /**
     * java计算文件32位md5值
     * @param fis 输入流
     * @return
     */
    public static String md5HashCode32(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();

            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes  = md.digest();
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;//解释参见最下方
                if (val < 16) {
                    /**
                     * 如果小于16，那么val值的16进制形式必然为一位，
                     * 因为十进制0,1...9,10,11,12,13,14,15 对应的 16进制为 0,1...9,a,b,c,d,e,f;
                     * 此处高位补0。
                     */
                    hexValue.append("0");
                }
                //这里借助了Integer类的方法实现16进制的转换
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        String s1 = md5HashCode32(new File("C:\\Users\\authorZhao\\Desktop\\note\\wallpaper\\wallhaven-57zww1.jpg"));
        String s2 = md5HashCode32(new File("C:\\Users\\authorZhao\\Desktop\\note\\wallpaper\\wallhaven-57zww1本.jpg"));
        String s3 = md5HashCode32(new File("C:\\Users\\authorZhao\\Desktop\\note\\wallpaper\\wallhaven-rdqv8w.jpg"));
        System.out.println("s1 = " + s1);
        System.out.println("s2 = " + s2);
        System.out.println("s3 = " + s3);
    }


}




