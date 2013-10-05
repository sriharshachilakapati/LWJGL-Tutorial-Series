package com.shc.tutorials.lwjgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;

/**
 * Tutorial 5: Display Lists
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial5 extends Game
{
    // ID of the DisplayList
    private int listID;

    /**
     * Initialize the OpenGL and create DisplayList.
     */
    public void init()
    {
        Display.setTitle("Tutorial 5: Display Lists");

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        glOrtho(0, 800, 600, 0, 1, -1);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        // Create a DisplayList ID
        listID = glGenLists(1);

        // Create the DisplayList
        glNewList(listID, GL_COMPILE);
        {
            // Commands to execute in this list.
            glBegin(GL_QUADS);
            {
                // Draw first vertex in red.
                glColor3f(1, 0, 0);
                glVertex2f(0, 0);

                // Draw second vertex in blue.
                glColor3f(0, 1, 0);
                glVertex2f(0, 100);

                // Draw third vertex in green.
                glColor3f(0, 0, 1);
                glVertex2f(100, 100);

                // Draw fourth vertex in white.
                glColor3f(1, 1, 1);
                glVertex2f(100, 0);
            }
            glEnd();
        }
        glEndList();
    }

    /**
     * Update the logic.
     */
    public void update(long elapsedTime)
    {
        if (isKeyDown(KEY_ESCAPE))
            end();

        if (isKeyDown(KEY_F4))
            switchFullscreen();
    }

    /**
     * Render to the screen.
     */
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT);

        // Call the list we created earlier.
        glCallList(listID);
    }

    /**
     * Display is resized.
     */
    public void resized()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void main(String[] args)
    {
        new Tutorial5();
    }

}
