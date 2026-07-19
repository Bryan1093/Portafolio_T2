package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ejercicio_5 {

    /**
     * Ejercicio 5: Efecto Blanco y Negro puro.
     * Reduce los colores de la imagen a únicamente blanco (255) y negro (0).
     * Mantiene el canal Alpha intacto.
     */
    public static void aplicarBlancoYNegro(String rutaEntrada, String rutaSalida) {
        try {
            File archivoEntrada = new File(rutaEntrada);
            BufferedImage imagenOriginal = org.example.portafolio.utils.ImageIOManager.read(archivoEntrada);

            if (imagenOriginal == null) {
                System.out.println("Error: No se pudo cargar la imagen de entrada.");
                return;
            }

            int ancho = imagenOriginal.getWidth();
            int alto = imagenOriginal.getHeight();

            BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int colorPixel = imagenOriginal.getRGB(x, y);

                    int a = (colorPixel >> 24) & 0xff;
                    int r = (colorPixel >> 16) & 0xff;
                    int g = (colorPixel >> 8) & 0xff;
                    int b = colorPixel & 0xff;

                    int brillo = (r + g + b) / 3;

                    int valorFinal = (brillo > 127) ? 255 : 0;

                    int nuevoColor = (a << 24) | (valorFinal << 16) | (valorFinal << 8) | valorFinal;
                    imagenSalida.setRGB(x, y, nuevoColor);
                }
            }

            File archivoSalida = new File(rutaSalida);
            org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "png", archivoSalida);
            System.out.println("¡Listo! Imagen en Blanco y Negro generada: " + rutaSalida);

        } catch (IOException e) {
            System.out.println("Error al procesar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String rutaEntrada = "images/images.jpg";
        String rutaSalida = "images/resultado_blanco_negro.png";

        System.out.println("Iniciando Blanco y Negro extremo...");
        aplicarBlancoYNegro(rutaEntrada, rutaSalida);
    }
}
