package com.shc.tutorials.lwjgl.intro3;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author Sri Harsha Chilakapati
 */
public class Game
{
    private static long    windowID;
    private static boolean running;

    // The callbacks
    GLFWErrorCallback       errorCallback;
    GLFWKeyCallback         keyCallback;
    GLFWCursorPosCallback   cursorPosCallback;
    GLFWMouseButtonCallback mouseButtonCallback;
    GLFWScrollCallback      scrollCallback;

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

        // Create the callbacks
        errorCallback = Callbacks.errorCallbackPrint(System.err);
        keyCallback = GLFWKeyCallback(this::glfwKeyCallback);
        cursorPosCallback = GLFWCursorPosCallback(this::glfwCursorPosCallback);
        mouseButtonCallback = GLFWMouseButtonCallback(this::glfwMouseButtonCallback);
        scrollCallback = GLFWScrollCallback(this::glfwScrollCallback);

        // Set the callbacks
        glfwSetErrorCallback(errorCallback);
        glfwSetKeyCallback(windowID, keyCallback);
        glfwSetCursorPosCallback(windowID, cursorPosCallback);
        glfwSetMouseButtonCallback(windowID, mouseButtonCallback);
        glfwSetScrollCallback(windowID, scrollCallback);

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
        keyCallback.release();
        cursorPosCallback.release();
        mouseButtonCallback.release();
        scrollCallback.release();
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

    public void glfwCursorPosCallback(long window, double xpos, double ypos)
    {
    }

    public void glfwMouseButtonCallback(long window, int button, int action, int mods)
    {
    }

    public void glfwScrollCallback(long window, double xoffset, double yoffset)
    {
    }

    // Static helpful polled input methods

    public static boolean isKeyPressed(int key)
    {
        return glfwGetKey(windowID, key) != GLFW_RELEASE;
    }

    public static boolean isMouseButtonPressed(int button)
    {
        return glfwGetMouseButton(windowID, button) != GLFW_RELEASE;
    }

    public static long getWindowID()
    {
        return windowID;
    }

    public static void main(String[] args)
    {
        new Game().start();
    }
}
