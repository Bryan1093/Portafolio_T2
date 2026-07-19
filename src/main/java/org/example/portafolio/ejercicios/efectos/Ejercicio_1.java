package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ejercicio_1 {

    /**
     * Ejercicio 1: Vidrio esmerilado
     * Crea una imagen donde la transparencia depende del brillo del píxel.
     * Brillo alto -> Opaco (Alpha ~ 255)
     * Brillo bajo -> Casi transparente (Alpha ~ 50)
     */
    public static void aplicarVidrioEsmerilado(String rutaEntrada, String rutaSalida) {
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

                    int r = (colorPixel >> 16) & 0xff;
                    int g = (colorPixel >> 8) & 0xff;
                    int b = colorPixel & 0xff;

                    int brillo = (r + g + b) / 3;

                    int nuevoAlpha = 50 + (brillo * 205 / 255);

                    nuevoAlpha = Math.max(0, Math.min(255, nuevoAlpha));

                    int nuevoColor = (nuevoAlpha << 24) | (r << 16) | (g << 8) | b;

                    imagenSalida.setRGB(x, y, nuevoColor);
                }
            }

            File archivoSalida = new File(rutaSalida);
            org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "png", archivoSalida);
            System.out.println("¡Listo! Imagen de vidrio esmerilado guardada en: " + rutaSalida);

        } catch (IOException e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String rutaEntrada = "images/images.jpg";
        String rutaSalida = "images/resultado_esmerilado.png";

        System.out.println("Iniciando el proceso de vidrio esmerilado...");
        aplicarVidrioEsmerilado(rutaEntrada, rutaSalida);
    }

}
