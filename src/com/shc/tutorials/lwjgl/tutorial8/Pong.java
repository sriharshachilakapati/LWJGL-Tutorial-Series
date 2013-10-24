package com.shc.tutorials.lwjgl.tutorial8;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.Texture;

/**
 * Tutorial 8: LWJGL Pong
 * 
 * @author Sri Harsha Chilakapati
 */
public class Pong extends Game
{
    // The textures
    Texture ballTex, paddleTex, paddle2Tex;

    // The VBOs
    TexturedVBO ballVBO, paddle1VBO, paddle2VBO, backgroundVBO;

    // Objects
    GObject paddle1, paddle2, ball;

    /**
     * Initialize game resources
     */
    public void init()
    {
        Display.setTitle("Tutorial 8: LWJGL Pong");

        // Initialize OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        glOrtho(0, 800, 600, 0, 1, -1);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Load textures
        ballTex = Texture.loadTexture("resources/pong/ball.png");
        paddleTex = Texture.loadTexture("resources/pong/paddle.png");
        paddle2Tex = Texture.loadTexture("resources/pong/paddle2.png");

        // Create VBOs
        ballVBO = TexturedVBO.loadTexturedVBO(ballTex);
        paddle1VBO = TexturedVBO.loadTexturedVBO(paddleTex);
        paddle2VBO = TexturedVBO.loadTexturedVBO(paddle2Tex);

        // Load background and create resized background VBO
        Texture backTex = Texture.loadTexture("resources/pong/background.png");
        backTex.width = 800;
        backTex.height = 600;
        backgroundVBO = TexturedVBO.loadTexturedVBO(backTex);

        // Create objects
        paddle2 = new GObject(paddle2VBO, (800 - 128) / 2, 10);
        paddle1 = new GObject(paddle1VBO, (800 - 128) / 2, 600 - 16 - 10);
        ball = new GObject(ballVBO, (800 - 16) / 2, (600 - 16) / 2);

        // Set ball velocities
        ball.dy = Math.random() > 0.5 ? 4 : -4;
        ball.dx = Math.random() > 0.5 ? 4 : -4;
    }

    /**
     * Update the game logic
     */
    public void update(long elapsedTime)
    {
        // Escape ends the game
        if (isKeyDown(KEY_ESCAPE))
            end();

        // Auto move paddle2
        paddle2.x = ball.x - 128 / 2;

        // Move paddle1 with keyboard
        paddle1.dx = 0;

        if (isKeyDown(KEY_LEFT))
            paddle1.dx = -4;

        if (isKeyDown(KEY_RIGHT))
            paddle1.dx = 4;

        // Bounce ball when it hits paddles
        if (ball.bounds.intersects(paddle1.bounds) || ball.bounds.intersects(paddle2.bounds))
            ball.dy = -ball.dy;

        // Bounce ball when it hits window boundaries
        if (ball.x <= 0 || ball.x + ball.width >= 800)
            ball.dx = -ball.dx;

        // Move all the objects
        ball.move();
        paddle1.move();
        paddle2.move();

        // Fix paddle2 from moving outside window
        paddle2.x = Math.min(paddle2.x, 800 - 128);
        paddle2.x = Math.max(paddle2.x, 0);

        // Fix paddle1 from moving outside window
        paddle1.x = Math.min(paddle1.x, 800 - 128);
        paddle1.x = Math.max(paddle1.x, 0);

        // Reset ball if moved out
        if (ball.y + ball.height >= 600)
        {
            ball.x = (800 - 16) / 2;
            ball.y = (600 - 16) / 2;

            ball.dy = Math.random() > 0.5 ? 4 : -4;
            ball.dx = Math.random() > 0.5 ? 4 : -4;
        }
    }

    /**
     * Render the game to screen
     */
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT);

        // Render the background
        backgroundVBO.render(0, 0);

        // Render the objects
        ball.render();
        paddle1.render();
        paddle2.render();
    }

    /**
     * Display Resized
     */
    public void resized()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    /**
     * Dispose resources used
     */
    public void dispose()
    {
        paddle1VBO.dispose();
        paddle2VBO.dispose();
        ballVBO.dispose();
        backgroundVBO.dispose();
    }

    public static void main(String[] args)
    {
        new Pong();
    }

}
