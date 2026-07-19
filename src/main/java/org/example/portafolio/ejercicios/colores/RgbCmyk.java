package org.example.portafolio.ejercicios.colores;

import java.awt.image.BufferedImage;
import java.io.File;

public class RgbCmyk {
    public static void main(String[] args) {
        File file = new File("images/galaxia.png");
        try {
            BufferedImage img = org.example.portafolio.utils.ImageIOManager.read(file);
            if (img == null) {
                System.out.println("Error: No se pudo leer la imagen.");
                return;
            }

            int w = img.getWidth();
            int h = img.getHeight();

            BufferedImage imgC = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            BufferedImage imgM = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            BufferedImage imgY = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            BufferedImage imgK = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            double sumR = 0, sumG = 0, sumB = 0;
            double sumC = 0, sumM = 0, sumY = 0, sumK = 0;

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = img.getRGB(x, y);
                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int b = pixel & 0xFF;

                    sumR += r;
                    sumG += g;
                    sumB += b;

                    double rP = r / 255.0;
                    double gP = g / 255.0;
                    double bP = b / 255.0;

                    double max = Math.max(rP, Math.max(gP, bP));
                    double k = 1.0 - max;

                    double c, m, yC;
                    if (k == 1.0) {
                        c = 0;
                        m = 0;
                        yC = 0;
                    } else {
                        c = (1.0 - rP - k) / (1.0 - k);
                        m = (1.0 - gP - k) / (1.0 - k);
                        yC = (1.0 - bP - k) / (1.0 - k);
                    }

                    sumC += c;
                    sumM += m;
                    sumY += yC;
                    sumK += k;

                    int cVal = (int) Math.round(c * 255);
                    int mVal = (int) Math.round(m * 255);
                    int yVal = (int) Math.round(yC * 255);
                    int kVal = (int) Math.round(k * 255);

                    imgC.setRGB(x, y, (0 << 16) | (cVal << 8) | cVal);
                    imgM.setRGB(x, y, (mVal << 16) | (0 << 8) | mVal);
                    imgY.setRGB(x, y, (yVal << 16) | (yVal << 8) | 0);
                    imgK.setRGB(x, y, (kVal << 16) | (kVal << 8) | kVal);
                }
            }

            int totalPixels = w * h;
            double avgR = sumR / totalPixels;
            double avgG = sumG / totalPixels;
            double avgB = sumB / totalPixels;
            double avgC = sumC / totalPixels;
            double avgM = sumM / totalPixels;
            double avgY = sumY / totalPixels;
            double avgK = sumK / totalPixels;

            System.out.println("=== CONVERSIÓN RGB -> CMYK ===");
            System.out.println("Dimensiones: " + w + "x" + h + " píxeles");
            System.out.println("RGB Promedio: R=" + String.format("%.1f", avgR) + ", G=" + String.format("%.1f", avgG)
                    + ", B=" + String.format("%.1f", avgB));
            System.out.println("CMYK Promedio: C=" + String.format("%.1f", avgC * 100) + "%, M="
                    + String.format("%.1f", avgM * 100) + "%, Y=" + String.format("%.1f", avgY * 100) + "%, K="
                    + String.format("%.1f", avgK * 100) + "%");
            System.out.println();
            System.out.println("Lista completa de píxeles (Valores de conversión RGB -> CMYK):");

            int printedCount = 0;
            int maxPrintLimit = 5000;
            boolean truncated = false;

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = img.getRGB(x, y);
                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int b = pixel & 0xFF;

                    double rP = r / 255.0;
                    double gP = g / 255.0;
                    double bP = b / 255.0;
                    double k = 1.0 - Math.max(rP, Math.max(gP, bP));
                    double c = (k == 1.0) ? 0 : (1.0 - rP - k) / (1.0 - k);
                    double m = (k == 1.0) ? 0 : (1.0 - gP - k) / (1.0 - k);
                    double yC = (k == 1.0) ? 0 : (1.0 - bP - k) / (1.0 - k);

                    int cVal = (int) Math.round(c * 100);
                    int mVal = (int) Math.round(m * 100);
                    int yVal = (int) Math.round(yC * 100);
                    int kVal = (int) Math.round(k * 100);

                    if (printedCount < maxPrintLimit) {
                        System.out.println(String.format("Píxel [%d,%d]: RGB(%d,%d,%d) -> CMYK(%d%%,%d%%,%d%%,%d%%)",
                                x, y, r, g, b, cVal, mVal, yVal, kVal));
                        printedCount++;
                    } else {
                        truncated = true;
                        break;
                    }
                }
                if (truncated) {
                    break;
                }
            }

            if (truncated) {
                System.out.println();
                System.out.println(">> [... Lista truncada en los primeros " + maxPrintLimit
                        + " píxeles para evitar bloquear la interfaz gráfica ...]");
                System.out.println(">> Total de píxeles en la imagen: " + totalPixels);
            }

            org.example.portafolio.utils.ImageIOManager.write(imgC, "jpg", new File("images/Canal_Cyan.jpg"));
            org.example.portafolio.utils.ImageIOManager.write(imgM, "jpg", new File("images/Canal_Magenta.jpg"));
            org.example.portafolio.utils.ImageIOManager.write(imgY, "jpg", new File("images/Canal_Yellow.jpg"));
            org.example.portafolio.utils.ImageIOManager.write(imgK, "jpg", new File("images/Canal_Black_Key.jpg"));

            System.out.println();
            System.out.println("Imágenes de los canales de tinta (C, M, Y, K) generadas con éxito.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
