package backend.logica;

import java.awt.image.BufferedImage;

public class FiltrosBasicos {

    public static BufferedImage aplicarBlancoYNegro(BufferedImage imagenOriginal) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int colorPixel = imagenOriginal.getRGB(x, y);
                int a = (colorPixel >> 24) & 0xff;
                int r = (colorPixel >> 16) & 0xff;
                int g = (colorPixel >> 8) & 0xff;
                int b = colorPixel & 0xff;

                int brillo = (r + g + b) / 3;
                int valorFinal = (brillo > 127) ? 255 : 0;

                int nuevoColor = (a << 24) | (valorFinal << 16) | (valorFinal << 8) | valorFinal;
                imagenSalida.setRGB(x, y, nuevoColor);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarTransparencia(BufferedImage imagenOriginal, float factorT) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = imagenOriginal.getRGB(x, y);

                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                a = (int) Math.max(0, Math.min(255, a * factorT));

                int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
                imagenSalida.setRGB(x, y, pixelNuevo);
            }
        }
        return imagenSalida;
    }

    // --- NUEVOS FILTROS BÁSICOS MIGRADOS DE Filtros.java y ejemploClase.java ---

    public static BufferedImage aplicarTinteOcre(BufferedImage imagenOriginal) {
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
                
                // Calculamos la luminosidad del pixel original (Luma) para no perder los detalles
                float luma = (0.299f * r + 0.587f * g + 0.114f * b) / 255.0f;
                
                // Teñimos la luminosidad usando el color Ocre del profe (209, 158, 37)
                int nuevoR = Math.min(255, (int)(209 * luma));
                int nuevoG = Math.min(255, (int)(158 * luma));
                int nuevoB = Math.min(255, (int)(37 * luma));
                
                // Mantenemos el Alpha al 80%
                int nuevoA = (int)(0.8f * a);

                int nuevoColor = (nuevoA << 24) | (nuevoR << 16) | (nuevoG << 8) | nuevoB;
                imagenSalida.setRGB(x, y, nuevoColor);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarNegativo(BufferedImage imagenOriginal) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p = imagenOriginal.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = 255 - ((p >> 16) & 0xff);
                int g = 255 - ((p >> 8) & 0xff);
                int b = 255 - (p & 0xff);
                imagenSalida.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarBrillo(BufferedImage imagenOriginal, int offset) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p = imagenOriginal.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = Math.min(255, Math.max(0, ((p >> 16) & 0xff) + offset));
                int g = Math.min(255, Math.max(0, ((p >> 8) & 0xff) + offset));
                int b = Math.min(255, Math.max(0, (p & 0xff) + offset));
                imagenSalida.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarEscalaGrises(BufferedImage imagenOriginal) {
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
                int prom = (r + g + b) / 3;
                imagenSalida.setRGB(x, y, (a << 24) | (prom << 16) | (prom << 8) | prom);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarSaturacion(BufferedImage imagenOriginal, float factorS) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        float[] hsb = new float[3];
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p = imagenOriginal.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                java.awt.Color.RGBtoHSB(r, g, b, hsb);

                // Multiplicamos la saturación
                hsb[1] = Math.min(1.0f, Math.max(0.0f, hsb[1] * factorS));

                int rgb = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                imagenSalida.setRGB(x, y, (a << 24) | (rgb & 0xFFFFFF));
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarRecorteBits(BufferedImage imagenOriginal, int bitsGuardados) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        
        if (bitsGuardados < 1) bitsGuardados = 1;
        if (bitsGuardados > 8) bitsGuardados = 8;
        
        int shift = 8 - bitsGuardados; 
        int maxValue = (1 << bitsGuardados) - 1; 

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p = imagenOriginal.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                
                // Recorte
                r = (r >> shift);
                g = (g >> shift);
                b = (b >> shift);

                // Estiramiento
                r = (r * 255) / maxValue;
                g = (g * 255) / maxValue;
                b = (b * 255) / maxValue;

                imagenSalida.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return imagenSalida;
    }

    public static BufferedImage aplicarSoloRecorteBits(BufferedImage imagenOriginal, int bitsGuardados) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        if (bitsGuardados < 1) bitsGuardados = 1;
        if (bitsGuardados > 8) bitsGuardados = 8;

        int shift = 8 - bitsGuardados;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p = imagenOriginal.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // Solo recorte, SIN estirar
                r = (r >> shift);
                g = (g >> shift);
                b = (b >> shift);

                imagenSalida.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return imagenSalida;
    }
}
