package org.example.portafolio.ejercicios.colores;

import java.awt.image.BufferedImage;
import java.io.File;

public class matrizDeColores {

        // ─── Básicos ──────────────────────────────────────────────────────────────

        static final float[][] IDENTIDAD = {
                        { 1.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 1.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 1.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] GRIS = {
                        { 0.299f, 0.587f, 0.114f, 0.0f },
                        { 0.299f, 0.587f, 0.114f, 0.0f },
                        { 0.299f, 0.587f, 0.114f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] SEPIA = {
                        { 0.393f, 0.769f, 0.189f, 0.0f },
                        { 0.349f, 0.686f, 0.168f, 0.0f },
                        { 0.272f, 0.534f, 0.131f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] INVERTIR = {
                        { -1.0f, 0.0f, 0.0f, 1.0f },
                        { 0.0f, -1.0f, 0.0f, 1.0f },
                        { 0.0f, 0.0f, -1.0f, 1.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        // ─── Ajustes de brillo y contraste ───────────────────────────────────────

        static final float[][] BRILLO_SUBIR = {
                        { 1.0f, 0.0f, 0.0f, 0.3f },
                        { 0.0f, 1.0f, 0.0f, 0.3f },
                        { 0.0f, 0.0f, 1.0f, 0.3f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] BRILLO_BAJAR = {
                        { 1.0f, 0.0f, 0.0f, -0.3f },
                        { 0.0f, 1.0f, 0.0f, -0.3f },
                        { 0.0f, 0.0f, 1.0f, -0.3f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        // Contraste: escala desde el punto medio. >1 = más contraste, <1 = menos
        static final float[][] CONTRASTE_ALTO = {
                        { 1.8f, 0.0f, 0.0f, -0.4f },
                        { 0.0f, 1.8f, 0.0f, -0.4f },
                        { 0.0f, 0.0f, 1.8f, -0.4f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] CONTRASTE_BAJO = {
                        { 0.5f, 0.0f, 0.0f, 0.25f },
                        { 0.0f, 0.5f, 0.0f, 0.25f },
                        { 0.0f, 0.0f, 0.5f, 0.25f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        // ─── Canales individuales ─────────────────────────────────────────────────

        static final float[][] SOLO_ROJO = {
                        { 1.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] SOLO_VERDE = {
                        { 0.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 1.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] SOLO_AZUL = {
                        { 0.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 1.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        // Intercambia R <-> B, produce tonos morados/naranjas
        static final float[][] INTERCAMBIAR_RB = {
                        { 0.0f, 0.0f, 1.0f, 0.0f },
                        { 0.0f, 1.0f, 0.0f, 0.0f },
                        { 1.0f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        // ─── Temperatura de color ─────────────────────────────────────────────────

        static final float[][] FRIO = {
                        { 0.8f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 1.0f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 1.2f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] CALIDO = {
                        { 1.2f, 0.0f, 0.0f, 0.0f },
                        { 0.0f, 1.1f, 0.0f, 0.0f },
                        { 0.0f, 0.0f, 0.8f, 0.0f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        // ─── Artísticos ───────────────────────────────────────────────────────────

        static final float[][] POLAROID = {
                        { 0.638f, 0.329f, 0.033f, 0.03f },
                        { 0.169f, 0.686f, 0.145f, 0.03f },
                        { 0.169f, 0.243f, 0.588f, 0.03f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        static final float[][] VINTAGE = {
                        { 0.9f, 0.05f, 0.05f, 0.08f },
                        { 0.05f, 0.85f, 0.1f, 0.06f },
                        { 0.1f, 0.1f, 0.8f, 0.05f },
                        { 0.0f, 0.0f, 0.0f, 1.0f }
        };

        public static void aplicar(String rutaEntrada, String rutaSalida, float[][] matriz) {
                try {
                        BufferedImage imagenOriginal = org.example.portafolio.utils.ImageIOManager
                                        .read(new File(rutaEntrada));
                        if (imagenOriginal == null) {
                                System.out.println("Error: no se pudo leer " + rutaEntrada);
                                return;
                        }

                        int ancho = imagenOriginal.getWidth();
                        int alto = imagenOriginal.getHeight();
                        BufferedImage imagenSalida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

                        for (int y = 0; y < alto; y++) {
                                for (int x = 0; x < ancho; x++) {
                                        int pixel = imagenOriginal.getRGB(x, y);
                                        float r = (pixel >> 16) & 0xFF;
                                        float g = (pixel >> 8) & 0xFF;
                                        float b = pixel & 0xFF;

                                        int r1 = clamp((int) (r * matriz[0][0] + g * matriz[0][1] + b * matriz[0][2]
                                                        + 255 * matriz[0][3]));
                                        int g1 = clamp((int) (r * matriz[1][0] + g * matriz[1][1] + b * matriz[1][2]
                                                        + 255 * matriz[1][3]));
                                        int b1 = clamp((int) (r * matriz[2][0] + g * matriz[2][1] + b * matriz[2][2]
                                                        + 255 * matriz[2][3]));

                                        imagenSalida.setRGB(x, y, (255 << 24) | (r1 << 16) | (g1 << 8) | b1);
                                }
                        }

                        if (org.example.portafolio.utils.ImageIOManager.write(imagenSalida, "jpg",
                                        new File(rutaSalida))) {
                                System.out.println("Guardado: " + rutaSalida);
                        } else {
                                System.out.println("Error al guardar: " + rutaSalida);
                        }

                } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        private static int clamp(int v) {
                return Math.min(255, Math.max(0, v));
        }

        public static void main(String[] args) {
                String entrada = "images\\manu.jpg";

                aplicar(entrada, "images\\manu_identidad.jpg", IDENTIDAD);
                aplicar(entrada, "images\\manu_gris.jpg", GRIS);
                aplicar(entrada, "images\\manu_sepia.jpg", SEPIA);
                aplicar(entrada, "images\\manu_invertir.jpg", INVERTIR);
                aplicar(entrada, "images\\manu_brillo_subir.jpg", BRILLO_SUBIR);
                aplicar(entrada, "images\\manu_brillo_bajar.jpg", BRILLO_BAJAR);
                aplicar(entrada, "images\\manu_contraste_alto.jpg", CONTRASTE_ALTO);
                aplicar(entrada, "images\\manu_contraste_bajo.jpg", CONTRASTE_BAJO);
                aplicar(entrada, "images\\manu_solo_rojo.jpg", SOLO_ROJO);
                aplicar(entrada, "images\\manu_solo_verde.jpg", SOLO_VERDE);
                aplicar(entrada, "images\\manu_solo_azul.jpg", SOLO_AZUL);
                aplicar(entrada, "images\\manu_intercambiar_rb.jpg", INTERCAMBIAR_RB);
                aplicar(entrada, "images\\manu_frio.jpg", FRIO);
                aplicar(entrada, "images\\manu_calido.jpg", CALIDO);
                aplicar(entrada, "images\\manu_polaroid.jpg", POLAROID);
                aplicar(entrada, "images\\manu_vintage.jpg", VINTAGE);
        }
}