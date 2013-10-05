package com.shc.tutorials.lwjgl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import static org.lwjgl.input.Keyboard.*;

/**
 * Tutorial 9: Introduction to 3D
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial9 extends Game
{

    // VBO Vertex ID
    int vboVertexID;
    // VBO Color ID
    int vboColorID;

    /**
     * Initialize resources
     */
    public void init()
    {
        Display.setTitle("Tutorial 9: Introduction to 3D");

        // Initialize OpenGL
        glMatrixMode(GL_PROJECTION);
        glOrtho(-1, 1, -1, 1, 1, -1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        // Enable Depth Test
        glEnable(GL_DEPTH_TEST);

        // Create Pyramid Vertex Buffer
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3 * 12);
        vertexBuffer.put(new float[] {
                // Front face
        +0.0f, +0.5f, +0.0f, -0.5f, -0.5f, +0.5f, +0.5f, -0.5f, +0.5f,

                // Right face
        +0.0f, +0.5f, +0.0f, +0.5f, -0.5f, +0.5f, +0.5f, -0.5f, -0.5f,

                // Back face
        +0.0f, +0.5f, +0.0f, +0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,

                // Left face
        +0.0f, +0.5f, +0.0f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, +0.5f });
        vertexBuffer.rewind();

        // Create Pyramid Color Buffer
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(3 * 12);
        colorBuffer.put(new float[] {
                // Front face
        1, 0, 0, 0, 1, 0, 0, 0, 1,

                // Right face
        1, 0, 0, 0, 1, 0, 0, 0, 1,

                // Back face
        1, 0, 0, 0, 1, 0, 0, 0, 1,

                // Left face
        1, 0, 0, 0, 1, 0, 0, 0, 1, });
        colorBuffer.rewind();

        // Create Vertex VBO
        vboVertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create Color VBO
        vboColorID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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
     * Render to the screen
     */
    public void render()
    {
        // Clear both color and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Rotate on the y-axis
        glRotatef(1, 0, 1, 0);

        // Enable client states
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        // Bind the vertex buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        // Bind the color buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glColorPointer(3, GL_FLOAT, 0, 0);

        // Draw the pyramid
        glDrawArrays(GL_TRIANGLES, 0, 12);

        // Disable client states
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
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
    }

    public static void main(String[] args)
    {
        new Tutorial9();
    }

}
