package com.shc.tutorials.lwjgl.tutorial4;

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
public class Tutorial4 extends Game
{
    private ShaderProgram shaderProgram;

    private int vaoID;
    private int vboID;

    public void init()
    {
        glfwSetWindowTitle(Game.getWindowID(), "Interleaved Buffer Object");

        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("com/shc/tutorials/lwjgl/tutorial4/Tutorial4.vs");
        shaderProgram.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial4/Tutorial4.fs");
        shaderProgram.link();

        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // The vertices of our triangle
        float[] vertices = new float[]
        {
            // x,    y,     r, g, b, a
            +0.0f, +0.8f,   1, 0, 0, 1,
            -0.8f, -0.8f,   0, 1, 0, 1,
            +0.8f, -0.8f,   0, 0, 1, 1
        };

        // Create a FloatBuffer of vertices
        FloatBuffer interleavedBuffer = BufferUtils.createFloatBuffer(vertices.length);
        interleavedBuffer.put(vertices).flip();

        // Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, interleavedBuffer, GL_STATIC_DRAW);

        // The size of float, in bytes (will be 4)
        final int sizeOfFloat = Float.SIZE / Byte.SIZE;

        // The sizes of the vertex and color components
        final int vertexSize = 2 * sizeOfFloat;
        final int colorSize  = 4 * sizeOfFloat;

        // The 'stride' is the sum of the sizes of individual components
        final int stride = vertexSize + colorSize;

        // The 'offset is the number of bytes from the start of the tuple
        final long offsetPosition = 0;
        final long offsetColor    = 2 * sizeOfFloat;

        // Setup pointers using 'stride' and 'offset' we calculated above
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, offsetPosition);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);

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
        glDeleteBuffers(vboID);
    }

    public static void main(String[] args)
    {
        new Tutorial4().start();
    }
}
