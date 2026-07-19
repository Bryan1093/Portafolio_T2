package backend.logica;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Graphics2D;

public class FiltrosEspaciales {

    private static BufferedImage estandarizarImagen(BufferedImage original) {
        if (original.getType() == BufferedImage.TYPE_INT_ARGB || original.getType() == BufferedImage.TYPE_INT_RGB) {
            return original;
        }
        BufferedImage convertida = new BufferedImage(original.getWidth(), original.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = convertida.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return convertida;
    }

    public static BufferedImage aplicarBoxBlur(BufferedImage imgOriginal, int N) {
        if (N % 2 == 0)
            N++;
        if (N < 3)
            N = 3;

        float[] matrizConv = new float[N * N];
        float peso = 1.0f / (N * N);
        for (int i = 0; i < matrizConv.length; i++) {
            matrizConv[i] = peso;
        }

        return aplicarKernelRGB(imgOriginal, matrizConv);
    }

    public static BufferedImage aplicarSobel(BufferedImage imgOriginal) {
        BufferedImage img = estandarizarImagen(imgOriginal);
        int ancho = img.getWidth();
        int alto = img.getHeight();
        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        float[] matrizConv = {
                1, 0, -1,
                0, 0, 0,
                -1, 0, 1
        };

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {

                float sumaR = 0, sumaG = 0, sumaB = 0;
                int indice = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = img.getRGB(x + j, y + i);
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = (pixel) & 0xFF;

                        sumaR += r * matrizConv[indice];
                        sumaG += g * matrizConv[indice];
                        sumaB += b * matrizConv[indice];
                        indice++;
                    }
                }

                int red = Math.min(255, Math.max(0, (int) Math.abs(sumaR)));
                int green = Math.min(255, Math.max(0, (int) Math.abs(sumaG)));
                int blue = Math.min(255, Math.max(0, (int) Math.abs(sumaB)));

                int pixelNuevo = (red << 16) | (green << 8) | blue;
                salida.setRGB(x, y, pixelNuevo);
            }
        }
        return salida;
    }

    private static BufferedImage aplicarKernelRGB(BufferedImage imgOriginal, float[] matrizConv) {
        BufferedImage actual = new BufferedImage(imgOriginal.getWidth(), imgOriginal.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = actual.createGraphics();
        g.drawImage(imgOriginal, 0, 0, null);
        g.dispose();

        int N = (int) Math.sqrt(matrizConv.length);
        Kernel kernel = new Kernel(N, N, matrizConv);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage salida = new BufferedImage(actual.getWidth(), actual.getHeight(), BufferedImage.TYPE_INT_RGB);
        return op.filter(actual, salida);
    }

    public static BufferedImage aplicarIdentity(BufferedImage imgOriginal) {
        float[] kernel = { 0, 0, 0, 0, 1, 0, 0, 0, 0 };
        return aplicarKernelRGB(imgOriginal, kernel);
    }

    public static BufferedImage aplicarEdge1(BufferedImage imgOriginal) {
        float[] kernel = { 1, 0, -1, 0, 0, 0, -1, 0, 1 };
        return aplicarKernelRGB(imgOriginal, kernel);
    }

    public static BufferedImage aplicarEdge2(BufferedImage imgOriginal) {
        float[] kernel = { 0, 1, 0, 1, -4, 1, 0, 1, 0 };
        return aplicarKernelRGB(imgOriginal, kernel);
    }

    public static BufferedImage aplicarEdge3(BufferedImage imgOriginal) {
        float[] kernel = { -1, -1, -1, -1, 8, -1, -1, -1, -1 };
        return aplicarKernelRGB(imgOriginal, kernel);
    }

    public static BufferedImage aplicarSharpenWiki(BufferedImage imgOriginal) {
        float[] kernel = { 0, -1, 0, -1, 5, -1, 0, -1, 0 };
        return aplicarKernelRGB(imgOriginal, kernel);
    }

    public static BufferedImage aplicarBoxBlurWiki(BufferedImage imgOriginal) {
        float[] kernel = { 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f };
        return aplicarKernelRGB(imgOriginal, kernel);
    }

    public static BufferedImage aplicarRelieveRojo(BufferedImage imgOriginal) {
        BufferedImage img = estandarizarImagen(imgOriginal);
        int ancho = img.getWidth();
        int alto = img.getHeight();
        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        float[] kernel = { -2, -1, 0, -1, 1, 1, 0, 1, 2 };

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {
                float sumaR = 0;
                int KIndice = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixelVecino = img.getRGB(x + j, y + i);
                        int r = (pixelVecino >> 16) & 0xFF;
                        sumaR += r * kernel[KIndice];
                        KIndice++;
                    }
                }
                int nuevoR = Math.min(255, Math.max(0, (int) sumaR + 128)); // Regla especial del profe
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                salida.setRGB(x, y, (a << 24) | (nuevoR << 16) | (g << 8) | b);
            }
        }
        return salida;
    }

    public static BufferedImage aplicarSobelNVeces(BufferedImage imgOriginal, int N) {
        BufferedImage actual = new BufferedImage(imgOriginal.getWidth(), imgOriginal.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = actual.createGraphics();
        g.drawImage(imgOriginal, 0, 0, null);
        g.dispose();

        float[] kernelArray = { 1, 0, -1, 0, 0, 0, -1, 0, 1 };
        Kernel kernel = new Kernel(3, 3, kernelArray);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        for (int iter = 0; iter < Math.min(N, 50); iter++) {
            BufferedImage nueva = new BufferedImage(actual.getWidth(), actual.getHeight(), BufferedImage.TYPE_INT_RGB);
            actual = op.filter(actual, nueva);
        }
        return actual;
    }
}
