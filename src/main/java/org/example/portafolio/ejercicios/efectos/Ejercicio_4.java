package org.example.portafolio.ejercicios.efectos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ejercicio_4 {

    /**
     * Ejercicio 4: Efecto retro 2
     * Reduce colores a N variantes, pero ÚNICAMENTE para 2 canales (RG, RB o GB).
     * El tercer canal (el no seleccionado) se apaga completamente (se vuelve 0).
     * El Alpha se mantiene intacto.
     */
    public static void aplicarEfectoRetro2(String rutaEntrada, String rutaSalida, int N, String canalesAGuardar) {
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

            File archivoSalida = new File(rutaSalida);
            org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "png", archivoSalida);

        } catch (IOException e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int cuantizarCanal(int valorOriginal, int N) {
        if (N >= 256)
            return valorOriginal;

        double proporcion = valorOriginal / 255.0;
        int escalon = (int) Math.round(proporcion * (N - 1));
        return (int) Math.round((double) escalon / (N - 1) * 255.0);
    }

    public static void main(String[] args) {
        String rutaEntrada = "images/images.jpg";
        int[] valoresN = { 2, 4, 8, 64, 128, 255 };
        String[] combinaciones = { "RG", "GB", "RB" }; // Rojiverde, Verdeazul, RojiAzul

        System.out.println("Iniciando Efecto Retro 2 (Anulación de 1 canal)...");

        for (String combo : combinaciones) {
            System.out.println("\nProcesando combinación de canales: " + combo);
            for (int n : valoresN) {
                String rutaSalida = "images/resultado_retro2_" + combo + "_N" + n + ".png";
                aplicarEfectoRetro2(rutaEntrada, rutaSalida, n, combo);
                System.out.println("Guardada: " + rutaSalida);
            }
        }

        System.out.println("\n¡Proceso finalizado! Todas las imágenes fueron generadas.");
    }
}

/*
 * CONCLUSIÓN DEL EJERCICIO 4:
 * Al eliminar por completo un canal (convirtiéndolo a 0) perdemos drásticamente
 * una dimensión entera del espectro de colores. Por ejemplo, al usar solo 'RG'
 * perdemos los tonos azules y fríos, dejando la imagen teñida de amarillos,
 * verdes y rojos. A medida que N aumenta, las transiciones de luz en esos dos
 * canales
 * sobrevivientes se suavizan eliminando el 'banding' (igual que en el ejercicio
 * 3),
 * pero es IMPOSIBLE recuperar el color original realista: la imagen queda con
 * un
 * filtro artificial (como un lente tintado) producto del canal que fue
 * eliminado a 0.
 */
