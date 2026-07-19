package org.example.portafolio.ejercicios.convoluciones;

public class kernels {

        // No modifica imagen
        public static final float[] KNormal = {

                        0f, 0f, 0f,
                        0f, 1f, 0f,
                        0f, 0f, 0f
        };

        // Sharkpen
        public static final float[] KSharkpen = {
                        -1f, -1f, -1f,
                        -1f, 9f, -1f,
                        -1f, -1f, -1f
        };

        // Desenfoque (blur)
        public static final float[] KBlur = {
                        1f / 9, 1f / 9, 1f / 9,
                        1f / 9, 1f / 9, 1f / 9,
                        1f / 9, 1f / 9, 1f / 9
        };

        // Deteccion de bordes
        public static final float[] KEdge = {
                        -0.5f, -0.5f, -0.5f,
                        -0.5f, 4f, -0.5f,
                        -0.5f, -0.5f, -0.5f
        };

        // Aclarar
        public static final float[] KLighten = {
                        0.1f, 0.1f, 0.1f,
                        0.1f, 0.1f, 0.1f,
                        0.1f, 0.1f, 0.1f
        };

        // Oscurecer
        public static final float[] KDarken = {
                        0.01f, 0.01f, 0.01f,
                        0.01f, 0.5f, 0.01f,
                        0.01f, 0.01f, 0.01f
        };
}
