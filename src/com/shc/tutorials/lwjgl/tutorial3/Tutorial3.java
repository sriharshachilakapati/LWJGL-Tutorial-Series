package com.shc.tutorials.lwjgl.tutorial3;

import com.shc.tutorials.lwjgl.Game;
import com.shc.tutorials.lwjgl.util.ShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Tutorial3 extends Game
{
    private ShaderProgram shaderProgram;

    private int vaoID;
    private int vboVertID;
    private int vboColID;
    private int eboID;

    public void init()
    {
        glfwSetWindowTitle(Game.getWindowID(), "Element Buffer Objects");

        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("com/shc/tutorials/lwjgl/tutorial3/Tutorial3.vs");
        shaderProgram.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial3/Tutorial3.fs");
        shaderProgram.link();

        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // The vertices of our Rectangle
        float[] vertices = new float[]
        {
            -0.8f, +0.8f,  // ID 0: Top left vertex
            +0.8f, +0.8f,  // ID 1: Top right vertex
            -0.8f, -0.8f,  // ID 2: Bottom left vertex
            +0.8f, -0.8f   // ID 3: Bottom right vertex
        };

        // The colors of the vertices
        float[] colors = new float[]
        {
            1, 0, 0, 1,    // Red color for top left vertex
            0, 1, 0, 1,    // Green color for top right vertex
            0, 0, 1, 1,    // Blue color for bottom left vertex
            1, 1, 1, 1     // White color for bottom right vertex
        };

        // The indices that form the rectangle
        short[] indices = new short[]
        {
            0, 1, 2,  // The indices for the left triangle
            1, 2, 3   // The indices for the right triangle
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

        // Create a ShortBuffer of indices
        ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(indices.length);
        indicesBuffer.put(indices).flip();

        // Create the Element Buffer object
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

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

        // Bind the vertex array
        glBindVertexArray(vaoID);

        // Draw a rectangle of 4 vertices, so it is 6 indices
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);

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

        // Dispose the element buffer object
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(eboID);
    }

    public static void main(String[] args)
    {
        new Tutorial3().start();
    }
}
