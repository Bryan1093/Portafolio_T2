package org.example.portafolio.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlaceholderImageGenerator {

    public static BufferedImage createDefaultImage(int width, int height, String label) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        GradientPaint gp;
        if (label.contains("B")) {
            gp = new GradientPaint(0, 0, new Color(241, 39, 17), width, height, new Color(245, 175, 25));
        } else {
            gp = new GradientPaint(0, 0, new Color(41, 128, 185), width, height, new Color(109, 213, 250));
        }
        g.setPaint(gp);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(255, 255, 255, 40));
        g.fillOval(width / 10, height / 10, width / 3, width / 3);
        g.fillOval(width - (width / 3), height - (height / 2), width / 4, width / 4);

        g.setColor(new Color(255, 255, 255, 60));
        int[] xPoints = { width / 2, width / 3, 2 * width / 3 };
        int[] yPoints = { height / 4, 3 * height / 4, 3 * height / 4 };
        g.fillPolygon(xPoints, yPoints, 3);

        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(20, height - 80, width - 40, 60, 15, 15);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        int textX = (width - fm.stringWidth(label)) / 2;
        int textY = height - 80 + ((60 - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(label, textX, textY);

        g.dispose();
        return img;
    }
}
