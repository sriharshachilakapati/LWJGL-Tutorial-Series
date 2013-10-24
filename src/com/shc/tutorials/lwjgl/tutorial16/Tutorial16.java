package com.shc.tutorials.lwjgl.tutorial16;

import org.lwjgl.opengl.Display;

import com.shc.tutorials.lwjgl.util.Camera;
import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.ShaderProgram;
import com.shc.tutorials.lwjgl.util.model.Model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

/**
 * Tutorial 16: Textured Models
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial16 extends Game
{

    // The Camera
    Camera camera;
    // Our model
    Model model;
    // The ShaderProgram
    ShaderProgram shaderProgram;

    /**
     * Initialize the resources
     */
    public void init()
    {
        Display.setTitle("Tutorial 16: Textured Models");
        // Create the camera
        camera = new Camera(70, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 100f);
        camera.setPosition(0, 0, -8);

        // Load the plane
        model = Model.loadOBJModel("resources/models/TexturedCube.obj");

        // Create shader program
        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("com/shc/tutorials/lwjgl/tutorial16/shader.vert");
        shaderProgram.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial16/shader.frag");
        shaderProgram.link();

        // Enable face culling
        glCullFace(GL_FRONT_FACE);
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

        // Move Up
        if (isKeyDown(KEY_Z))
            camera.addPosition(0, 0.1f, 0);

        // Move Down
        if (isKeyDown(KEY_X))
            camera.addPosition(0, -0.1f, 0);
        ;
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
        shaderProgram.setUniform("mProjection", camera.getProjectionMatrix());
        shaderProgram.setUniform("mView", camera.getViewMatrix());
        shaderProgram.setUniform("lightPos", camera.getPosition());
        shaderProgram.setUniform("texture", 0);

        // Normal matrix
        shaderProgram.setUniform("mNormal", camera.getNormalMatrix());

        // Render the model
        model.render();

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
        model.dispose();
        shaderProgram.dispose();
    }

    public static void main(String[] args)
    {
        new Tutorial16();
    }

}
