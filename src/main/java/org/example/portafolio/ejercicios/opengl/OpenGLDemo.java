package org.example.portafolio.ejercicios.opengl;

import java.awt.image.BufferedImage;

public class OpenGLDemo {

    public static BufferedImage customTextureImage = null;

    public static void main(String[] args) {
        System.out.println("=== OpenGL Demo - Grupo 3 ===");
        System.out.println("Controles:");
        System.out.println("  ESC - Salir");
        System.out.println("  L   - Alternar Z-Buffer / W-Buffer simulado");
        System.out.println("  F   - Activar/Desactivar Z-Buffer (Algoritmo del Pintor)");

        Window window = new Window(1024, 768, "OpenGL 3D - Grupo 3 [ESC: Salir | L: Alternar Z/W-Buffer | F: Activar/Desactivar Z-Buffer]");
        Renderer renderer = new Renderer(window);
        renderer.run();
    }
}
