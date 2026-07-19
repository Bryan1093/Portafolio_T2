package org.example.portafolio.ejercicios.transparencia;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class transparencia2 {

    public static void main(String[] args) {
        String mode = "alpha";
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            mode = args[0].toLowerCase();
        }

        float w1 = 0.5f;
        float w2 = 0.5f;
        if (args.length >= 3) {
            try {
                w1 = Float.parseFloat(args[1]);
                w2 = Float.parseFloat(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Advertencia: pesos inválidos, usando 0.5/0.5");
                w1 = 0.5f;
                w2 = 0.5f;
            }
        }

        File file1 = new File("images/manchester.jpg");
        File file2 = new File("images/montanas.jpg");
        File fileSalida = new File("images/resultado_transparencia2_" + mode + ".jpg");

        int mascara = 0xFF;

        try {
            BufferedImage bufer1 = org.example.portafolio.utils.ImageIOManager.read(file1);
            BufferedImage bufer2 = org.example.portafolio.utils.ImageIOManager.read(file2);

            if (bufer1 == null || bufer2 == null) {
                System.out.println("Error: No se pudieron leer las imágenes.");
                return;
            }

            int ancho = Math.min(bufer1.getWidth(), bufer2.getWidth());
            int alto = Math.min(bufer1.getHeight(), bufer2.getHeight());

            Image impTemp2 = bufer2.getScaledInstance(ancho, alto, Image.SCALE_FAST);
            BufferedImage bufer2Temp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            Graphics2D grTemp2 = bufer2Temp.createGraphics();
            grTemp2.drawImage(impTemp2, 0, 0, null);
            grTemp2.dispose();
            bufer2 = bufer2Temp;

            BufferedImage buferSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int pixel1 = bufer1.getRGB(x, y);
                    int pixel2 = bufer2.getRGB(x, y);

                    int r1 = (pixel1 >> 16) & mascara;
                    int g1 = (pixel1 >> 8) & mascara;
                    int b1 = (pixel1 >> 0) & mascara;

                    int r2 = (pixel2 >> 16) & mascara;
                    int g2 = (pixel2 >> 8) & mascara;
                    int b2 = (pixel2 >> 0) & mascara;

                    int rOut;
                    int gOut;
                    int bOut;

                    switch (mode) {
                        case "additive":
                            rOut = r1 + r2;
                            gOut = g1 + g2;
                            bOut = b1 + b2;
                            break;
                        case "multiply":
                            rOut = (r1 * r2) / 255;
                            gOut = (g1 * g2) / 255;
                            bOut = (b1 * b2) / 255;
                            break;
                        case "alpha":
                        default:
                            rOut = (int) (w1 * r1 + w2 * r2);
                            gOut = (int) (w1 * g1 + w2 * g2);
                            bOut = (int) (w1 * b1 + w2 * b2);
                            break;
                    }

                    // Clamp
                    rOut = Math.min(255, Math.max(0, rOut));
                    gOut = Math.min(255, Math.max(0, gOut));
                    bOut = Math.min(255, Math.max(0, bOut));

                    int pixelNuevo = (rOut << 16) | (gOut << 8) | bOut;
                    buferSalida.setRGB(x, y, pixelNuevo);
                }
            }

            if (org.example.portafolio.utils.ImageIOManager.write(buferSalida, "jpg", fileSalida)) {
                System.out.println("Imagen guardada!");
            }

        } catch (IOException e) {
            System.out.println("Error al procesar: " + e.getMessage());
        }
    }
}
