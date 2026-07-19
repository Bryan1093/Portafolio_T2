package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ejercicio_2 {

    /**
     * Ejercicio 2: Desvanecimiento circular
     * La imagen es totalmente opaca en el centro y se vuelve transparente hacia las
     * esquinas.
     * Calcula la distancia del píxel al centro para determinar el canal Alpha.
     */
    public static void aplicarDesvanecimientoCircular(String rutaEntrada, String rutaSalida) {
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

            double centroX = ancho / 2.0;
            double centroY = alto / 2.0;
            double distanciaMaxima = Math.sqrt(Math.pow(centroX, 2) + Math.pow(centroY, 2));

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int colorPixel = imagenOriginal.getRGB(x, y);

                    int r = (colorPixel >> 16) & 0xff;
                    int g = (colorPixel >> 8) & 0xff;
                    int b = colorPixel & 0xff;

                    double distanciaAlCentro = Math.sqrt(Math.pow(x - centroX, 2) + Math.pow(y - centroY, 2));

                    double proporcion = 1.0 - (distanciaAlCentro / distanciaMaxima);
                    int nuevoAlpha = (int) (255 * proporcion);

                    nuevoAlpha = Math.max(0, Math.min(255, nuevoAlpha));

                    int nuevoColor = (nuevoAlpha << 24) | (r << 16) | (g << 8) | b;
                    imagenSalida.setRGB(x, y, nuevoColor);
                }
            }
            File archivoSalida = new File(rutaSalida);
            org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "png", archivoSalida);
            System.out.println("¡Listo! Imagen con desvanecimiento circular guardada en: " + rutaSalida);

        } catch (IOException e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String rutaEntrada = "images/images.jpg";

        String rutaSalida = "images/resultado_circular.png";

        System.out.println("Iniciando el proceso de desvanecimiento circular...");
        aplicarDesvanecimientoCircular(rutaEntrada, rutaSalida);
    }
}
