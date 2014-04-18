package com.shc.tutorials.lwjgl.tutorial1;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.shc.tutorials.lwjgl.util.Game;
import com.shc.tutorials.lwjgl.util.ShaderProgram;

/**
 * Tutorial 1: The First Triangle
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial1 extends Game
{
    // Our shader program
    ShaderProgram shader;
    
    // VBO and VAO IDs
    int vboID, vaoID;
    
    /**
     * Initialize the application
     */
    public void init()
    {
        // Set the title
        Display.setTitle("Tutorial 1: The First Triangle");
        
        // Create a new ShaderProgram
        shader = new ShaderProgram();
        shader.attachVertexShader("com/shc/tutorials/lwjgl/tutorial1/tutorial1.vert");
        shader.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial1/tutorial1.frag");
        shader.link();
        
        // Create a FloatBuffer to hold our vertex data
        FloatBuffer vertices = BufferUtils.createFloatBuffer(6);
        
        // Add vertices of the triangle
        vertices.put(new float[]
        {
            +0.0f, +0.8f,    // Top vertex
            +0.8f, -0.8f,    // Bottom-right vertex
            -0.8f, -0.8f     // Bottom-left vertex
        });
        
        // Rewind the vertices
        vertices.rewind();
        
        // Create a VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        
        // Create a VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        
        // Set the pointers in the VAO
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        
        // Unbind the VAO
        glBindVertexArray(vaoID);
    }
    
    /**
     * Update the application. This method will be called
     * every 1/60th part of second, i.e., 60 times in a second.
     */
    public void update(long elapsedTime)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            Game.end();
    }
    
    /**
     * Render to the screen. This method will be called
     * every 1/60th part of second, just after update()
     */
    public void render()
    {
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT);
        
        // Bind our ShaderProgram
        shader.bind();
        
        // Bind the VAO
        glBindVertexArray(vaoID);
        
        // Enable the location 0 to send vertices to the shader
        glEnableVertexAttribArray(0);
        
        // Draw a triangle with first 3 vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);
         
        // Disable the location 0 and unbind the VAO
        glDisableVertexAttribArray(0);        
        glBindVertexArray(0);
        
        // Unbind the ShaderProgram
        ShaderProgram.unbind();
    }
    
    /**
     * Called when the window is resized
     */
    public void resized()
    {
        // Set the viewport to the entire window
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
    
    /**
     * Called before exiting, to dispose any resources used.
     */
    public void dispose()
    {
        // Dispose the shaders
        shader.dispose();
        
        // Dispose the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
        
        // Dispose the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
    }
    
    public static void main(String[] args)
    {
        new Tutorial1();
    }
}
