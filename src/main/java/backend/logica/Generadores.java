package backend.logica;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Generadores {

    public static BufferedImage generarGradiente(int ancho, int alto, String tipo) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int r = 0, g = 0, b = 0;
                double porc = 0;

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
        return imagen;
    }

    public static BufferedImage generarRuido(int ancho, int alto) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Random random = new Random();

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                int pixelNuevo = (r << 16) | (g << 8) | b;
                imagen.setRGB(x, y, pixelNuevo);
            }
        }
        return imagen;
    }
}
