package org.example.portafolio.ejercicios.ecualizador.ui;

import javax.swing.*;
import java.awt.*;

public class PanelHistograma extends JPanel {

    private int[][] histogramData;
    private boolean isGrayScale = false;

    private Color backgroundColor = new Color(35, 38, 41);
    private Color gridColor = new Color(50, 53, 55);
    private Color textColor = new Color(150, 153, 155);
    private Color axisColor = new Color(100, 103, 105);
    private Color titleColor = new Color(187, 187, 187);
    private Color borderColor = new Color(60, 63, 65);

    public PanelHistograma() {
        applyPanelStyles();
    }

    private void applyPanelStyles() {
        setBackground(backgroundColor);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                "Histograma de Intensidades",
                0, 0, null, titleColor));
    }

    public void setHistogram(int[][] histogramData, boolean isGrayScale) {
        this.histogramData = histogramData;
        this.isGrayScale = isGrayScale;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (histogramData == null) {
            g.setColor(textColor);
            g.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            String text = "Sin datos de histograma";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(text, x, y);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 30;
        int width = getWidth() - (2 * padding);
        int height = getHeight() - (2 * padding);

        int maxFrequency = 0;
        for (int c = 0; c < 3; c++) {
            if (isGrayScale && c > 0)
                continue;
            for (int i = 0; i < 256; i++) {
                if (histogramData[c][i] > maxFrequency) {
                    maxFrequency = histogramData[c][i];
                }
            }
        }
        if (maxFrequency == 0)
            maxFrequency = 1;

        g2d.setColor(gridColor);
        for (int i = 1; i <= 4; i++) {
            int gridY = padding + (height * i / 4);
            g2d.drawLine(padding, gridY, padding + width, gridY);
        }

        double stepX = (double) width / 256;

        if (isGrayScale) {
            g2d.setColor(new Color(128, 128, 128, 150));
            drawChannel(g2d, histogramData[0], maxFrequency, padding, height, stepX);
        } else {
            g2d.setColor(new Color(230, 75, 75, 120));
            drawChannel(g2d, histogramData[0], maxFrequency, padding, height, stepX);

            g2d.setColor(new Color(75, 200, 75, 120));
            drawChannel(g2d, histogramData[1], maxFrequency, padding, height, stepX);

            g2d.setColor(new Color(75, 150, 240, 120));
            drawChannel(g2d, histogramData[2], maxFrequency, padding, height, stepX);
        }

        g2d.setColor(axisColor);
        g2d.drawLine(padding, padding + height, padding + width, padding + height);
        g2d.drawLine(padding, padding, padding, padding + height);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2d.setColor(textColor);
        g2d.drawString("0", padding, padding + height + 15);
        g2d.drawString("128", padding + (width / 2) - 8, padding + height + 15);
        g2d.drawString("255", padding + width - 15, padding + height + 15);
    }

    private void drawChannel(Graphics2D g2d, int[] channelData, int maxFrequency, int padding, int height,
            double stepX) {
        Polygon poly = new Polygon();
        poly.addPoint(padding, padding + height);

        for (int i = 0; i < 256; i++) {
            int x = padding + (int) (i * stepX);
            int y = padding + height - (int) ((double) channelData[i] / maxFrequency * height);
            poly.addPoint(x, y);
        }

        poly.addPoint(padding + (int) (255 * stepX), padding + height);
        g2d.fillPolygon(poly);

        Color currentFill = g2d.getColor();
        g2d.setColor(new Color(currentFill.getRed(), currentFill.getGreen(), currentFill.getBlue(), 220));
        g2d.setStroke(new BasicStroke(1.5f));

        for (int i = 0; i < 255; i++) {
            int x1 = padding + (int) (i * stepX);
            int y1 = padding + height - (int) ((double) channelData[i] / maxFrequency * height);
            int x2 = padding + (int) ((i + 1) * stepX);
            int y2 = padding + height - (int) ((double) channelData[i + 1] / maxFrequency * height);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}