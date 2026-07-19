package org.example.portafolio.ejercicios.transparencia;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class transparencia {

    public static void main(String[] args) {
        // Parámetros: [mode] [w1] [w2] [w3]
        // mode: "alpha" (default), "additive", "multiply"
        String mode = "alpha";
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            mode = args[0].toLowerCase();
        }

        float w1 = 0.33f;
        float w2 = 0.33f;
        float w3 = 0.34f;
        if (args.length >= 4) {
            try {
                w1 = Float.parseFloat(args[1]);
                w2 = Float.parseFloat(args[2]);
                w3 = Float.parseFloat(args[3]);
            } catch (NumberFormatException e) {
                System.out.println("Advertencia: pesos inválidos, usando 0.33/0.33/0.34");
                w1 = 0.33f;
                w2 = 0.33f;
                w3 = 0.34f;
            }
        }

        File file1 = new File("images/manchester.jpg");
        File file2 = new File("images/montanas.jpg");
        File file3 = new File("images/galaxia2.jpg"); //
        File fileSalida = new File("images/resultado_transparencia_" + mode + ".jpg");

        int mascara = 0xFF;

        try {
            BufferedImage bufer1 = org.example.portafolio.utils.ImageIOManager.read(file1);
            BufferedImage bufer2 = org.example.portafolio.utils.ImageIOManager.read(file2);
            BufferedImage bufer3 = org.example.portafolio.utils.ImageIOManager.read(file3);

            if (bufer1 == null || bufer2 == null || bufer3 == null) {
                System.out.println("Error: No se pudieron leer las imágenes.");
                return;
            }

            int ancho = Math.min(Math.min(bufer1.getWidth(), bufer2.getWidth()), bufer3.getWidth());
            int alto = Math.min(Math.min(bufer1.getHeight(), bufer2.getHeight()), bufer3.getHeight());

            Image impTemp2 = bufer2.getScaledInstance(ancho, alto, Image.SCALE_FAST);
            BufferedImage bufer2Temp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            Graphics2D grTemp2 = bufer2Temp.createGraphics();
            grTemp2.drawImage(impTemp2, 0, 0, null);
            grTemp2.dispose();
            bufer2 = bufer2Temp;

            Image impTemp3 = bufer3.getScaledInstance(ancho, alto, Image.SCALE_FAST);
            BufferedImage bufer3Temp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            Graphics2D grTemp3 = bufer3Temp.createGraphics();
            grTemp3.drawImage(impTemp3, 0, 0, null);
            grTemp3.dispose();
            bufer3 = bufer3Temp;

            BufferedImage buferSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int pixel1 = bufer1.getRGB(x, y);
                    int pixel2 = bufer2.getRGB(x, y);
                    int pixel3 = bufer3.getRGB(x, y);

                    int r1 = (pixel1 >> 16) & mascara;
                    int g1 = (pixel1 >> 8) & mascara;
                    int b1 = (pixel1 >> 0) & mascara;

                    int r2 = (pixel2 >> 16) & mascara;
                    int g2 = (pixel2 >> 8) & mascara;
                    int b2 = (pixel2 >> 0) & mascara;

                    int r3 = (pixel3 >> 16) & mascara;
                    int g3 = (pixel3 >> 8) & mascara;
                    int b3 = (pixel3 >> 0) & mascara;

                    int rOut;
                    int gOut;
                    int bOut;

                    switch (mode) {
                        case "additive":
                            rOut = r1 + r2 + r3;
                            gOut = g1 + g2 + g3;
                            bOut = b1 + b2 + b3;
                            break;
                        case "multiply":
                            // multiplicación de tres canales: normalizar por 255^2
                            rOut = (int) ((r1 * r2 * r3) / (255.0 * 255.0));
                            gOut = (int) ((g1 * g2 * g3) / (255.0 * 255.0));
                            bOut = (int) ((b1 * b2 * b3) / (255.0 * 255.0));
                            break;
                        case "alpha":
                        default:
                            rOut = (int) (w1 * r1 + w2 * r2 + w3 * r3);
                            gOut = (int) (w1 * g1 + w2 * g2 + w3 * g3);
                            bOut = (int) (w1 * b1 + w2 * b2 + w3 * b3);
                            break;
                    }

                    // Clamp channels to [0,255]
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
