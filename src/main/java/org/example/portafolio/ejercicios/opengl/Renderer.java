package org.example.portafolio.ejercicios.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.awt.image.BufferedImage;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

public class Renderer {

    private final Window window;
    private ShaderProgram shader;

    private int vaoCube, vboCube;
    private int vaoFloor, vboFloor;
    private int texture;

    private boolean showLinearDepth = false;
    private boolean depthTestEnabled = true;

    public Renderer(Window window) {
        this.window = window;
        setup();
    }

    private void setup() {
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glClearColor(0.1f, 0.1f, 0.15f, 1.0f);

        shader = new ShaderProgram();
        setupCube();
        setupFloor();
        setupTexture();

        System.out.println("✓ Inicialización completa");
    }

    private void setupCube() {
        float[] vertices = {
                // Cara frontal
                -0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                // Cara trasera
                -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                // Cara izquierda
                -0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                // Cara derecha
                0.5f, 0.5f, 0.5f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.5f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f,
                // Cara inferior
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                // Cara superior
                -0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f
        };
        vaoCube = glGenVertexArrays();
        vboCube = glGenBuffers();
        uploadMesh(vaoCube, vboCube, vertices);
    }

    private void setupFloor() {
        float[] vertices = {
                -0.5f, 0.0f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                0.5f, 0.0f, -0.5f, 1.0f, 1.0f, 1.0f, 5.0f, 0.0f,
                0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f, 5.0f, 5.0f,
                -0.5f, 0.0f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f, 5.0f, 5.0f,
                -0.5f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 5.0f
        };
        vaoFloor = glGenVertexArrays();
        vboFloor = glGenBuffers();
        uploadMesh(vaoFloor, vboFloor, vertices);
    }

    private void uploadMesh(int vao, int vbo, float[] vertices) {
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);

        int stride = 8 * Float.BYTES;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    private void setupTexture() {
        ByteBuffer pixels;
        int w, h;
        
        if (OpenGLDemo.customTextureImage != null) {
            BufferedImage image = OpenGLDemo.customTextureImage;
            w = image.getWidth();
            h = image.getHeight();
            
            int[] rawPixels = new int[w * h];
            image.getRGB(0, 0, w, h, rawPixels, 0, w);

            pixels = MemoryUtil.memAlloc(w * h * 4);
            // Flip vertically because OpenGL textures start from bottom-left
            for (int y = h - 1; y >= 0; y--) {
                for (int x = 0; x < w; x++) {
                    int pixel = rawPixels[y * w + x];
                    pixels.put((byte) ((pixel >> 16) & 0xFF));     // Red
                    pixels.put((byte) ((pixel >> 8) & 0xFF));      // Green
                    pixels.put((byte) (pixel & 0xFF));             // Blue
                    pixels.put((byte) ((pixel >> 24) & 0xFF));     // Alpha
                }
            }
            pixels.flip();
        } else {
            STBImage.stbi_set_flip_vertically_on_load(true);
            int[] width = new int[1];
            int[] height = new int[1];
            int[] channels = new int[1];

            pixels = STBImage.stbi_load("src/main/resources/textura.jpg", width, height, channels, 4);

            if (pixels == null) {
                throw new RuntimeException("No se pudo cargar la textura: " + STBImage.stbi_failure_reason());
            }
            w = width[0];
            h = height[0];
        }

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        glGenerateMipmap(GL_TEXTURE_2D);

        if (OpenGLDemo.customTextureImage != null) {
            MemoryUtil.memFree(pixels);
        } else {
            STBImage.stbi_image_free(pixels); // liberar memoria
        }
    }

    public void run() {
        Matrix4f projection = new Matrix4f().perspective(
                (float) Math.toRadians(45.0), window.getAspectRatio(), 0.1f, 100.0f);

        Matrix4f view = new Matrix4f().lookAt(
                new Vector3f(4, 3, 4),
                new Vector3f(0, 0, 0),
                new Vector3f(0, 1, 0));

        float angle = 0;
        double lastToggleTime = 0;

        while (!window.shouldClose()) {
            double currentTime = window.getTime();

            lastToggleTime = handleInput(currentTime, lastToggleTime);

            draw(projection, view, angle);

            angle += 0.01f;
            window.swapBuffers();
            window.pollEvents();
        }

        cleanup();
    }

