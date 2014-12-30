package com.shc.tutorials.lwjgl.tutorial1;

import com.shc.tutorials.lwjgl.Game;
import com.shc.tutorials.lwjgl.util.ShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Tutorial1 extends Game
{
    private ShaderProgram shaderProgram;

    private int vaoID;
    private int vboID;

    public void init()
    {
        glfwSetWindowTitle(Game.getWindowID(), "The First Triangle");

        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("com/shc/tutorials/lwjgl/tutorial1/Tutorial1.vs");
        shaderProgram.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial1/Tutorial1.fs");
        shaderProgram.link();

        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // The vertices of our Triangle
        float[] vertices = new float[]
        {
            +0.0f, +0.8f,    // Top coordinate
            -0.8f, -0.8f,    // Bottom-left coordinate
            +0.8f, -0.8f     // Bottom-right coordinate
        };

        // Create a FloatBuffer of vertices
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        // Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // Point the buffer at location 0, the location we set
        // inside the vertex shader. You can use any location
        // but the locations should match
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);
    }

    public void render(float delta)
    {
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT);

        // Use our program
        shaderProgram.bind();

        // Bind the vertex array and enable our location
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        // Draw a triangle of 3 vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Disable our location
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        // Un-bind our program
        ShaderProgram.unbind();
    }

    public void dispose()
    {
        // Dispose the program
        shaderProgram.dispose();

        // Dispose the vertex array
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);

        // Dispose the buffer object
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
    }

    public static void main(String[] args)
    {
        new Tutorial1().start();
    }
}
