package com.shc.tutorials.lwjgl.tutorial2;

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
public class Tutorial2 extends Game
{
    private ShaderProgram shaderProgram;

    private int vaoID;
    private int vboVertID;
    private int vboColID;

    public void init()
    {
        glfwSetWindowTitle(Game.getWindowID(), "Coloring the Triangle");

        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("com/shc/tutorials/lwjgl/tutorial2/Tutorial2.vs");
        shaderProgram.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial2/Tutorial2.fs");
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

        float[] colors = new float[]
        {
            1, 0, 0, 1,
            0, 1, 0, 1,
            0, 0, 1, 1
        };

        // Create a FloatBuffer of vertices
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        // Create a Buffer Object and upload the vertices buffer
        vboVertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // Point the buffer at location 0, the location we set
        // inside the vertex shader. You can use any location
        // but the locations should match
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        // Create a FloatBuffer of colors
        FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorsBuffer.put(colors).flip();

        // Create a Buffer Object and upload the colors buffer
        vboColID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);

        // Point the buffer at location 1, the location we set
        // inside the vertex shader. You can use any location
        // but the locations should match
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // Enable the vertex attribute locations
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

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

        // Draw a triangle of 3 vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);

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
        glDeleteBuffers(vboVertID);
        glDeleteBuffers(vboColID);
    }

    public static void main(String[] args)
    {
        new Tutorial2().start();
    }
}
