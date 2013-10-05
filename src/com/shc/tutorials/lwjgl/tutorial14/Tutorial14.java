package com.shc.tutorials.lwjgl.tutorial14;

import org.lwjgl.opengl.Display;
import com.shc.tutorials.lwjgl.util.Camera;
import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.ShaderProgram;
import com.shc.tutorials.lwjgl.util.model.Model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

/**
 * Tutorial 14: 3D Models
 *  
 * @author Sri Harsha Chilakapati
 */
public class Tutorial14 extends Game
{

    // The Camera
    Camera camera;
    // Our model
    Model plane;
    // The ShaderProgram
    ShaderProgram shaderProgram;

    /**
     * Initialize the resources
     */
    public void init()
    {
        Display.setTitle("Tutorial 14: 3D Models");

        // Create the camera
        camera = new Camera(70, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 100f);
        camera.setPosition(6, 0, 8);
        camera.setRotation(0, 135, 0);

        // Load the plane
        plane = Model.loadOBJModel("resources/models/AirHawk.obj");

        // Set the clear color to Corn-Flower Blue
        glClearColor(0.39f, 0.58f, 0.929f, 1.0f);

        // Create shader program
        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("com/shc/tutorials/lwjgl/tutorial13/shader.vert");
        shaderProgram.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial13/shader.frag");
        shaderProgram.link();
    }

    /**
     * Update logic
     */
    public void update(long elapsedTime)
    {
        if (isKeyDown(KEY_ESCAPE))
            end();

        // Look up
        if (isKeyDown(KEY_UP))
            camera.addRotation(-1f, 0, 0);

        // Look down
        if (isKeyDown(KEY_DOWN))
            camera.addRotation(1f, 0, 0);

        // Turn left
        if (isKeyDown(KEY_LEFT))
            camera.addRotation(0, -1f, 0);

        // Turn right
        if (isKeyDown(KEY_RIGHT))
            camera.addRotation(0, 1f, 0);

        // Move front
        if (isKeyDown(KEY_W))
            camera.move(0.1f, 1);

        // Move back
        if (isKeyDown(KEY_S))
            camera.move(-0.1f, 1);

        // Strafe left
        if (isKeyDown(KEY_A))
            camera.move(0.1f, 0);

        // Strafe right
        if (isKeyDown(KEY_D))
            camera.move(-0.1f, 0);
    }

    /**
     * Render to screen
     */
    public void render()
    {
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Perform camera transformations
        camera.apply();

        // Bind the shaders
        shaderProgram.bind();

        // Set the uniforms
        shaderProgram.setUniform("projection", camera.getProjectionMatrix());
        shaderProgram.setUniform("view", camera.getViewMatrix());

        // Render the model
        plane.render();

        // Unbind the shaders
        ShaderProgram.unbind();
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
        plane.dispose();
        shaderProgram.dispose();
    }

    public static void main(String[] args)
    {
        new Tutorial14();
    }

}
