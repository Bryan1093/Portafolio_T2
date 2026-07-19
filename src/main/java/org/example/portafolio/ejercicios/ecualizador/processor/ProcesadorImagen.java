package org.example.portafolio.ejercicios.ecualizador.processor;

import java.awt.image.BufferedImage;

public class ProcesadorImagen {

    public static int[][] calculateHistogram(BufferedImage image) {
        int[][] histogram = new int[3][256];
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                histogram[0][r]++;
                histogram[1][g]++;
                histogram[2][b]++;
            }
        }
        return histogram;
    }

    static int[] calculateLUT(int[] hist, int totalPixels) {
        int[] cdf = new int[256];
        cdf[0] = hist[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + hist[i];
        }

        int cdfMin = 0;
        for (int i = 0; i < 256; i++) {
            if (cdf[i] > 0) {
                cdfMin = cdf[i];
                break;
            }
        }

        int[] lut = new int[256];
        int denominator = totalPixels - cdfMin;
        for (int i = 0; i < 256; i++) {
            if (denominator > 0) {
                float normalized = (float) (cdf[i] - cdfMin) / denominator;
                lut[i] = Math.round(normalized * 255);
            } else {
                lut[i] = i;
            }
        }
        return lut;
    }

    public static BufferedImage equalize(BufferedImage input, float alpha) {
        int width = input.getWidth();
        int height = input.getHeight();
        int totalPixels = width * height;
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[][] hist = calculateHistogram(input);
        int[] lutR = calculateLUT(hist[0], totalPixels);
        int[] lutG = calculateLUT(hist[1], totalPixels);
        int[] lutB = calculateLUT(hist[2], totalPixels);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = input.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int eqR = Math.round((1 - alpha) * r + alpha * lutR[r]);
                int eqG = Math.round((1 - alpha) * g + alpha * lutG[g]);
                int eqB = Math.round((1 - alpha) * b + alpha * lutB[b]);

                eqR = clamp(eqR);
                eqG = clamp(eqG);
                eqB = clamp(eqB);

                output.setRGB(x, y, (eqR << 16) | (eqG << 8) | eqB);
            }
        }
        return output;
    }

    static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}