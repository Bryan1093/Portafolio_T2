package org.example.portafolio.ejercicios.generadores;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.Scanner;

public class Gradientes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int ancho = 900;
        int alto = 500;

        System.out.println("1. Izquierda a Derecha");
        System.out.println("2. Derecha a Izquierda (Rojo a Azul)");
        System.out.println("3. Arriba a Abajo");
        System.out.println("4. Abajo a Arriba");
        System.out.println("5. Radial (Centro a Esquinas)");
        System.out.println("6. GENERAR TODOS");
        System.out.println("0. Salir");
        System.out.print("\nElegir una opcion: ");

        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                generarGradiente("images/1_izq_der.jpg", ancho, alto, "HORIZONTAL_NORMAL");
                break;
            case 2:
                generarGradiente("images/2_der_izq_rojo_azul.jpg", ancho, alto, "HORIZONTAL_INVERSO_ROJO_AZUL");
                break;
            case 3:
                generarGradiente("images/3_arr_aba.jpg", ancho, alto, "VERTICAL_NORMAL");
                break;
            case 4:
                generarGradiente("images/4_aba_arr.jpg", ancho, alto, "VERTICAL_INVERSO");
                break;
            case 5:
                generarGradiente("images/5_radial.jpg", ancho, alto, "RADIAL");
                break;
            case 6:
                generarGradiente("images/1_izq_der.jpg", ancho, alto, "HORIZONTAL_NORMAL");
                generarGradiente("images/2_der_izq_rojo_azul.jpg", ancho, alto, "HORIZONTAL_INVERSO_ROJO_AZUL");
                generarGradiente("images/3_arr_aba.jpg", ancho, alto, "VERTICAL_NORMAL");
                generarGradiente("images/4_aba_arr.jpg", ancho, alto, "VERTICAL_INVERSO");
                generarGradiente("images/5_radial.jpg", ancho, alto, "RADIAL");
                break;
            case 0:
                System.out.println("¡Adiós!");
                break;
            default:
                System.out.println("Opcion no valida.");
        }

        scanner.close();
    }

    public static void generarGradiente(String nombreArchivo, int ancho, int alto, String tipo) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int r = 0, g = 0, b = 0;
                double porc = 0; // Porcentaje de 0.0 a 1.0

                switch (tipo) {
                    case "HORIZONTAL_NORMAL":
                        porc = (double) x / ancho;
                        r = (int) (255 * porc);
                        g = (int) (255 * porc);
                        b = (int) (255 * porc);
                        break;

                    case "HORIZONTAL_INVERSO_ROJO_AZUL":
                        porc = (double) x / ancho;
                        r = (int) (255 * porc);
                        g = 0;
                        b = (int) (255 * (1 - porc));
                        break;

                    case "VERTICAL_NORMAL":
                        porc = (double) y / alto;
                        r = (int) (100 * porc);
                        g = (int) (200 * porc);
                        b = (int) (255 * porc);
                        break;

                    case "VERTICAL_INVERSO":
                        porc = 1.0 - ((double) y / alto);
                        r = (int) (255 * porc);
                        g = (int) (150 * porc);
                        b = 0;
                        break;

                    case "RADIAL":
                        int cx = ancho / 2;
                        int cy = alto / 2;
                        double dist = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
                        double maxDist = Math.sqrt(Math.pow(cx, 2) + Math.pow(cy, 2));
                        porc = dist / maxDist;

                        r = (int) (255 * (1 - porc));
                        g = (int) (255 * (1 - porc));
                        b = (int) (255 * (1 - porc));
                        break;
                }

                int pixel = (r << 16) | (g << 8) | b;
                imagen.setRGB(x, y, pixel);
            }
        }

        try {
            File output = new File(nombreArchivo);
            output.getParentFile().mkdirs();
            org.example.portafolio.utils.ImageIOManager.write(imagen, "jpg", output);
            System.out.println("Generada: " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar " + nombreArchivo);
        }
    }
}
