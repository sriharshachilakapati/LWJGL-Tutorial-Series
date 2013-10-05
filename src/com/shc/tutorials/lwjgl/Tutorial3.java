package com.shc.tutorials.lwjgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;

/**
 * Tutorial 3: A simple moving rotating gradient triangle.
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial3 extends Game
{
    // The coordinates of the triangle.
    private float x, y;

    // The rotation of the triangle.
    private float rotation;

    /**
     * Initialize LWJGL and OpenGL context.
     */
    public void init()
    {
        Display.setTitle("Tutorial 3: Moving rotating triangle");

        x = 0;
        y = 0;
        rotation = 0;

        // Set the clearing color to black.
        glClearColor(0, 0, 0, 1);

        // Set identity projection matrix.
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // Set the view matrix.
        glMatrixMode(GL_MODELVIEW);

        // Viewport:
        // ------------------
        // The area of the window which can be drawn.

        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        // Ortho:
        // ------------------
        // The orthographic camera. Set for 2d graphics.
        // The parameters are as follows.
        // left - The left most value on the axis.
        // right - The right most value on the axis.
        // bottom - The bottom most value on the axis.
        // top - The top most value on the axis.
        // zNear - The near value on the z-axis.
        // zFar - The far value on the z-axis.
        glOrtho(0, 800, 600, 0, 1, -1);
    }

    /**
     * Update the game. Moves the coords of the rectangle as per the keyboard
     * input.
     */
    public void update(long elapsedTime)
    {
        if (isKeyDown(KEY_LEFT))
            x -= 4;

        if (isKeyDown(KEY_RIGHT))
            x += 4;

        if (isKeyDown(KEY_UP))
            y -= 4;

        if (isKeyDown(KEY_DOWN))
            y += 4;

        if (isKeyDown(KEY_ESCAPE))
            end();

        if (isKeyDown(KEY_F4))
            switchFullscreen();

        // Rotate 4 degrees clockwise.
        rotation += 4;
    }

    /**
     * Render to the screen.
     */
    public void render()
    {
        // Clear the color information.
        glClear(GL_COLOR_BUFFER_BIT);

        // Create a new draw matrix for custom settings.
        glPushMatrix();
        {
            // Translate to the location.
            glTranslatef(x, y, 0);

            // Translate to the center of triangle
            glTranslatef(50, 50, 0);

            // Rotate on the z-axis with 1-degree.
            glRotatef(rotation, 0, 0, 1);

            // Translate back to the first coord.
            glTranslatef(-50, -50, 0);

            // Draw a triangle.
            glBegin(GL_TRIANGLES);
            {
                // Draw first vertex in red
                glColor3f(1, 0, 0);
                glVertex2f(0, 0);

                // Draw second vertex in green.
                glColor3f(0, 1, 0);
                glVertex2f(0, 100);

                // Draw third vertex in blue.
                glColor3f(0, 0, 1);
                glVertex2f(100, 100);
            }
            glEnd();
        }
        glPopMatrix(); // Restore original settings.
    }

    /**
     * The display has been resized.
     */
    public void resized()
    {
        // Reset the viewport.
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void main(String[] args)
    {
        new Tutorial3();
    }

}