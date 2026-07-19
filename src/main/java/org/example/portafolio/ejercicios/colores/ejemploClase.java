package org.example.portafolio.ejercicios.colores;

import java.awt.image.BufferedImage;
import java.io.File;

public class ejemploClase {

    public static void ejemplo(String rutaEntrada, String rutaSalida) {
        try {
            File imagenEntrada = new File(rutaEntrada);
            BufferedImage imagenOriginal = org.example.portafolio.utils.ImageIOManager.read(imagenEntrada);

            if (imagenOriginal == null) {
                System.out.println("Error");
                return;
            }

            int ancho = imagenOriginal.getWidth();
            int alto = imagenOriginal.getHeight();

            BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_4BYTE_ABGR);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int imagenPixel = imagenOriginal.getRGB(x, y);

                    float a = (imagenPixel >> 24) & 0xff;
                    int r = (imagenPixel >> 16) & 0xFF;
                    int g = (imagenPixel >> 8) & 0xFF;
                    int b = imagenPixel & 0xFF;

                    int nuevoColor = ((int) (0.8f * a) << 24) | (209 << 16) | (158 << 8) | 37;

                    imagenSalida.setRGB(x, y, nuevoColor);

                }
            }

            File archivoSalida = new File(rutaSalida);
            org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "jpg", archivoSalida);
            System.out.println("Listo: Imagen generada en " + rutaSalida);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String rutaEntrada = "images\\manu.jpg";
        String rutaSalida = "images\\manu2.jpg";
        ejemplo(rutaEntrada, rutaSalida);

    }
}
