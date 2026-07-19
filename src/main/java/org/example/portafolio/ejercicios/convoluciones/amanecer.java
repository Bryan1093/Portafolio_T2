package org.example.portafolio.ejercicios.convoluciones;

import java.awt.image.BufferedImage;
import java.io.File;

public class amanecer {

    public static void ej4(String rutaEntrada, String rutaSalidaBase) {
        try {
            File archivoEntrada = new File(rutaEntrada);
            BufferedImage imagenOriginal = org.example.portafolio.utils.ImageIOManager.read(archivoEntrada);

            if (imagenOriginal == null) {
                System.out.println("Error");
                return;
            }

            int ancho = imagenOriginal.getWidth();
            int alto = imagenOriginal.getHeight();

            for (int paso = 1; paso <= 10; paso++) {
                double factor = calcularFactorAmanecer(paso);
                BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
                double[][] kernel = {
                        {0.0, 0.0, 0.0},
                        {0.0, factor, 0.0},
                        {0.0, 0.0, 0.0}
                };

                for (int y = 0; y < alto; y++) {
                    for (int x = 0; x < ancho; x++) {
                        int nuevoColor = aplicarConvolucionRGB(imagenOriginal, x, y, kernel);
                        imagenSalida.setRGB(x, y, nuevoColor);
                    }
                }

                String rutaSalida = construirRutaSalida(rutaSalidaBase, paso);
                File archivoSalida = new File(rutaSalida);
                org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "png", archivoSalida);
                System.out.println("Paso " + paso + " (Factor: " + String.format("%.2f", factor) + ") guardado.");
            }

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    private static int aplicarConvolucionRGB(BufferedImage imagen, int x, int y, double[][] kernel) {
        double sumaR = 0.0;
        double sumaG = 0.0;
        double sumaB = 0.0;

        for (int ky = -1; ky <= 1; ky++) {
            for (int kx = -1; kx <= 1; kx++) {
                int muestraX = x + kx;
                int muestraY = y + ky;

                if (muestraX < 0) {
                    muestraX = 0;
                } else if (muestraX >= imagen.getWidth()) {
                    muestraX = imagen.getWidth() - 1;
                }

                if (muestraY < 0) {
                    muestraY = 0;
                } else if (muestraY >= imagen.getHeight()) {
                    muestraY = imagen.getHeight() - 1;
                }

                int colorPixel = imagen.getRGB(muestraX, muestraY);
                double peso = kernel[ky + 1][kx + 1];

                sumaR += ((colorPixel >> 16) & 0xff) * peso;
                sumaG += ((colorPixel >> 8) & 0xff) * peso;
                sumaB += (colorPixel & 0xff) * peso;
            }
        }

        int nuevoR = (int) Math.round(sumaR);
        int nuevoG = (int) Math.round(sumaG);
        int nuevoB = (int) Math.round(sumaB);

        if (nuevoR < 0) {
            nuevoR = 0;
        } else if (nuevoR > 255) {
            nuevoR = 255;
        }

        if (nuevoG < 0) {
            nuevoG = 0;
        } else if (nuevoG > 255) {
            nuevoG = 255;
        }

        if (nuevoB < 0) {
            nuevoB = 0;
        } else if (nuevoB > 255) {
            nuevoB = 255;
        }

        return (nuevoR << 16) | (nuevoG << 8) | nuevoB;
    }

    private static double calcularFactorAmanecer(int paso) {
        return 0.10 + ((paso - 1) * 0.90 / 9.0);
    }

    private static String construirRutaSalida(String rutaSalidaBase, int paso) {
        return rutaSalidaBase + String.format("_%02d.png", paso);
    }

    public static void main(String[] args) {
        String rutaEntrada = "images/case.jpg";
        String rutaSalidaBase = "images/case_amanecer";
        ej4(rutaEntrada, rutaSalidaBase);
    }
}
