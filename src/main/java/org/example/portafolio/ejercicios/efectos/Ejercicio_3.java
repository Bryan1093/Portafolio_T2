package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ejercicio_3 {

    /**
     * Ejercicio 3: Efecto retro 1
     * Reduce los colores de la imagen a N posibles valores por cada canal (R, G,
     * B),
     * manteniendo el canal Alpha intacto.
     * N es la cantidad de variaciones de un color (ej: N=2 significa solo 0 o 255).
     */
    public static void aplicarEfectoRetro(String rutaEntrada, String rutaSalida, int N) {
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

                    r = cuantizarCanal(r, N);
                    g = cuantizarCanal(g, N);
                    b = cuantizarCanal(b, N);

                    int nuevoColor = (a << 24) | (r << 16) | (g << 8) | b;
                    imagenSalida.setRGB(x, y, nuevoColor);
                }
            }

            File archivoSalida = new File(rutaSalida);
            org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "png", archivoSalida);
            System.out.println("¡Listo! Imagen con N=" + N + " colores guardada en: " + rutaSalida);

        } catch (IOException e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int cuantizarCanal(int valorOriginal, int N) {
        if (N >= 256)
            return valorOriginal;

        double proporcion = valorOriginal / 255.0;
        int escalon = (int) Math.round(proporcion * (N - 1));

        int valorPixelReducido = (int) Math.round((double) escalon / (N - 1) * 255.0);
        return valorPixelReducido;
    }

    public static void main(String[] args) {
        String rutaEntrada = "images/images.jpg";

        int[] valoresN = { 2, 4, 8, 64, 128, 255 };

        System.out.println("Iniciando el proceso de efecto retro múltiple...");
        for (int n : valoresN) {
            String rutaSalida = "images/resultado_retro_N" + n + ".png";
            aplicarEfectoRetro(rutaEntrada, rutaSalida, n);
        }

        System.out.println("Proceso finalizado. ¡Revisá tus imágenes!");
    }
}

/*
 * CONCLUSIÓN DEL EJERCICIO:
 * A medida que el valor N (la profundidad de color) aumenta, la imagen suaviza
 * las
 * transiciones de luz logrando que el ojo humano no detecte los escalones de
 * color.
 * Queda demostrado así que la pérdida de calidad visual (posterización) sucede
 * porque
 * eliminás la información de colores intermedios. Curiosamente, la de N=64 te
 * demuestra
 * que no siempre necesitamos los 16.7 millones de colores para engañar al ojo
 * haciéndole creer que está viendo una foto realista, ¡con una fracción de los
 * colores
 * el cerebro se la cree igual!
 */
