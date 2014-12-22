package com.shc.tutorials.lwjgl;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Game
{
    private long    windowID;
    private boolean running;

    // The callbacks
    GLFWErrorCallback errorCallback;
    GLFWKeyCallback   keyCallback;

    public Game()
    {
        if (glfwInit() != GL_TRUE)
        {
            System.err.println("Error initializing GLFW");
            System.exit(1);
        }

        // Window Hints for OpenGL context
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        windowID = glfwCreateWindow(640, 480, "My GLFW Window", NULL, NULL);

        if (windowID == NULL)
        {
            System.err.println("Error creating a window");
            System.exit(1);
        }

        glfwMakeContextCurrent(windowID);
        GLContext.createFromCurrent();

        glfwSwapInterval(1);
    }

    public void init()
    {
    }

    public void update(float delta)
    {
    }

    public void render(float delta)
    {
    }

    public void dispose()
    {
    }

    public void start()
    {
        float now, last, delta;

        last = 0;

        // Set the callbacks
        glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
        glfwSetKeyCallback(windowID, keyCallback = GLFWKeyCallback(this::glfwKeyCallback));

        // Initialise the Game
        init();

        running = true;

        // Loop continuously and render and update
        while (running && glfwWindowShouldClose(windowID) != GL_TRUE)
        {
            // Get the time
            now = (float) glfwGetTime();
            delta = now - last;
            last = now;

            // Update and render
            update(delta);
            render(delta);

            // Poll the events and swap the buffers
            glfwPollEvents();
            glfwSwapBuffers(windowID);
        }

        // Dispose the game
        dispose();

        // Release the callbacks
        errorCallback.release();

        // Destroy the window
        glfwDestroyWindow(windowID);
        glfwTerminate();

        System.exit(0);
    }

    public void end()
    {
        running = false;
    }

    // Callback functions which can be overriden

    public void glfwKeyCallback(long window, int key, int scancode, int action, int mods)
    {
        // End on escape
        if (key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE)
            end();
    }

    public static void main(String[] args)
    {
        new Game().start();
    }
}
