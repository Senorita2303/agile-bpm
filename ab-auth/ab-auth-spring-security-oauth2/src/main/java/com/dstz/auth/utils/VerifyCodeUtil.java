package com.dstz.auth.utils;

import cn.hutool.core.util.StrUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Verification Code Tools
 *
 */
public final class VerifyCodeUtil {
    /* Define the width of the image */
    private static int WIDTH = 80;
    /* Define the height of the image */
    private static int HEIGHT = 30;
    /* Define the number of verification codes displayed on the image */
    private static int CODECOUNT = 4;
    /* Character spacing */
    private static final int CHARACETRSPACING = 8;
    /* Font size */
    private static final int FONTHEIGHT = 24;
    /* Interference line */
    private static final int LINENUMBER = 10;
    /* Character vertical position */
    private static final int VERTICALPOSITION = 24;
    private static final char[] CODESEQUENCE = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
            'P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n',
            'o','p','q','r','s','t','u','v','w','x','y','z','1','2','3','4','5','6','7','8','9','0'};
    /* No-argument constructor */
    private VerifyCodeUtil(){}

    /**
     * Generate verification code string
     * @param count Number of characters in verification code
     * @return Return verification code string
     */
    public static String generateVerifyCode(int count){
        // Generate random numbers
        Random random = ThreadLocalRandom.current();
        // Generate verification code
        StringBuilder randomCode = new StringBuilder();/* StringBuilder mutable string sequence */
        for (int i = 0; i < count; i++) {
            // Get a verification code
            String code = String.valueOf(CODESEQUENCE[random.nextInt(CODESEQUENCE.length)]);
            randomCode.append(code);/* String concatenation */
        }
        return randomCode.toString();
    }

    /**
     * Generate verification code string
     * @return Return verification code string
     */
    public static String generateVerifyCode(){// Method Overloading
        return generateVerifyCode(CODECOUNT);// Static methods can only call static methods
    }

    /**
     * Generate verification code image 
     * @param width The width of the verification code image, default is 95
     * @param  height The height of the verification code image, the default is 30
     * @param code Verification code string
     * @return Returns the created canvas object
     */
    public static BufferedImage outputImage(Integer width, Integer height, String code){
        if(!StrUtil.isEmpty(String.valueOf(width))){// Methods in the Spring Toolkit
            WIDTH = width;// Set the width for the image
        }
        if(!StrUtil.isEmpty(String.valueOf(height))){
            HEIGHT = height;// Set the height for the image
        }
        if(StrUtil.isEmpty(code)){
            return null;// These judgments are made to prevent null pointer exceptions.
        }
        // Create a canvas object (without transparent color)
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // Creating a Brush Object
        Graphics gd = buffImg.getGraphics();
        // Create a random number generator object
        Random random = ThreadLocalRandom.current();
        // Set canvas background color (white)
        gd.setColor(Color.WHITE);
        // Fill the canvas according to the set background color
        gd.fillRect(0, 0, WIDTH, HEIGHT);
        // Creating a font object
        Font font = new Font("Arial", Font.BOLD, FONTHEIGHT);
        // Set the font for the brush object
        gd.setFont(font);
        // Set the color of the brush object
        gd.setColor(Color.BLACK);
        // Draw a border around the canvas
        gd.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        // Generate verification code
        char[] codes = code.toCharArray();
        for (int i = 0; i < codes.length; i++) {
            // Setting Color
            gd.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            // Draw verification code
            gd.drawString(String.valueOf(codes[i]), CHARACETRSPACING + i * 2 * CHARACETRSPACING, VERTICALPOSITION);
        }
        // Set the interference line color (dark gray)
        gd.setColor(Color.DARK_GRAY);
        // Interference line
        for (int i = 0; i < LINENUMBER; i++) {
            // The starting X coordinate of the line
            int startX = random.nextInt(WIDTH);
            // The starting Y coordinate of the line
            int startY = random.nextInt(HEIGHT);
            // End X coordinate of the line
            int endX = random.nextInt(VERTICALPOSITION);
            // End Y coordinate of the line
            int endY = random.nextInt(VERTICALPOSITION);
            // Draw a straight line
            gd.drawLine(startX, startY, startX + endX, startY + endY);
        }
        return buffImg;
    }
    /**
     * Output verification code image stream
     * @param w Width
     * @param h Height
     * @param code Verification code string
     * @param os Output Stream
     */
    public static void outputImage(Integer w, Integer h, String code, OutputStream os) throws IOException {
        if(StrUtil.isEmpty(code)){
            return;
        }
        // Creating a Canvas Object
        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // Creating a Brush Object
        Graphics gd = bi.getGraphics();
        // Create a random number generator object
        Random random = ThreadLocalRandom.current();
        // Set the canvas background color
        gd.setColor(Color.DARK_GRAY);
        // Fill the canvas according to the set background color
        gd.fillRect(0, 0, WIDTH, HEIGHT);
        // Creating a font object
        Font font = new Font("Arial", Font.BOLD, FONTHEIGHT);
        // Set the font for the brush object
        gd.setFont(font);
        // Set the color for the brush
        gd.setColor(Color.BLACK);
        // Draw a border around the canvas
        //gd.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        // Set the color of the interference line
        gd.setColor(Color.BLACK);
        // Interference line
        for (int i = 0; i < LINENUMBER; i++) {
            // The starting X coordinate of the line
            int startX = random.nextInt(WIDTH);
            // The starting Y coordinate of the line
            int startY = random.nextInt(HEIGHT);
            // End X coordinate of the line
            int endX = random.nextInt(VERTICALPOSITION);
            // End Y coordinate of the line
            int endY = random.nextInt(VERTICALPOSITION);
            // Draw a straight line
            gd.drawLine(startX, startY, startX + endX, startY + endY);
        }
        // Generate verification code
        char[] codes = code.toCharArray();
        for (int i = 0; i < codes.length; i++) {
            // Setting Color
            gd.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            // Draw verification code
            gd.drawString(String.valueOf(codes[i]), CHARACETRSPACING + i * 2 * CHARACETRSPACING, VERTICALPOSITION);
        }
        gd.dispose();
        ImageIO.write(bi, "png", os);
    }
    /**
     * Output verification code image stream
     * @param code Verification Code
     * @param os Create an output stream
     */
    public static void outputImage(String code, OutputStream os) throws IOException {
        outputImage(WIDTH, HEIGHT, code, os);
    }



}
