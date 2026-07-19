package org.example.portafolio.ejercicios.ecualizador.processor;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ModosAvanzados {

    // ESCALA DE GRISES
    private static BufferedImage convertToGray(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = input.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                gray = ProcesadorImagen.clamp(gray);
                output.setRGB(x, y, (gray << 16) | (gray << 8) | gray);
            }
        }
        return output;
    }

    public static BufferedImage equalizeGray(BufferedImage input, float alpha) {
        BufferedImage grayImg = convertToGray(input);
        int width = grayImg.getWidth();
        int height = grayImg.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int[] grayHist = ProcesadorImagen.calculateHistogram(grayImg)[0];
        int[] lut = ProcesadorImagen.calculateLUT(grayHist, width * height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = grayImg.getRGB(x, y);
                int grayValue = rgb & 0xFF;
                int eqValue = lut[grayValue];
                int weightedValue = Math.round((1.0f - alpha) * grayValue + alpha * eqValue);
                weightedValue = ProcesadorImagen.clamp(weightedValue);
                output.setRGB(x, y, (weightedValue << 16) | (weightedValue << 8) | weightedValue);
            }
        }
        return output;
    }

    // HSB (BRILLO)
    public static BufferedImage equalizeHSB(BufferedImage input, float alpha) {
        int width = input.getWidth();
        int height = input.getHeight();
        int totalPixels = width * height;
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        float[] hArr = new float[totalPixels];
        float[] sArr = new float[totalPixels];
        int[] bValues = new int[totalPixels];
        int[] brightnessHist = new int[256];

        int idx = 0;
        float[] hsb = new float[3];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = input.getRGB(x, y);
                Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, hsb);

                hArr[idx] = hsb[0];
                sArr[idx] = hsb[1];
                int brightnessVal = ProcesadorImagen.clamp(Math.round(hsb[2] * 255.0f));
                bValues[idx] = brightnessVal;
                brightnessHist[brightnessVal]++;
                idx++;
            }
        }

        int[] lut = ProcesadorImagen.calculateLUT(brightnessHist, totalPixels);

        idx = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int origB = bValues[idx];
                float weightedB = ((1.0f - alpha) * origB + alpha * lut[origB]) / 255.0f;
                weightedB = Math.max(0.0f, Math.min(1.0f, weightedB));

                output.setRGB(x, y, Color.HSBtoRGB(hArr[idx], sArr[idx], weightedB));
                idx++;
            }
        }
        return output;
    }
}