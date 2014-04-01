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

public class Tutorial1 extends Game
{
    ShaderProgram shader;
    
    int vboID, vaoID;
    
    public void init()
    {
        Display.setTitle("Tutorial 1: A simple triangle");
        
        shader = new ShaderProgram();
        shader.attachVertexShader("com/shc/tutorials/lwjgl/tutorial1/tutorial1.vert");
        shader.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial1/tutorial1.frag");
        shader.link();
        
        FloatBuffer vertices = BufferUtils.createFloatBuffer(6);
        
        vertices.put(new float[]
        {
            +0.0f, +0.8f,
            +0.8f, -0.8f,
            -0.8f, -0.8f
        });
        
        vertices.rewind();
        
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    }
    
    public void update(long elapsedTime)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            Game.end();
    }
    
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        
        shader.bind();
        
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        
        glEnableVertexAttribArray(0);
        
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        
        glDrawArrays(GL_TRIANGLES, 0, 3);
         
        glDisableVertexAttribArray(0);        
        glBindVertexArray(0);
        
        ShaderProgram.unbind();
    }
    
    public void resized()
    {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
    
    public void dispose()
    {
        shader.dispose();
        
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
    }
    
    public static void main(String[] args)
    {
        new Tutorial1();
    }
}
