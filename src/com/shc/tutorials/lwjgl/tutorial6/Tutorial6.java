package com.shc.tutorials.lwjgl.tutorial6;

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
import com.shc.tutorials.lwjgl.util.Camera;
import com.shc.tutorials.lwjgl.util.ShaderProgram;
import com.shc.tutorials.lwjgl.util.Transform;
import org.lwjgl.util.vector.Vector3f;

/**
 * Tutorial 6: Perspective Camera
 *
 * @author Sri Harsha Chilakapati
 */
public class Tutorial6 extends Game
{
    int vaoID, vboVertID, vboColID, eboID;
    ShaderProgram shader;
    Transform transform;
    Camera camera;

    public void init()
    {
        Display.setTitle("Tutorial 6: Perspective Camera");

        // Load the shaders
        shader = new ShaderProgram();
        shader.attachVertexShader("com/shc/tutorials/lwjgl/tutorial6/tutorial6.vert");
        shader.attachFragmentShader("com/shc/tutorials/lwjgl/tutorial6/tutorial6.frag");
        shader.link();

        // Create a new transform
        transform = new Transform();

        // Create a Camera
        camera = new Camera(67, ((float)Display.getWidth())/((float)Display.getHeight()), 0.1f, 100);
        camera.setPosition(new Vector3f(0, 0, 0.8f));

        // Vertices for our cube
        FloatBuffer vertices = BufferUtils.createFloatBuffer(24);
        vertices.put(new float[]
        {
            -0.5f, +0.5f, +0.5f,    // ID: 0
            +0.5f, +0.5f, +0.5f,    // ID: 1
            -0.5f, -0.5f, +0.5f,    // ID: 2
            +0.5f, -0.5f, +0.5f,    // ID: 3
            +0.5f, +0.5f, -0.5f,    // ID: 4
            +0.5f, -0.5f, -0.5f,    // ID: 5
            -0.5f, +0.5f, -0.5f,    // ID: 6
            -0.5f, -0.5f, -0.5f     // ID: 7
        });
        vertices.rewind();

        // Colors for our cube
        FloatBuffer colors = BufferUtils.createFloatBuffer(32);
        colors.put(new float[]
        {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            1, 1, 1, 0,
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            1, 1, 1, 0
        });
        colors.rewind();

        // Elements for our cube
        ShortBuffer elements = BufferUtils.createShortBuffer(36);
        elements.put(new short[]
        {
            0, 1, 2, 2, 3, 1,    // Front  face
            1, 4, 3, 3, 5, 4,    // Right  face
            4, 6, 5, 5, 7, 6,    // Back   face
            6, 0, 7, 7, 2, 0,    // Left   face
            6, 4, 0, 0, 1, 4,    // Top    face
            7, 5, 2, 2, 3, 5     // Bottom face
        });
        elements.rewind();

        // Create and bind the VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create the VBO for vertices
        vboVertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create the VBO for colors
        vboColID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create the EBO for our cube
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);

        glBindVertexArray(0);

        // Enable Depth Testing
        glEnable(GL_DEPTH_TEST);
    }

    public void update(long elapsedTime)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            Game.end();

        // Look up
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            camera.rotateX(1);

        // Look down
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            camera.rotateX(-1);

        // Turn left
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            camera.rotateY(1);

        // Turn right
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            camera.rotateY(-1);

        // Move front
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            camera.move(Camera.Direction.FORWARD, 0.05f);

        // Move back
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            camera.move(Camera.Direction.BACKWARD, 0.05f);

        // Strafe left
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            camera.move(Camera.Direction.LEFT, 0.05f);

        // Strafe right
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            camera.move(Camera.Direction.RIGHT, 0.05f);

        // Move up
        if (Keyboard.isKeyDown(Keyboard.KEY_Z))
            camera.move(Camera.Direction.UP, 0.05f);

        // Move down
        if (Keyboard.isKeyDown(Keyboard.KEY_X))
            camera.move(Camera.Direction.DOWN, 0.05f);

        // Update the Camera
        camera.update();
    }

    public void render()
    {
        // Clear the screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Move the entire scene left by one
        transform.translate(-1, 0, 0);

        // Draw an array of cubes
        for (int x = 0; x < 2; x++)
        {
            // Move the column of cubes
            transform.translate(x, 0, 0);

            for (int z = 0; z < 5; z++)
            {
                // Add some depth for each row
                transform.translate(0, 0, -2);

                // Bind the shaders
                shader.bind();
                shader.setUniform("m_model", transform.getFloatBuffer());
                shader.setUniform("m_view", camera.getViewBuffer());
                shader.setUniform("m_proj", camera.getProjectionBuffer());

                // Bind the VAO
                glBindVertexArray(vaoID);
                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);

                // Draw a cube
                glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, 0);

                // Unbind the VAO
                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);
                glBindVertexArray(0);

                // Unbind the shaders
                ShaderProgram.unbind();
            }

            transform.reset();
        }
    }

    public void resized()
    {
        // Change the viewport
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
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glDeleteBuffers(vboVertID);
        glDeleteBuffers(vboColID);

        // Delete the EBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glDeleteBuffers(eboID);
    }

    public static void main(String[] args)
    {
        new Tutorial6();
    }
}
