package org.example.portafolio.ejercicios.transparencia;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public class Blending {
    static int alto, ancho, pixel, pixel2, pixelNuevo;
    static int mascara = 0x03;

    public static void main(String[] args) {
        File file1 = new File("images/anime.png");
        File file2 = new File("images/galaxia.png");
        File salida = new File("images/salida.png");

        float[] hsb;

        try {
            BufferedImage bufer1 = org.example.portafolio.utils.ImageIOManager.read(file1);
            BufferedImage bufer2 = org.example.portafolio.utils.ImageIOManager.read(file2);

            alto = bufer1.getHeight();
            ancho = bufer1.getWidth();

            Image impTemp2 = bufer2.getScaledInstance(ancho, alto, Image.SCALE_FAST);
            BufferedImage bufer2Temp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            Graphics2D grTemp2 = bufer2Temp.createGraphics();
            grTemp2.drawImage(impTemp2, 0, 0, null);
            grTemp2.dispose();

            BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    pixel = bufer1.getRGB(x, y);
                    pixel2 = bufer2Temp.getRGB(x, y);

                    int a1 = (pixel >> 24) & 0xFF;
                    int r1 = (pixel >> 16) & 0xFF;
                    int g1 = (pixel >> 8) & 0xFF;
                    int b1 = pixel & 0xFF;

                    int a2 = (pixel2 >> 24) & 0xFF;
                    int r2 = (pixel2 >> 16) & 0xFF;
                    int g2 = (pixel2 >> 8) & 0xFF;
                    int b2 = pixel2 & 0xFF;

                    r2 = (((r2 >> 6) & mascara)) * 255 / 3;
                    g2 = (((g2 >> 6) & mascara)) * 255 / 3;
                    b2 = (((b2 >> 6) & mascara)) * 255 / 3;

                    hsb = Color.RGBtoHSB(r2, g2, b2, null);

                    float h = hsb[0];
                    float s = hsb[1];
                    float v = hsb[2];

                    s = Math.min(1.0f, s * 1.2f);
                    v = Math.max(0.0f, v * 0.9f);

                    int nuevoRGB = Color.HSBtoRGB(h, s, v);

                    a2 = (nuevoRGB >> 24) & 0xFF;
                    r2 = (nuevoRGB >> 16) & 0xFF;
                    g2 = (nuevoRGB >> 8) & 0xFF;
                    b2 = (nuevoRGB) & 0xFF;

                    int r = ((r1 * r1) + (r2 * (255 - r1))) / 255;
                    int g = ((g1 * g1) + (g2 * (255 - g1))) / 255;
                    int b = ((b1 * b1) + (b2 * (255 - b1))) / 255;
                    g = Math.min(Math.max(g, 0), 255);
                    b = Math.min(Math.max(b, 0), 255);

                    int na = 255;

                    pixelNuevo = (a2 << 24) | (r << 16) | (g << 8) | b;
                    resultado.setRGB(x, y, pixelNuevo);
                }
            }
            org.example.portafolio.utils.ImageIOManager.write(resultado, "png", salida);
            System.out.println("Imagen creada");
        } catch (Exception e) {
            System.out.println("Error = " + e);
        }
    }
}
