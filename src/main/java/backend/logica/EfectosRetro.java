package backend.logica;

import java.awt.image.BufferedImage;

public class EfectosRetro {

    // Ejercicio 1: Vidrio Esmerilado
    public static BufferedImage aplicarVidrioEsmerilado(BufferedImage imagenOriginal) {
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
        return imagenSalida;
    }

    // Ejercicio 2: Desvanecimiento Circular
    public static BufferedImage aplicarDesvanecimientoCircular(BufferedImage imagenOriginal) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        double centroX = ancho / 2.0;
        double centroY = alto / 2.0;
        double distanciaMaxima = Math.sqrt(Math.pow(centroX, 2) + Math.pow(centroY, 2));

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int colorPixel = imagenOriginal.getRGB(x, y);
                int r = (colorPixel >> 16) & 0xff;
                int g = (colorPixel >> 8) & 0xff;
                int b = colorPixel & 0xff;

                double distanciaAlCentro = Math.sqrt(Math.pow(x - centroX, 2) + Math.pow(y - centroY, 2));
                double proporcion = 1.0 - (distanciaAlCentro / distanciaMaxima);
                int nuevoAlpha = (int) (255 * proporcion);
                nuevoAlpha = Math.max(0, Math.min(255, nuevoAlpha));

                int nuevoColor = (nuevoAlpha << 24) | (r << 16) | (g << 8) | b;
                imagenSalida.setRGB(x, y, nuevoColor);
            }
        }
        return imagenSalida;
    }

    // Método auxiliar para cuantizar colores (usado en Ej 3, 4 y 6)
    private static int cuantizarCanal(int valorOriginal, int N) {
        if (N >= 256)
            return valorOriginal;
        double proporcion = valorOriginal / 255.0;
        int escalon = (int) Math.round(proporcion * (N - 1));
        return (int) Math.round((double) escalon / (N - 1) * 255.0);
    }

    // Ejercicio 3: Efecto Retro 1 (Posterización)
    public static BufferedImage aplicarEfectoRetro(BufferedImage imagenOriginal, int N) {
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
        return imagenSalida;
    }

    // Ejercicio 4: Efecto Retro 2 (Anulación de canal)
    public static BufferedImage aplicarEfectoRetroAnulacion(BufferedImage imagenOriginal, int N,
            String canalesAGuardar) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        if (N < 2)
            N = 2;
        if (N > 256)
            N = 256;

        boolean mantenerR = canalesAGuardar.contains("R");
        boolean mantenerG = canalesAGuardar.contains("G");
        boolean mantenerB = canalesAGuardar.contains("B");

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int colorPixel = imagenOriginal.getRGB(x, y);
                int a = (colorPixel >> 24) & 0xff;
                int r = (colorPixel >> 16) & 0xff;
                int g = (colorPixel >> 8) & 0xff;
                int b = colorPixel & 0xff;

                r = mantenerR ? cuantizarCanal(r, N) : 0;
                g = mantenerG ? cuantizarCanal(g, N) : 0;
                b = mantenerB ? cuantizarCanal(b, N) : 0;

                int nuevoColor = (a << 24) | (r << 16) | (g << 8) | b;
                imagenSalida.setRGB(x, y, nuevoColor);
            }
        }
        return imagenSalida;
    }

    // Ejercicio 6: Grises Retro
    public static BufferedImage aplicarEscalaDeGrisesRetro(BufferedImage imagenOriginal, int N) {
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
        return imagenSalida;
    }

    public static BufferedImage aplicarDivididoEn3(BufferedImage imagenOriginal) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p = imagenOriginal.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int nuevoColor;
                if (x < ancho / 3) {
                    nuevoColor = (a << 24) | (r << 16) | (0 << 8) | 0;
                } else if (x < (ancho / 3) * 2) {
                    nuevoColor = (a << 24) | (0 << 16) | (g << 8) | 0;
                } else {
                    nuevoColor = (a << 24) | (0 << 16) | (0 << 8) | b;
                }
                imagenSalida.setRGB(x, y, nuevoColor);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarEspejo(BufferedImage imagenOriginal) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int a = (imagenOriginal.getRGB(x, y) >> 24) & 0xff;
                int color;
                if (x < ancho / 2) {
                    color = imagenOriginal.getRGB(x, y) & 0xFFFFFF;
                } else {
                    color = imagenOriginal.getRGB(ancho - 1 - x, y) & 0xFFFFFF;
                }
                imagenSalida.setRGB(x, y, (a << 24) | color);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarVerdeYGris(BufferedImage imagenOriginal) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p = imagenOriginal.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int nuevoColor;
                if (g > r && g > b) {
                    nuevoColor = (a << 24) | (0 << 16) | (255 << 8) | 0;
                } else {
                    int gris = (r + g + b) / 3;
                    nuevoColor = (a << 24) | (gris << 16) | (gris << 8) | gris;
                }
                imagenSalida.setRGB(x, y, nuevoColor);
            }
        }
        return imagenSalida;
    }
}
