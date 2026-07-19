package org.example.portafolio.ejercicios.colores;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Imagen {

    public static void main(String[] args) {

        File file = new File("images/imagen.jpg");
        File file2 = new File("images/imagen_copia.jpg");
        int ancho, alto, pixel, pixelnuevo;
        int r, g, b;
        int mascara = 0xFF;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            if (bufer == null) {
                System.out.println("Error: No se pudo decodificar la imagen (formato no soportado o archivo vacío).");
                return;
            }

            ancho = bufer.getWidth();
            alto = bufer.getHeight();
            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    pixel = bufer.getRGB(x, y);

                    // System.out.println("Pixel en (" + x + "," + y + "): " + pixel);
                    r = (pixel >> 16) & mascara;
                    g = (pixel >> 8) & mascara;
                    b = (pixel >> 0) & mascara;

                    pixelnuevo = (r << 16) | (g << 8) | (b & mascara);
                    bufer2.setRGB(x, y, pixelnuevo);
                }
            }

            if (org.example.portafolio.utils.ImageIOManager.write(bufer2, "jpg", file2)) {
                System.out.println("Imagen guardada exitosamente como " + file2.getPath());
            } else {
                System.out.println("Error al intentar guardar la imagen. Formato no válido.");
            }
            System.out.println("Imagen procesada correctamente. Dimensiones: " + ancho + "x" + alto);

        } catch (IOException e) {
            // System.out.println("Error al leer imagen: " + e.getMessage());
        }

    }
}