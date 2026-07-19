package org.example.portafolio.ejercicios.convoluciones;

import java.awt.image.BufferedImage;
import java.io.File;

public class convolucion {

    private static String selectedKernel = "sobel";

    public static void main(String[] args) {
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            selectedKernel = args[0].toLowerCase();
        }
        
        File file = new File("images/manu.jpg");
        File file2 = new File("images/nuevo_3x3.jpg");
        File file3 = new File("images/nuevo_9x9.jpg");

        try {
            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);
            if (bufer == null) {
                System.out.println("No se pudo leer la imagen. Revisa la ruta.");
                return;
            }

            // Matriz de 3x3 seleccionada aplicada 9 veces
            BufferedImage resultado3x3 = bufer;
            for (int i = 0; i < 9; i++) {
                resultado3x3 = aplicarConvolucion(resultado3x3);
            }
            org.example.portafolio.utils.ImageIOManager.write(resultado3x3, "jpg", file2);
            System.out.println("Imagen 3x3 iterada (" + selectedKernel.toUpperCase() + ") guardada como: " + file2.getName());

            // Matriz de 9x9 dinámica aplicada 1 sola vez
            BufferedImage resultado9x9 = aplicarConvolucion9x9(bufer);
            org.example.portafolio.utils.ImageIOManager.write(resultado9x9, "jpg", file3);
            System.out.println("Imagen 9x9 dinámica (" + selectedKernel.toUpperCase() + ") guardada como: " + file3.getName());

        } catch (Exception e) {
            System.out.println("Error en el proceso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static BufferedImage aplicarConvolucion(BufferedImage img) {
        int ancho = img.getWidth();
        int alto = img.getHeight();

        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        float[] matrizConv;
        switch (selectedKernel) {
            case "box blur":
            case "blur":
                matrizConv = new float[]{
                    1f / 9, 1f / 9, 1f / 9,
                    1f / 9, 1f / 9, 1f / 9,
                    1f / 9, 1f / 9, 1f / 9
                };
                break;
            case "identidad":
            case "identity":
                matrizConv = new float[]{
                    0, 0, 0,
                    0, 1f, 0,
                    0, 0, 0
                };
                break;
            case "detección de bordes":
            case "deteccion de bordes":
            case "edgedetect":
                matrizConv = new float[]{
                    -1, -1, -1,
                    -1, 8, -1,
                    -1, -1, -1
                };
                break;
            case "sharpen":
                matrizConv = new float[]{
                    0, -1, 0,
                    -1, 5, -1,
                    0, -1, 0
                };
                break;
            case "gaussiano":
            case "gaussian":
                matrizConv = new float[]{
                    1f / 16, 2f / 16, 1f / 16,
                    2f / 16, 4f / 16, 2f / 16,
                    1f / 16, 2f / 16, 1f / 16
                };
                break;
            case "sobel":
            default:
                matrizConv = new float[]{
                    1, 0, -1,
                    0, 0, 0,
                    -1, 0, 1
                };
                break;
        }

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {

                float sumaR = 0, sumaG = 0, sumaB = 0;
                int indice = 0;

                // Recorremos la máscara de 3x3
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = img.getRGB(x + j, y + i);

                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = (pixel) & 0xFF;

                        sumaR += r * matrizConv[indice];
                        sumaG += g * matrizConv[indice];
                        sumaB += b * matrizConv[indice];

                        indice++;
                    }
                }

                int red = Math.min(255, Math.max(0, (int) sumaR));
                int green = Math.min(255, Math.max(0, (int) sumaG));
                int blue = Math.min(255, Math.max(0, (int) sumaB));

                int pixelNuevo = (red << 16) | (green << 8) | blue;
                salida.setRGB(x, y, pixelNuevo);
            }
        }
        return salida;
    }

    public static BufferedImage aplicarConvolucion9x9(BufferedImage img) {
        int ancho = img.getWidth();
        int alto = img.getHeight();

        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Matriz dinámica de 9x9 con el kernel seleccionado
        int n = 9;
        int radio = n / 2;

        float[] matrizConv = new float[n * n];
        switch (selectedKernel) {
            case "box blur":
            case "blur":
                for (int i = 0; i < 81; i++) {
                    matrizConv[i] = 1f / 81f;
                }
                break;
            case "identidad":
            case "identity":
                matrizConv[40] = 1f; // Center is 1.0f
                break;
            case "detección de bordes":
            case "deteccion de bordes":
            case "edgedetect":
                for (int i = 0; i < 81; i++) {
                    matrizConv[i] = -1f;
                }
                matrizConv[40] = 80f; // Center is 80.0f
                break;
            case "sharpen":
                for (int i = 0; i < 81; i++) {
                    matrizConv[i] = -1f;
                }
                matrizConv[40] = 81f; // Center is 81.0f
                break;
            case "gaussiano":
            case "gaussian":
                float sigma = 2.0f;
                float sum = 0;
                for (int yVal = -4; yVal <= 4; yVal++) {
                    for (int xVal = -4; xVal <= 4; xVal++) {
                        float val = (float) Math.exp(-(xVal*xVal + yVal*yVal) / (2 * sigma * sigma));
                        matrizConv[(yVal+4)*9 + (xVal+4)] = val;
                        sum += val;
                    }
                }
                for (int i = 0; i < 81; i++) {
                    matrizConv[i] /= sum;
                }
                break;
            case "sobel":
            default:
                for (int yVal = -4; yVal <= 4; yVal++) {
                    for (int xVal = -4; xVal <= 4; xVal++) {
                        int idx = (yVal + 4) * 9 + (xVal + 4);
                        if (yVal < 0 && xVal < 0) matrizConv[idx] = 1f;
                        else if (yVal > 0 && xVal > 0) matrizConv[idx] = 1f;
                        else if (yVal < 0 && xVal > 0) matrizConv[idx] = -1f;
                        else if (yVal > 0 && xVal < 0) matrizConv[idx] = -1f;
                        else matrizConv[idx] = 0f;
                    }
                }
                break;
        }

        for (int y = radio; y < alto - radio; y++) {
            for (int x = radio; x < ancho - radio; x++) {

                float sumaR = 0, sumaG = 0, sumaB = 0;
                int indice = 0;

                for (int i = -radio; i <= radio; i++) {
                    for (int j = -radio; j <= radio; j++) {

                        int pixel = img.getRGB(x + j, y + i);

                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = (pixel) & 0xFF;

                        sumaR += r * matrizConv[indice];
                        sumaG += g * matrizConv[indice];
                        sumaB += b * matrizConv[indice];

                        indice++;
                    }
                }

                int red = Math.min(255, Math.max(0, (int) sumaR));
                int green = Math.min(255, Math.max(0, (int) sumaG));
                int blue = Math.min(255, Math.max(0, (int) sumaB));

                int pixelNuevo = (red << 16) | (green << 8) | blue;
                salida.setRGB(x, y, pixelNuevo);
            }
        }
        return salida;
    }
}