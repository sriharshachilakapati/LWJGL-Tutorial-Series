package com.shc.tutorials.lwjgl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Game
{
    private long windowID;

    public Game()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
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
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        windowID = glfwCreateWindow(640, 480, "My GLFW Window", NULL, NULL);

        if (windowID == NULL)
        {
            System.err.println("Error creating a window");
            System.exit(1);
        }

        glfwShowWindow(windowID);

        glfwMakeContextCurrent(windowID);
        GL.createCapabilities();

        glfwSwapInterval(1);
    }

    public static void main(String[] args)
    {
        new Game().start();
    }

    public void end()
    {
        glfwSetWindowShouldClose(windowID, true);
    }

    public void windowMoved(int x, int y)
    {
        System.out.println(String.format("Window moved: [%d, %d]", x, y));
    }

    public void windowResized(int width, int height)
    {
        System.out.println(String.format("Window resized: [%d, %d]", width, height));
    }

    public void windowClosing()
    {
        System.out.println("Window closing");
    }

    public void windowFocusChanged(boolean focused)
    {
        System.out.println(String.format("Window focus changed: FOCUS = %B", focused));
    }

    public void windowIconfyChanged(boolean iconified)
    {
        System.out.println(String.format("Window iconified/restored: ICONIFIED = %B", iconified));
    }

    public void framebufferResized(int width, int height)
    {
        System.out.println(String.format("Framebuffer resized: [%d, %d]", width, height));
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
        float now, delta, last = 0;

        // Set the callbacks
        glfwSetWindowPosCallback(windowID, (window, x, y) -> windowMoved(x, y));
        glfwSetWindowSizeCallback(windowID, (window, width, height) -> windowResized(width, height));
        glfwSetWindowCloseCallback(windowID, window -> windowClosing());
        glfwSetWindowFocusCallback(windowID, (window, focused) -> windowFocusChanged(focused));
        glfwSetWindowIconifyCallback(windowID, (window, iconified) -> windowIconfyChanged(iconified));
        glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> framebufferResized(width, height));

        // Initialize the game
        init();

        // Loop continuously and render and update
        while (!glfwWindowShouldClose(windowID))
        {
            now = (float) glfwGetTime();
            delta = now - last;
            last = now;

            // Update and Render
            update(delta);
            render(delta);

            // Poll the events and swap the buffers
            glfwPollEvents();
            glfwSwapBuffers(windowID);
        }

        // Dispose the game
        dispose();

        // Free the callbacks
        glfwSetWindowPosCallback(windowID, null).free();
        glfwSetWindowSizeCallback(windowID, null).free();
        glfwSetWindowCloseCallback(windowID, null).free();
        glfwSetWindowFocusCallback(windowID, null).free();
        glfwSetWindowIconifyCallback(windowID, null).free();
        glfwSetFramebufferSizeCallback(windowID, null).free();

        // Destroy the window
        glfwDestroyWindow(windowID);
        glfwTerminate();

        System.exit(0);
    }
}
