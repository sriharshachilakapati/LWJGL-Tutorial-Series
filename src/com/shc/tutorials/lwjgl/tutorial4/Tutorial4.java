package com.shc.tutorials.lwjgl.tutorial4;

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
import com.shc.tutorials.lwjgl.util.Texture;

/**
 * Tutorial 4: Textures
 * 
 * @author Sri Harsha Chilakapati
 */
public class Tutorial4 extends Game
{
    // IDs for VAOs, VBOs and EBO
    int vaoID, vboVertID, vboTexID, eboID;
    
    // Our shaders
    ShaderProgram shader;
    
    // Our texture
    Texture texture;
    
    public void init()
    {
        // Set the title
        Display.setTitle("Tutorial 4: Textures");
        
        // Load the shaders
        shader = new ShaderProgram();
        shader.attachVertexShader("com/shc/tutorials/lwjgl/tutorial4/tutorial4.vert");
        shader.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial4/tutorial4.frag");
        shader.link();
        
        // Set the texture sampler
        shader.setUniform("tex", 0);
        
        // The vertices for our textured rectangle
        FloatBuffer vertices = BufferUtils.createFloatBuffer(8);
        vertices.put(new float[]
        {
            -0.8f, +0.8f,    // ID 0: Top Left
            +0.8f, +0.8f,    // ID 1: Top Right
            -0.8f, -0.8f,    // ID 2: Bottom Left
            +0.8f, -0.8f     // ID 3: Bottom Right
        });
        vertices.rewind();
        
        // The texture coordinates
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(8);
        texCoords.put(new float[]
        {
            0, 0,    // Top Left
            1, 0,    // Top Right
            0, 1,    // Bottom Left
            1, 1     // Bottom Right
        });
        texCoords.rewind();
        
        // Elements for the EBO
        ShortBuffer elements = BufferUtils.createShortBuffer(4);
        elements.put(new short[]
        {
            0, 1, 2, 3
        });
        elements.rewind();
        
        // Create and bind the VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        
        // Create the VBO for vertices
        vboVertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        // Create the VBO for texture coordinates
        vboTexID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTexID);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        // Create the EBO
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);
        
        // Unbind the VAO
        glBindVertexArray(0);
        
        // Load the texture and bind to our sampler
        texture = Texture.loadTexture("com/shc/tutorials/lwjgl/tutorial4/texture.png");
        texture.setActiveTextureUnit(0);
        texture.bind();
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
        glDeleteBuffers(vboTexID);
        
        // Dispose the EBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(eboID);
    }
    
    public static void main(String[] args)
    {
        new Tutorial4();
    }
}
