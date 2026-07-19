package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EvaluacionSumativa {

    public static void main(String[] args) throws IOException {
        BufferedImage img = org.example.portafolio.utils.ImageIOManager.read(new File("images/manchester.jpg"));
        BufferedImage resultado = aplicarFiltroTaller(img);
        org.example.portafolio.utils.ImageIOManager.write(resultado, "png", new File("images/salida.png"));
        System.out.println("Imagen guardada.");
    }

    public static BufferedImage aplicarFiltroTaller(BufferedImage imagenOriginal) {

        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();

        float[][] kernel = {
                { 0f, 0f, 0f, 0f, 0f },
                { 0f, 0f, -1f, 0f, 0f },
                { 0f, -1f, 5f, -1f, 0f },
                { 0f, 0f, -1f, 0f, 0f },
                { 0f, 0f, 0f, 0f, 0f }
        };

        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {

                if (y < 2 || y >= alto - 2 || x < 2 || x >= ancho - 2) {
                    int p = imagenOriginal.getRGB(x, y);
                    int g = (p >> 8) & 0xFF;
                    imagenSalida.setRGB(x, y, (255 << 24) | (g << 8));
                    continue;
                }

                float sumaVerde = 0f;

                for (int ky = -2; ky <= 2; ky++) {
                    for (int kx = -2; kx <= 2; kx++) {
                        int pixelVecino = imagenOriginal.getRGB(x + kx, y + ky);
                        int gVecino = (pixelVecino >> 8) & 0xFF;
                        sumaVerde += gVecino * kernel[ky + 2][kx + 2];
                    }
                }

                int gFinal = clamp((int) sumaVerde);

                int nuevoPixel = (255 << 24) | (0 << 16) | (gFinal << 8) | 0;
                imagenSalida.setRGB(x, y, nuevoPixel);
            }
        }

        return imagenSalida;
    }

    private static int clamp(int v) {
        return Math.min(255, Math.max(0, v));
    }
}