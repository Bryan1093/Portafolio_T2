package org.example.portafolio.ejercicios.opengl;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;

    public ShaderProgram() {
        String vertexSrc = buildVertexShader();
        String fragmentSrc = buildFragmentShader();

        int vs = compileShader(GL_VERTEX_SHADER, vertexSrc);
        int fs = compileShader(GL_FRAGMENT_SHADER, fragmentSrc);

        programId = glCreateProgram();
        glAttachShader(programId, vs);
        glAttachShader(programId, fs);
        glLinkProgram(programId);

        glDeleteShader(vs);
        glDeleteShader(fs);
    }

    private int compileShader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String log = glGetShaderInfoLog(shader);
            throw new RuntimeException("Error compilando shader: " + log);
        }
        return shader;
    }

    private String buildVertexShader() {
        return "#version 330 core\n"
                + "layout(location = 0) in vec3 aPos;\n"
                + "layout(location = 1) in vec3 aColor;\n"
                + "layout(location = 2) in vec2 aTexCoord;\n"
                + "out vec3 vColor;\n"
                + "out vec2 vTexCoord;\n"
                + "uniform mat4 model;\n"
                + "uniform mat4 view;\n"
                + "uniform mat4 projection;\n"
                + "void main() {\n"
                + "    gl_Position = projection * view * model * vec4(aPos, 1.0);\n"
                + "    vColor = aColor;\n"
                + "    vTexCoord = aTexCoord;\n"
                + "}\n";
    }

    private String buildFragmentShader() {
        return "#version 330 core\n"
                + "out vec4 FragColor;\n"
                + "in vec3 vColor;\n"
                + "in vec2 vTexCoord;\n"
                + "uniform sampler2D uTexture;\n"
                + "uniform int useTexture;\n"
                + "uniform int linearDepth;\n"
                + "void main() {\n"
                + "    vec4 color;\n"
                + "    if (useTexture == 1) {\n"
                + "        color = texture(uTexture, vTexCoord);\n"
                + "    } else {\n"
                + "        color = vec4(vColor, 1.0);\n"
                + "    }\n"
                // Linearizar la profundidad para que los objetos lejanos
                // se oscurezcan más que los cercanos (near/far de la proyección)
                + "    float near = 0.1;\n"
                + "    float far = 100.0;\n"
                + "    float z_depth = (2.0 * near) / (far + near - gl_FragCoord.z * (far - near));\n"
                + "    if (linearDepth == 1) {\n"
                + "        // W-Buffer: oscurecimiento suave — toda la escena casi igual\n"
                + "        float factor = 1.0 - (z_depth * 0.15);\n"  // muy suave, escena brillante
                + "        color.rgb *= factor;\n"
                + "    } else {\n"
                + "        // Z-Buffer: penaliza fuerte los objetos lejanos\n"
                + "        float factor = 1.0 - pow(z_depth, 0.3) * 0.85;\n"  // más agresivo
                + "        color.rgb *= factor;\n"
                + "    }\n"
                + "    FragColor = color;\n"
                + "}\n";
    }

    public void use() {
        glUseProgram(programId);
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(programId, name);
    }

    public void delete() {
        glDeleteProgram(programId);
    }
}