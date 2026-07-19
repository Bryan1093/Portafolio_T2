package org.example.portafolio.ejercicios.convoluciones;

import java.awt.image.BufferedImage;
import java.io.File;

public class separable {
    public static void main(String[] args) {
        File file = new File("images/manu.jpg");
        File fileOut = new File("images/resultado_separable.jpg");
        try {
            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);
            if (bufer == null) {
                System.out.println("No se pudo leer la imagen.");
                return;
            }

            // Aplicamos filtro separable (desenfoque de caja 1D horizontal y luego 1D vertical)
            BufferedImage resultado = aplicarFiltroSeparable(bufer);
            org.example.portafolio.utils.ImageIOManager.write(resultado, "jpg", fileOut);
            System.out.println("Filtro separable (Desenfoque de Caja 1D horizontal y vertical) guardado como: " + fileOut.getName());

        } catch (Exception e) {
            System.out.println("Error en el filtro separable: " + e.getMessage());
        }
    }

    public static BufferedImage aplicarFiltroSeparable(BufferedImage img) {
        int ancho = img.getWidth();
        int alto = img.getHeight();
        
        // Paso 1: Convolución horizontal 1D con kernel [1/3, 1/3, 1/3]
        BufferedImage temp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        float[] kernelH = {1f/3f, 1f/3f, 1f/3f};
        
        for (int y = 0; y < alto; y++) {
            for (int x = 1; x < ancho - 1; x++) {
                float sumR = 0, sumG = 0, sumB = 0;
                for (int j = -1; j <= 1; j++) {
                    int pixel = img.getRGB(x + j, y);
                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int b = (pixel) & 0xFF;
                    sumR += r * kernelH[j + 1];
                    sumG += g * kernelH[j + 1];
                    sumB += b * kernelH[j + 1];
                }
                int r = Math.min(255, Math.max(0, (int) sumR));
                int g = Math.min(255, Math.max(0, (int) sumG));
                int b = Math.min(255, Math.max(0, (int) sumB));
                temp.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }

        // Paso 2: Convolución vertical 1D con kernel [1/3, 1/3, 1/3]
        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        float[] kernelV = {1f/3f, 1f/3f, 1f/3f};
        
        for (int y = 1; y < alto - 1; y++) {
            for (int x = 0; x < ancho; x++) {
                float sumR = 0, sumG = 0, sumB = 0;
                for (int i = -1; i <= 1; i++) {
                    int pixel = temp.getRGB(x, y + i);
                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int b = (pixel) & 0xFF;
                    sumR += r * kernelV[i + 1];
                    sumG += g * kernelV[i + 1];
                    sumB += b * kernelV[i + 1];
                }
                int r = Math.min(255, Math.max(0, (int) sumR));
                int g = Math.min(255, Math.max(0, (int) sumG));
                int b = Math.min(255, Math.max(0, (int) sumB));
                salida.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return salida;
    }
}