    private double handleInput(double currentTime, double lastToggleTime) {
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            window.setShouldClose(true);
        }

        if (window.isKeyPressed(GLFW_KEY_L) && currentTime - lastToggleTime > 0.3) {
            showLinearDepth = !showLinearDepth;
            lastToggleTime = currentTime;

            if (showLinearDepth) {
                window.setTitle("OpenGL 3D: W-Buffer SIMULADO [ESC: Salir | L: Alternar Z/W | F: Depth Test]");
                System.out.println("=== W-Buffer SIMULADO ===");
                System.out.println("► Escena MÁS BRILLANTE");
                System.out.println("► El piso lejano y el cubo cercano se ven con brillo similar");
                System.out.println("► Representa: precisión uniforme en toda la escena");
                System.out.println("► Fórmula: factor = 1.0 - (z_depth * 0.15)  →  oscurecimiento mínimo");
            } else {
                window.setTitle("OpenGL 3D: Z-Buffer ESTÁNDAR [ESC: Salir | L: Alternar Z/W | F: Depth Test]");
                System.out.println("=== Z-Buffer ESTÁNDAR ===");
                System.out.println("► Escena MÁS OSCURA");
                System.out.println("► El piso lejano se oscurece MÁS que el cubo cercano");
                System.out.println("► Representa: precisión concentrada cerca, se pierde a lo lejos");
                System.out.println("► Fórmula: factor = 1.0 - pow(z_depth, 0.3) * 0.85  →  curva agresiva");
            }
        }

        if (window.isKeyPressed(GLFW_KEY_F) && currentTime - lastToggleTime > 0.3) {
            depthTestEnabled = !depthTestEnabled;
            lastToggleTime = currentTime;

            if (depthTestEnabled) {
                glEnable(GL_DEPTH_TEST);
                window.setTitle("OpenGL 3D: Z-Buffer ACTIVO [ESC: Salir | L: Alternar Z/W | F: Depth Test]");
                System.out.println("=== Z-Buffer ACTIVADO ===");
            } else {
                glDisable(GL_DEPTH_TEST);
                window.setTitle("OpenGL 3D: Z-Buffer DESACTIVADO (Pintor) [ESC: Salir | L: Alternar Z/W | F: Depth Test]");
                System.out.println("=== Z-Buffer DESACTIVADO (Algoritmo del Pintor) ===");
            }
        }

        return lastToggleTime;
    }

    private void draw(Matrix4f projection, Matrix4f view, float angle) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        shader.use();

        glUniformMatrix4fv(shader.getUniformLocation("projection"), false, projection.get(new float[16]));
        glUniformMatrix4fv(shader.getUniformLocation("view"), false, view.get(new float[16]));
        glUniform1i(shader.getUniformLocation("linearDepth"), showLinearDepth ? 1 : 0);

        Matrix4f modelFloor = new Matrix4f().identity().scale(5, 1, 5);
        Matrix4f modelCube = new Matrix4f().identity().translate(0, 0.5f, 0).rotateY(angle);

        if (depthTestEnabled) {
            drawFloor(modelFloor);
            drawCube(modelCube);
        } else {
            drawCube(modelCube);
            drawFloor(modelFloor);
        }
    }

    private void drawFloor(Matrix4f model) {
        glUniformMatrix4fv(shader.getUniformLocation("model"), false, model.get(new float[16]));
        glUniform1i(shader.getUniformLocation("useTexture"), 1);
        glBindVertexArray(vaoFloor);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    private void drawCube(Matrix4f model) {
        glUniformMatrix4fv(shader.getUniformLocation("model"), false, model.get(new float[16]));
        glUniform1i(shader.getUniformLocation("useTexture"), 0);
        glBindVertexArray(vaoCube);
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    private void cleanup() {
        glDeleteVertexArrays(vaoCube);
        glDeleteBuffers(vboCube);
        glDeleteVertexArrays(vaoFloor);
        glDeleteBuffers(vboFloor);
        shader.delete();
        window.destroy();
    }
}
