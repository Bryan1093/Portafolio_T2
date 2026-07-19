package org.example.portafolio.ejercicios.histograma;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Histograma {

    public static void main(String[] args) {

        File file = new File("images/galaxia.jpg");
        File file2 = new File("images/HistoRojo.jpg");
        File file3 = new File("images/HistoVerde.jpg");
        File file4 = new File("images/HistoAzul.jpg");
        File file5 = new File("images/HistoCombinado.jpg");

        int ancho, alto, pixel;
        int r, g, b;

        try {

            BufferedImage bufer = org.example.portafolio.utils.ImageIOManager.read(file);

            if (bufer == null) {
                System.out.println("No se pudo cargar la imagen.");
                return;
            }

            ancho = bufer.getWidth();
            alto = bufer.getHeight();

            int anchoHisto = 800;
            int altoHisto = 600;

            int[] histoR = new int[256];
            int[] histoG = new int[256];
            int[] histoB = new int[256];

            BufferedImage buferRojo = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);
            BufferedImage buferVerde = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);
            BufferedImage buferAzul = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);
            BufferedImage buferCombinado = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);

            Graphics2D grR = buferRojo.createGraphics();
            Graphics2D grG = buferVerde.createGraphics();
            Graphics2D grB = buferAzul.createGraphics();
            Graphics2D grC = buferCombinado.createGraphics();

            grR.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            grG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            grB.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            grC.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            grR.setColor(Color.WHITE);
            grR.fillRect(0, 0, anchoHisto, altoHisto);
            grG.setColor(Color.WHITE);
            grG.fillRect(0, 0, anchoHisto, altoHisto);
            grB.setColor(Color.WHITE);
            grB.fillRect(0, 0, anchoHisto, altoHisto);
            grC.setColor(Color.WHITE);
            grC.fillRect(0, 0, anchoHisto, altoHisto);

            for (int i = 0; i < alto; i++) {

                for (int j = 0; j < ancho; j++) {

                    pixel = bufer.getRGB(j, i);

                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = pixel & 0xFF;

                    histoR[r]++;
                    histoG[g]++;
                    histoB[b]++;
                }
            }

            imprimir(histoR);
            imprimir(histoG);
            imprimir(histoB);

            int max = maximo(histoR);
            int max2 = maximo(histoG);
            int max3 = maximo(histoB);

            int margenTop = 40;
            int margenLateral = 20;
            int margenBottom = 5;

            float escalaX = (float) (anchoHisto - (2 * margenLateral)) / 255;
            float escalaY = (float) (altoHisto - margenTop - margenBottom) / max;
            float escalaY2 = (float) (altoHisto - margenTop - margenBottom) / max2;
            float escalaY3 = (float) (altoHisto - margenTop - margenBottom) / max3;

            Polygon polyR = new Polygon();
            polyR.addPoint(margenLateral, altoHisto - margenBottom);
            for (int i = 0; i < histoR.length; i++) {
                int x = margenLateral + (int) (escalaX * i);
                int y = altoHisto - margenBottom - (int) (escalaY * histoR[i]);
                polyR.addPoint(x, y);
            }
            polyR.addPoint(margenLateral + (int) (escalaX * 255), altoHisto - margenBottom);

            grR.setColor(new Color(255, 0, 0, 80));
            grR.fillPolygon(polyR);
            grC.setColor(new Color(255, 0, 0, 80));
            grC.fillPolygon(polyR);

            grR.setColor(Color.RED);
            grC.setColor(Color.RED);
            for (int i = 1; i < histoR.length; i++) {
                int x1 = margenLateral + (int) (escalaX * (i - 1));
                int y1 = altoHisto - margenBottom - (int) (escalaY * histoR[i - 1]);
                int x2 = margenLateral + (int) (escalaX * i);
                int y2 = altoHisto - margenBottom - (int) (escalaY * histoR[i]);
                grR.drawLine(x1, y1, x2, y2);
                grC.drawLine(x1, y1, x2, y2);
            }

            Polygon polyG = new Polygon();
            polyG.addPoint(margenLateral, altoHisto - margenBottom);
            for (int i = 0; i < histoG.length; i++) {
                int x = margenLateral + (int) (escalaX * i);
                int y = altoHisto - margenBottom - (int) (escalaY2 * histoG[i]);
                polyG.addPoint(x, y);
            }
            polyG.addPoint(margenLateral + (int) (escalaX * 255), altoHisto - margenBottom);

            grG.setColor(new Color(0, 255, 0, 80));
            grG.fillPolygon(polyG);
            grC.setColor(new Color(0, 255, 0, 80));
            grC.fillPolygon(polyG);

            grG.setColor(Color.GREEN);
            grC.setColor(Color.GREEN);
            for (int i = 1; i < histoG.length; i++) {
                int x1 = margenLateral + (int) (escalaX * (i - 1));
                int y1 = altoHisto - margenBottom - (int) (escalaY2 * histoG[i - 1]);
                int x2 = margenLateral + (int) (escalaX * i);
                int y2 = altoHisto - margenBottom - (int) (escalaY2 * histoG[i]);
                grG.drawLine(x1, y1, x2, y2);
                grC.drawLine(x1, y1, x2, y2);
            }

            Polygon polyB = new Polygon();
            polyB.addPoint(margenLateral, altoHisto - margenBottom);
            for (int i = 0; i < histoB.length; i++) {
                int x = margenLateral + (int) (escalaX * i);
                int y = altoHisto - margenBottom - (int) (escalaY3 * histoB[i]);
                polyB.addPoint(x, y);
            }
            polyB.addPoint(margenLateral + (int) (escalaX * 255), altoHisto - margenBottom);

            grB.setColor(new Color(0, 0, 255, 80));
            grB.fillPolygon(polyB);
            grC.setColor(new Color(0, 0, 255, 80));
            grC.fillPolygon(polyB);

            grB.setColor(Color.BLUE);
            grC.setColor(Color.BLUE);
            for (int i = 1; i < histoB.length; i++) {
                int x1 = margenLateral + (int) (escalaX * (i - 1));
                int y1 = altoHisto - margenBottom - (int) (escalaY3 * histoB[i - 1]);
                int x2 = margenLateral + (int) (escalaX * i);
                int y2 = altoHisto - margenBottom - (int) (escalaY3 * histoB[i]);
                grB.drawLine(x1, y1, x2, y2);
                grC.drawLine(x1, y1, x2, y2);
            }

            org.example.portafolio.utils.ImageIOManager.write(buferRojo, "jpg", file2);
            org.example.portafolio.utils.ImageIOManager.write(buferVerde, "jpg", file3);
            org.example.portafolio.utils.ImageIOManager.write(buferAzul, "jpg", file4);
            org.example.portafolio.utils.ImageIOManager.write(buferCombinado, "jpg", file5);

            System.out.println("Histograma creado correctamente");

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void imprimir(int[] h) {

        for (int i = 0; i < h.length; i++) {

            System.out.println(i + ": " + h[i]);
        }
    }

    private static int maximo(int[] h) {

        int aux = h[0];
        int indice = 0;

        for (int i = 0; i < h.length; i++) {

            if (h[i] > aux) {

                aux = h[i];
                indice = i;
            }
        }

        System.out.println("indice = " + indice +
                ", valor = " + aux);

        return aux;
    }
}