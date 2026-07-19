package org.example.portafolio.ejercicios.opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long handle;
    private final int width;
    private final int height;
    private String title;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        init();
    }

    private void init() {
        Configuration.SHARED_LIBRARY_EXTRACT_DIRECTORY.set("lwjgl_natives");

        if (!glfwInit()) {
            throw new IllegalStateException("No se pudo inicializar GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) {
            throw new RuntimeException("No se pudo crear la ventana GLFW");
        }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1); // Vsync — habilita doble buffering sincronizado
        glfwShowWindow(handle);

        GL.createCapabilities();
        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void setShouldClose(boolean value) {
        glfwSetWindowShouldClose(handle, value);
    }

    public boolean isKeyPressed(int key) {
        return glfwGetKey(handle, key) == GLFW_PRESS;
    }

    public void swapBuffers() {
        glfwSwapBuffers(handle);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
        glfwSetWindowTitle(handle, newTitle);
    }

    public double getTime() {
        return glfwGetTime();
    }

    public float getAspectRatio() {
        return (float) width / height;
    }

    public void destroy() {
        glfwDestroyWindow(handle);
        glfwTerminate();
    }
}
