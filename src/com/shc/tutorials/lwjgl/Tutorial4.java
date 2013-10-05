package com.shc.tutorials.lwjgl;

import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

/**
 * Tutorial 4: Drawing a Texture.
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial4 extends Game
{
    // The texture object.
    Texture texture;

    /**
     * Initialize OpenGL and load Texture.
     */
    public void init()
    {
        Display.setTitle("Tutorial 4: Textures");

        // Setup OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        glOrtho(0, 800, 600, 0, 1, -1);

        // Enable Blending for transparent textures
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Enable textures.
        glEnable(GL_TEXTURE_2D);

        // Load the texture
        texture = Texture.loadTexture("resources/textures/texture1.png");
    }

    /**
     * Update the logic.
     */
    public void update(long elapsedTime)
    {
        // Escape ends the game
        if (isKeyDown(KEY_ESCAPE))
            end();
    }

    /**
     * Render to the screen.
     */
    public void render()
    {
        // Clear the color buffer bit
        glClear(GL_COLOR_BUFFER_BIT);

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.id);

        // Draw a textured quad.
        glBegin(GL_QUADS);
        {
            // Top left corner of the texture
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);

            // Top right corner of the texture
            glTexCoord2f(1, 0);
            glVertex2f(100, 0);

            // Bottom right corner of the texture
            glTexCoord2f(1, 1);
            glVertex2f(100, 100);

            // Bottom left corner of the texture
            glTexCoord2f(0, 1);
            glVertex2f(0, 100);
        }
        glEnd();
    }

    /**
     * Display resized. Reset the Viewport.
     */
    public void resized()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void main(String[] args)
    {
        new Tutorial4();
    }

}
