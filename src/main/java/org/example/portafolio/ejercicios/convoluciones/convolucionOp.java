package org.example.portafolio.ejercicios.convoluciones;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;

public class convolucionOp {

    public static void main(String[] args) {
        String filterName = "blur";
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            filterName = args[0].toLowerCase();
        }

        try {
            File archivoEntrada = new File("images/manu.jpg");
            BufferedImage imagenOriginal = org.example.portafolio.utils.ImageIOManager.read(archivoEntrada);

            if (imagenOriginal == null) {
                System.out.println("No se encontró la imagen.");
                return;
            }

            float[] matriz;
            switch (filterName) {
                case "identidad":
                case "identity":
                case "normal":
                    matriz = kernels.KNormal;
                    break;
                case "sharpen":
                case "sharkpen":
                    matriz = kernels.KSharkpen;
                    break;
                case "detección de bordes":
                case "deteccion de bordes":
                case "edge":
                case "bordes":
                    matriz = kernels.KEdge;
                    break;
                case "aclarar":
                case "lighten":
                    matriz = kernels.KLighten;
                    break;
                case "oscurecer":
                case "darken":
                    matriz = kernels.KDarken;
                    break;
                case "box blur":
                case "blur":
                default:
                    matriz = kernels.KBlur;
                    break;
            }

            Kernel kernel = new Kernel(3, 3, matriz);
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage imagenFiltrada = op.filter(imagenOriginal, null);

            File archivoSalida = new File("images/resultado_op.jpg");
            org.example.portafolio.utils.ImageIOManager.write(imagenFiltrada, "jpg", archivoSalida);

            System.out.println("¡Filtro ConvolveOp (" + filterName.toUpperCase() + ") aplicado con éxito!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
