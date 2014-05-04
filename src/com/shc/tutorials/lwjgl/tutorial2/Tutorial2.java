package com.shc.tutorials.lwjgl.tutorial2;

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
 * Tutorial 2: Coloring the Triangle
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial2 extends Game
{
    // Our ShaderProgram
    ShaderProgram shader;
    // VAO and VBO handles
    int vaoID, vboVertID, vboColID;
    
    public void init()
    {
        // Set the title
        Display.setTitle("Tutorial 2: Coloring the Triangle");
        
        // Create our ShaderProgram
        shader = new ShaderProgram();
        shader.attachVertexShader("com/shc/tutorials/lwjgl/tutorial2/tutorial2.vert");
        shader.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial2/tutorial2.frag");
        shader.link();
        
        // Create vertex data
        FloatBuffer vertices = BufferUtils.createFloatBuffer(6);
        vertices.put(new float[]
        {
            +0.0f, +0.8f,
            +0.8f, -0.8f,
            -0.8f, -0.8f
        });
        vertices.rewind();
        
        // Create color data
        FloatBuffer colors = BufferUtils.createFloatBuffer(12);
        colors.put(new float[]
        {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0
        });
        colors.rewind();
        
        // Create VAO handle and bind it
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        
        // Create a VBO for vertices
        vboVertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        
        // Create a VBO for colors
        vboColID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        
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
        
        // Draw the triangle
        glDrawArrays(GL_TRIANGLES, 0, 3);
        
        // Disable locations and unbind the VAO
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
        
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
        
        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboVertID);
        glDeleteBuffers(vboColID);
    }
    
    public static void main(String[] args)
    {
        new Tutorial2();
    }
}
