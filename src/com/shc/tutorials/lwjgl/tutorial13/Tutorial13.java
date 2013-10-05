package com.shc.tutorials.lwjgl.tutorial13;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.input.Keyboard.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.shc.tutorials.lwjgl.util.Camera;
import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.ShaderProgram;

/**
 * Tutorial 13: Creating a 3D Camera
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial13 extends Game
{

    // VBO Vertex Buffer ID
    int vboVertexID;
    // VBO Color Buffer ID
    int vboColorID;

    // FloatBuffer for projection matrix
    FloatBuffer projBuffer;
    // FloatBuffer for view matrix
    FloatBuffer viewBuffer;

    // The camera
    Camera camera;

    // Shader Program
    ShaderProgram shaderProgram;

    /**
     * Initialize
     */
    public void init()
    {
        Display.setTitle("Tutorial 13: Creating a 3D Camera");

        // Create the camera
        camera = new Camera(45, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 100f);
        camera.setPosition(new Vector3f(0, 0, -3));

        // Enable client states
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        // Create Cube vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3 * 24);
        vertexBuffer.put(new float[]
        {
            // Front face
            -0.5f, +0.5f, +0.5f,
            +0.5f, +0.5f, +0.5f,
            -0.5f, -0.5f, +0.5f,
            +0.5f, -0.5f, +0.5f,

            // Right face
            +0.5f, +0.5f, +0.5f,
            +0.5f, +0.5f, -0.5f,
            +0.5f, -0.5f, +0.5f,
            +0.5f, -0.5f, -0.5f,

            // Back face
            +0.5f, +0.5f, -0.5f,
            -0.5f, +0.5f, -0.5f,
            +0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,

            // Left face
            -0.5f, +0.5f, -0.5f,
            -0.5f, +0.5f, +0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, +0.5f,

            // Top face
            -0.5f, +0.5f, +0.5f,
            +0.5f, +0.5f, +0.5f,
            -0.5f, +0.5f, -0.5f,
            +0.5f, +0.5f, -0.5f,

            // Bottom face
            -0.5f, -0.5f, +0.5f,
            +0.5f, -0.5f, +0.5f,
            -0.5f, -0.5f, -0.5f,
            +0.5f, -0.5f, -0.5f,
        });
        vertexBuffer.rewind();

        // Create cube colors
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(3 * 24);
        colorBuffer.put(new float[] 
        {
            // Front face
            1, 0, 0,
            0, 1, 0,
            0, 0, 1,
            1, 0, 0,

            // Right face
            0, 1, 0,
            0, 0, 1,
            1, 0, 0,
            0, 1, 0,

            // Back face
            0, 0, 1,
            1, 0, 0,
            0, 1, 0,
            0, 0, 1,

            // Left face
            1, 0, 0,
            0, 1, 0,
            0, 0, 1,
            1, 0, 0,

            // Top face
            0, 1, 0,
            0, 0, 1,
            1, 0, 0,
            0, 1, 0,

            // Bottom face
            0, 0, 1,
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
        });
        colorBuffer.rewind();

        // Create vertex VBO
        vboVertexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create color VBO
        vboColorID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

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
        // Set Camera View
        camera.apply();

        // Bind the shaders
        shaderProgram.bind();

        // Set the uniforms
        shaderProgram.setUniform("projection", camera.getProjectionMatrix());
        shaderProgram.setUniform("view", camera.getViewMatrix());

        // Clean both color and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Bind the vertex VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        // Bind the color VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glColorPointer(3, GL_FLOAT, 0, 0);

        // Draw the cube with triangle strip
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 24);

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
     * Dispose resources
     */
    public void dispose()
    {
        glDeleteBuffers(vboVertexID);
        glDeleteBuffers(vboColorID);
        shaderProgram.dispose();
    }

    public static void main(String[] args)
    {
        new Tutorial13();
    }

}
