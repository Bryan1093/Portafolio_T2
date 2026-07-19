package org.example.portafolio.ejercicios.efectos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Filtros {

    public static void main(String[] args) {

        File file = new File("images/images.png");
        File file2 = new File("images/images2.png");

        int ancho, alto, pixel, pixelNuevo;
        int a, r, g, b;
        // int mascara = 0b11111111;

        int mascara = 0xFF;

        Random rd = new Random();
        int brillo = 40;

        float[] hsv = new float[3];
        float h, s, v;

        float factorS = 1.3f;
        float factorB = 0.9f;
        float factorT = 0.8f;

        // Color color;

        int mascaraRecortarBit = 0b1111;
        int mascaraDecimal = 15;
        int mascaraHex = 0xF;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    pixel = bufer.getRGB(x, y);

                    // color = new Color(pixel);

                    // a = color.getAlpha();

                    a = (pixel >> 24) & 0xFF;
                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel >> 0) & 0xFF;

                    a = (int) (Math.min(255, a * factorT));

                    // RECORTE A 4 BITS
                    r = (r >> 4) & mascaraRecortarBit;
                    g = (g >> 4) & mascaraRecortarBit;
                    b = (b >> 4) & mascaraRecortarBit;

                    // DEBER: ESTIRAR CON LA FORMULA Y MASCARA DE DECIMAL Y HEXADECIMAL

                    // ESTIRAR
                    r = (r * 255) / mascaraRecortarBit;
                    g = (g * 255) / mascaraRecortarBit;
                    b = (b * 255) / mascaraRecortarBit;

                    // ----------------------------------------
                    // RECORTE A 4 BITS - DECIMAL
                    r = (r >> 4) & mascaraDecimal;
                    g = (g >> 4) & mascaraDecimal;
                    b = (b >> 4) & mascaraDecimal;

                    // ESTIRAR
                    r = (r * 255) / mascaraDecimal;
                    g = (g * 255) / mascaraDecimal;
                    b = (b * 255) / mascaraDecimal;

                    // ----------------------------------------
                    // RECORTE A 4 BITS - HEXADECIMAL
                    r = (r >> 4) & mascaraHex;
                    g = (g >> 4) & mascaraHex;
                    b = (b >> 4) & mascaraHex;

                    // ESTIRAR
                    r = (r * 255) / mascaraHex;
                    g = (g * 255) / mascaraHex;
                    b = (b * 255) / mascaraHex;
                    // ----------------------------------------

                    /*
                     * //MODELO BASTANTE UTIL PARA VER LOS COLORES REALES
                     * hsv = Color.RGBtoHSB(r, g, b, null);
                     * 
                     * h = hsv[0];
                     * s = hsv[1];
                     * v = hsv[2];
                     * 
                     * s = Math.min(1, s * factorS);
                     * v = Math.min(1, v * factorB);
                     * pixelNuevo = Color.HSBtoRGB(h, s, v);
                     * 
                     * pixelNuevo = (a << 24) | (pixelNuevo & 0x00FFFFFF);
                     * 
                     */

                    pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);
                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(bufer2, "png", file2);
            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }

    // Transparencia
    public static void transparencia(String[] args) {

        File file = new File("images/images.jpg");
        File file2 = new File("images/images2.jpg");

        int ancho, alto, pixel, pixelNuevo;
        int a, r, g, b;
        // int mascara = 0b11111111;

        int mascara = 0xFF;

        Random rd = new Random();
        int brillo = 40;

        float[] hsv = new float[3];
        float h, s, v;

        float factorS = 1.3f;
        float factorB = 0.9f;
        float factorT = 1.2f;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    pixel = bufer.getRGB(x, y);

                    a = (pixel >> 24) & 0xFF;
                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel >> 0) & 0xFF;

                    a = (int) (Math.min(255, a * factorT));

                    pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);
                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(bufer2, "png", file2);
            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }

    // Saturacion Brillo
    public static void saturacionBrillo(String[] args) {

        File file = new File("images/images.png");
        File file2 = new File("images/images2.png");

        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;
        // int mascara = 0b11111111;

        int mascara = 0xFF;

        Random rd = new Random();
        int brillo = 40;

        float[] hsv = new float[3];
        float h, s, v;

        float factorS = 1.0f;
        float factorB = 1.15f;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    pixel = bufer.getRGB(x, y);

                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel >> 0) & 0xFF;

                    hsv = Color.RGBtoHSB(r, g, b, null);

                    h = hsv[0];
                    s = hsv[1];
                    v = hsv[2];

                    s = Math.min(1, s * factorS);
                    v = Math.min(1, v * factorB);

                    pixelNuevo = Color.HSBtoRGB(h, s, v);

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(bufer2, "jpg", file2);
            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }

    // brillo
    public static void brillo(String[] args) {

        File file = new File("images/images.png");
        File file2 = new File("images/images2.png");

        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;
        // int mascara = 0b11111111;

        int mascara = 0xFF;

        Random rd = new Random();

        int brillo = 80;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    pixel = bufer.getRGB(x, y);

                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel >> 0) & 0xFF;

                    r = Math.min(255, (r + brillo));
                    g = Math.min(255, (g + brillo));
                    b = Math.min(255, (b + brillo));

                    pixelNuevo = (r << 16) | (g << 8) | (b << 0);

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(bufer2, "jpg", file2);
            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }

    // Negativo
    public static void negativo(String[] args) {

        File file = new File("images/images.png");
        File file2 = new File("images/images2.png");

        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;
        // int mascara = 0b11111111;

        int mascara = 0xFF;

        Random rd = new Random();

        int gris;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    pixel = bufer.getRGB(x, y);

                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel >> 0) & 0xFF;

                    r = (255 - r);
                    g = (255 - g);
                    b = (255 - b);

                    pixelNuevo = (r << 16) | (g << 8) | (b << 0);

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(bufer2, "jpg", file2);
            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }

    // Escala de Grises
    public static void escalaGrises(String[] args) {

        File file = new File("images/images.png");
        File file2 = new File("images/images2.png");

        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;
        // int mascara = 0b11111111;

        int mascara = 0xFF;

        Random rd = new Random();

        int gris;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    pixel = bufer.getRGB(x, y);

                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel >> 0) & 0xFF;

                    r = (255 - r);
                    g = (255 - r);
                    b = (255 - r);

                    gris = (r + g + b) / 3;
                    // gris = (int)(0.2126*r + 0.7152*g +0.0722*b);

                    pixelNuevo = (gris << 16) | (gris << 8) | (gris << 0);

                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(bufer2, "jpg", file2);
            System.out.println("Imagen creada correctamente");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }

    // Duplicado de imagenes
    public static void duplicarImagen(String[] args) {
        File file = new File("images/images.png");
        File file2 = new File("images/images2.png");

        int ancho;
        int alto;
        int pixel;
        int pixelNuevo;

        int r;
        int g;
        int b;

        int mascara = 0xFF;

        Random random = new Random();

        int binario = (7 >> 2);

        try {
            ancho = 900;
            alto = 500;

            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    r = random.nextInt(256);
                    g = random.nextInt(256);
                    b = 250;

                    // System.out.println("(" + r + ", " + g + ", " + b + ")");

                    pixelNuevo = (r << 16) | (g << 8) | (b << 0);
                    bufer2.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(bufer2, "jpg", file2);
            System.out.println("Imagen guardada");
        } catch (Exception e) {
            System.out.println("Error al leer la imagen" + e.getMessage());
        }
    }
}