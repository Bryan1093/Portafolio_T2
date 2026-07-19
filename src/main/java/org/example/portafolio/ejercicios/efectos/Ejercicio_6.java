package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ejercicio_6 {

    /**
     * Ejercicio 6: Efecto escala de grises con posterización (Efecto Retro)
     * Convierte la imagen a escala de grises, pero limitando los tonos de gris
     * a una cantidad N de valores permitidos.
     */
    public static void aplicarEscalaDeGrisesRetro(String rutaEntrada, String rutaSalida, int N) {
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

            if (N < 2)
                N = 2;
            if (N > 256)
                N = 256;

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int colorPixel = imagenOriginal.getRGB(x, y);

                    int a = (colorPixel >> 24) & 0xff;
                    int r = (colorPixel >> 16) & 0xff;
                    int g = (colorPixel >> 8) & 0xff;
                    int b = colorPixel & 0xff;

                    int nivelDeGris = (r + g + b) / 3;

                    int grisCuantizado = cuantizarCanal(nivelDeGris, N);

                    int nuevoColor = (a << 24) | (grisCuantizado << 16) | (grisCuantizado << 8) | grisCuantizado;
                    imagenSalida.setRGB(x, y, nuevoColor);
                }
            }

            File archivoSalida = new File(rutaSalida);
            org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "png", archivoSalida);
            System.out.println("Generada gris retro con N=" + N + " -> " + rutaSalida);

        } catch (IOException e) {
            System.out.println("Error al procesar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int cuantizarCanal(int valorOriginal, int N) {
        if (N >= 256)
            return valorOriginal;
        double proporcion = valorOriginal / 255.0;
        int escalon = (int) Math.round(proporcion * (N - 1));
        return (int) Math.round((double) escalon / (N - 1) * 255.0);
    }

    public static void main(String[] args) {
        String rutaEntrada = "images/images.jpg";
        int[] valoresN = { 2, 4, 8, 64, 128, 255 };

        System.out.println("Iniciando generación de escalas de grises niveladas...");

        for (int n : valoresN) {
            String rutaSalida = "images/resultado_gris_retro_N" + n + ".png";
            aplicarEscalaDeGrisesRetro(rutaEntrada, rutaSalida, n);
        }

        System.out.println("\nImágenes generadas.");
    }
}
