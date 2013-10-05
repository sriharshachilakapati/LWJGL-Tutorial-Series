package com.shc.tutorials.lwjgl.tutorial12;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.input.Keyboard.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.ShaderProgram;

/**
 * Tutorial 12: Introduction to Shaders
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial12 extends Game
{

    // VBO Vertex Buffer ID
    int vboVertexID;
    // VBO Color Buffer ID
    int vboColorID;

    // Shader program
    ShaderProgram shaderProgram;

    /**
     * Initialize
     */
    public void init()
    {
        Display.setTitle("Tutorial 12: Introduction to Shaders");

        // Initialize OpenGL
        glMatrixMode(GL_PROJECTION);
        gluPerspective(70f, 800f / 600f, 0.1f, 1000);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Enable Depth Testing
        glEnable(GL_DEPTH_TEST);

        // Enable client states
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        // Create Cube vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3 * 24);
        vertexBuffer.put(new float[] {
                // Front face
        -0.5f, +0.5f, +0.5f, +0.5f, +0.5f, +0.5f, -0.5f, -0.5f, +0.5f, +0.5f, -0.5f, +0.5f,

                // Right face
        +0.5f, +0.5f, +0.5f, +0.5f, +0.5f, -0.5f, +0.5f, -0.5f, +0.5f, +0.5f, -0.5f, -0.5f,

                // Back face
        +0.5f, +0.5f, -0.5f, -0.5f, +0.5f, -0.5f, +0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,

                // Left face
        -0.5f, +0.5f, -0.5f, -0.5f, +0.5f, +0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, +0.5f,

                // Top face
        -0.5f, +0.5f, +0.5f, +0.5f, +0.5f, +0.5f, -0.5f, +0.5f, -0.5f, +0.5f, +0.5f, -0.5f,

                // Bottom face
        -0.5f, -0.5f, +0.5f, +0.5f, -0.5f, +0.5f, -0.5f, -0.5f, -0.5f, +0.5f, -0.5f, -0.5f, });
        vertexBuffer.rewind();

        // Create cube colors
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(3 * 24);
        colorBuffer.put(new float[] {
                // Front face
        1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0,

                // Right face
        0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0,

                // Back face
        0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1,

                // Left face
        1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0,

                // Top face
        0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0,

                // Bottom face
        0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1 });
        colorBuffer.rewind();

        // Create vertex VBO
        vboVertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create color VBO
        vboColorID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create Shader Program
        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("com/shc/tutorials/lwjgl/tutorial12/shader.vert");
        shaderProgram.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial12/shader.frag");

        // Link the program
        shaderProgram.link();
    }

    /**
     * Update logic
     */
    public void update(long elapsedTime)
    {
        if (isKeyDown(KEY_ESCAPE))
            end();
    }

    /**
     * Render to screen
     */
    public void render()
    {
        // Clean both color and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Use shader
        shaderProgram.bind();

        // Translate into the view
        glTranslatef(0, 0, -2);

        // Rotate on both x and y axes
        glRotatef(1, 1, 1, 0);

        // Bind the vertex VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        // Bind the color VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glColorPointer(3, GL_FLOAT, 0, 0);

        // Draw the cube with triangle strip
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 24);

        // Translate back
        glTranslatef(0, 0, 2);

        // Disable shader
        ShaderProgram.unbind();
    }

    /**
     * Display resized
     */
    public void resized()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    /**
     * Dispose resources
     */
    public void dispose()
    {
        glDeleteBuffers(vboVertexID);
        glDeleteBuffers(vboColorID);

        shaderProgram.dispose();
    }

    public static void main(String[] args)
    {
        new Tutorial12();
    }

}
