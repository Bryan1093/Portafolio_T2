package org.example.portafolio.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

public class ImageIOManager {
    private static BufferedImage inputImage;
    private static BufferedImage inputImage2;
    private static BufferedImage inputImage3;
    private static final List<OutputEntry> outputImages = new ArrayList<>();
    private static boolean interceptEnabled = true;
    private static int readCounter = 0;

    public static class OutputEntry {
        public String name;
        public BufferedImage image;

        public OutputEntry(String name, BufferedImage image) {
            this.name = name;
            this.image = image;
        }
    }

    public static void setInputImages(BufferedImage img1, BufferedImage img2, BufferedImage img3) {
        inputImage = img1;
        inputImage2 = img2;
        inputImage3 = img3;
    }

    public static void clearOutputs() {
        outputImages.clear();
        readCounter = 0;
    }

    public static List<OutputEntry> getOutputImages() {
        return outputImages;
    }

    public static void setInterceptEnabled(boolean enabled) {
        interceptEnabled = enabled;
    }

    public static boolean isInterceptEnabled() {
        return interceptEnabled;
    }

    public static BufferedImage read(File file) throws IOException {
        if (interceptEnabled && inputImage != null) {
            readCounter++;
            if (readCounter == 1) {
                return inputImage;
            } else if (readCounter == 2) {
                return inputImage2 != null ? inputImage2 : inputImage;
            } else if (readCounter == 3) {
                return inputImage3 != null ? inputImage3 : inputImage;
            } else {
                return inputImage;
            }
        }
        
        // Fallback: If running the class directly and the file doesn't exist on disk, autogenerate a test image in memory
        if (!file.exists()) {
            System.out.println("[ImageIOManager] La imagen física '" + file.getPath() + "' no existe en disco.");
            System.out.println("[ImageIOManager] Autogenerando imagen de prueba dinámica en memoria...");
            return org.example.portafolio.utils.PlaceholderImageGenerator.createDefaultImage(500, 500, file.getName());
        }
        
        return ImageIO.read(file);
    }

    public static boolean write(BufferedImage im, String formatName, File output) throws IOException {
        if (interceptEnabled && inputImage != null) {
            outputImages.add(new OutputEntry(output.getName(), im));
            return true;
        }
        
        // Fallback: If running directly (not via GUI), write the result to the disk
        System.out.println("[ImageIOManager] Guardando resultado físico en disco: " + output.getPath());
        // Ensure parent directories exist
        File parent = output.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        return ImageIO.write(im, formatName, output);
    }
}
