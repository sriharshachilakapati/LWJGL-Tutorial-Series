package com.shc.tutorials.lwjgl.tutorial3;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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
 * Tutorial 3: Element Buffer Objects
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial3 extends Game
{
    // Handles for VAO, VBOs and EBO
    int vaoID, vboVertID, vboColID, eboID;
    
    // Our ShaderProgram
    ShaderProgram shader;
    
    public void init()
    {
        // Set the title
        Display.setTitle("Tutorial 3: Element Buffer Objects");
        
        // Load the shaders
        shader = new ShaderProgram();
        shader.attachVertexShader("com/shc/tutorials/lwjgl/tutorial3/tutorial3.vert");
        shader.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial3/tutorial3.frag");
        shader.link();
        
        // Vertices for our quad
        FloatBuffer vertices = BufferUtils.createFloatBuffer(8);
        vertices.put(new float[]
        {
            -0.8f, +0.8f,    // Top Left:     ID 0
            +0.8f, +0.8f,    // Top Right:    ID 1
            -0.8f, -0.8f,    // Bottom Left:  ID 2
            +0.8f, -0.8f     // Bottom Right: ID 3
        });
        vertices.rewind();
        
        // Colors for the vertices
        FloatBuffer colors = BufferUtils.createFloatBuffer(16);
        colors.put(new float[]
        {
            1, 0, 0, 1,
            0, 1, 0, 1,
            0, 0, 1, 1,
            1, 1, 1, 1
        });
        colors.rewind();
        
        // Elements for our quad
        ShortBuffer elements = BufferUtils.createShortBuffer(4);
        elements.put(new short[]
        {
            0, 1, 2, 3
        });
        elements.rewind();
        
        // Create and bind the VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        
        // Create a VBO for vertices
        vboVertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        // Create a VBO for colors
        vboColID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        // Create a EBO for indexed drawing
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);
        
        // Unbind the VAO
        glBindVertexArray(0);
    }
    
    public void update(long elapsedTime)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            Game.end();
    }
    
    public void render()
    {
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT);
        
        // Bind the shaders
        shader.bind();
        
        // Bind the VAO and enable locations
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        // Draw the elements
        glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, 0);
        
        // Disable the locations and unbind the VAO
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        
        // Unbind the shaders
        ShaderProgram.unbind();
    }
    
    public void resized()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
    
    public void dispose()
    {
        // Dispose the shaders
        shader.dispose();
        
        // Dispose the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
        
        // Dispose the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboVertID);
        glDeleteBuffers(vboColID);
        
        // Dispose the EBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(eboID);
    }
    
    public static void main(String[] args)
    {
        new Tutorial3();
    }
}
