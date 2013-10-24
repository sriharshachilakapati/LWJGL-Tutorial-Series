package com.shc.tutorials.lwjgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.Texture;

/**
 * Tutorial 6: Vertex Arrays
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial6 extends Game
{
    // Array of the quad
    FloatBuffer rect;
    // Texture coords array
    FloatBuffer tex;

    // The texture
    Texture texture;

    /**
     * Initialize
     */
    public void init()
    {
        Display.setTitle("Tutorial 6: Vertex Arrays");

        // Init OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        glOrtho(0, 800, 600, 0, 1, -1);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        texture = Texture.loadTexture("resources/textures/texture1.png");

        // Create Vertex Arrays
        rect = BufferUtils.createFloatBuffer(2 * 4);
        tex = BufferUtils.createFloatBuffer(2 * 4);

        rect.put(new float[] {
                // X, Y
        0, 0, // <- First vertex
        100, 0, // <- Second vertex
        100, 100, // <- Third vertex
        0, 100 // <- Fourth vertex
        });

        tex.put(new float[] {
                // X, Y
        0, 0, // <- First texture coord
        1, 0, // <- Second texture coord
        1, 1, // <- Third texture coord
        0, 1 // <- Fourth texture coord
        });
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
     * Render to the screen
     */
    public void render()
    {
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT);

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.id);

        // Enable Vertex Arrays and Texture Coord Arrays
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Rewind the buffers to beginning
        rect.rewind();
        tex.rewind();

        // Set the pointers to them
        glTexCoordPointer(2, 0, tex);
        glVertexPointer(2, 0, rect);

        // Draw them
        glDrawArrays(GL_QUADS, 0, 4);

        // Disable vertex arrays and texture coord arrays
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

    public static void main(String[] args)
    {
        new Tutorial6();
    }

}
