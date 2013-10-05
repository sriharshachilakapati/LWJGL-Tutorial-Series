package com.shc.tutorials.lwjgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.input.Keyboard.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.Texture;

/**
 * Tutorial 7: Vertex Buffer Objects
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial7 extends Game
{
    // ID for vertex buffer of the vbo
    int vboVertexID;
    // ID for texture buffer of the vbo
    int vboTextureID;

    // The texture
    Texture texture;

    /**
     * Initialize the resources
     */
    public void init()
    {
        Display.setTitle("Tutorial 7: Vertex Buffer Objects");

        // Setup OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        glOrtho(0, 800, 600, 0, 1, -1);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Load the texture
        texture = Texture.loadTexture("resources/textures/texture2.png");

        // Create the buffers
        FloatBuffer vertices = BufferUtils.createFloatBuffer(2 * 4);
        vertices.put(new float[]
        {
            0,   0,
            800, 0,
            800, 600,
            0,   600
        });
        vertices.rewind();

        FloatBuffer texCoords = BufferUtils.createFloatBuffer(2 * 4);
        texCoords.put(new float[]
        {
            0, 0,
            1, 0,
            1, 1,
            0, 1
        });
        texCoords.rewind();

        // Create the vbo
        vboVertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vboTextureID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTextureID);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Update the logic
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
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT);

        // Bind the vertex buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(2, GL_FLOAT, 0, 0);

        // Bind the texture buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboTextureID);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        // Enable Client states
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Draw the textured rectangle
        glDrawArrays(GL_QUADS, 0, 4);

        // Disable Client states
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    /**
     * Display resized
     */
    public void resized()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    /**
     * Dispose the resources
     */
    public void dispose()
    {
        // Dispose the buffers
        glDeleteBuffers(vboVertexID);
        glDeleteBuffers(vboTextureID);
    }

    public static void main(String[] args)
    {
        new Tutorial7();
    }

}
