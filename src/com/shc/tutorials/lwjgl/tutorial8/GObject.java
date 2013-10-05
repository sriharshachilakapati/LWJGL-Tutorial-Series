package com.shc.tutorials.lwjgl.tutorial8;

import java.awt.Rectangle;

/**
 * A simple game object
 * 
 * @author Sri Harsha Chilakapati
 */
public class GObject
{

    // Position, width and height
    public float x, y, width, height;

    // The VBO of this object
    public TexturedVBO vbo;

    // The hit box
    public Rectangle bounds;

    // Velocities
    public float dx, dy;

    /**
     * Create an object with textured VBO and a position
     */
    public GObject(TexturedVBO vbo, float x, float y)
    {
        this.vbo = vbo;
        this.x = x;
        this.y = y;
        width = vbo.texture.width;
        height = vbo.texture.height;
        bounds = new Rectangle();
        bounds.x = (int) x;
        bounds.y = (int) y;
        bounds.width = (int) width;
        bounds.height = (int) height;
    }

    /**
     * Move the object. Applies velocities
     */
    public void move()
    {
        x += dx;
        y += dy;
        bounds.x = (int) x;
        bounds.y = (int) y;
    }

    /**
     * Render to screen.
     */
    public void render()
    {
        vbo.render(x, y);
    }

}
