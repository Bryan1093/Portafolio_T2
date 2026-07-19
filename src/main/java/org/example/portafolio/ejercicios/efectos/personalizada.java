package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class personalizada {

    public static void main(String[] args) {

        File file2 = new File("images/imagen_personalizada.jpg");
        int ancho = 900;
        int alto = 500;
        int r, g, b;
        int mascara = 0xFF;

        Random random = new Random();

        try {
            // Asegurarse de que la carpeta 'images' exista
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Ya no leemos ninguna imagen, creamos una directamente en memoria
            BufferedImage bufer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    // Generamos colores aleatorios para cada píxel
                    r = random.nextInt(256);
                    g = random.nextInt(256);
                    b = random.nextInt(256);

                    int pixelnuevo = (r << 16) | (g << 8) | (b & mascara);
                    bufer2.setRGB(x, y, pixelnuevo);
                }
            }

            if (org.example.portafolio.utils.ImageIOManager.write(bufer2, "jpg", file2)) {
                System.out.println("Imagen guardada exitosamente como " + file2.getPath());
            } else {
                System.out.println("Error al intentar guardar la imagen. Formato no válido.");
            }
            System.out.println("Imagen creada correctamente. Dimensiones: " + ancho + "x" + alto);

        } catch (IOException e) {
            System.out.println("Error al crear la imagen: " + e.getMessage());
        }

    }
}