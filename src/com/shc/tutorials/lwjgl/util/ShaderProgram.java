package com.shc.tutorials.lwjgl.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * ShaderProgram Class. Used to load and use Vertex and Fragment shaders easily.
 * 
 * @author Sri Harsha Chilakapati
 */
public class ShaderProgram
{
    // ProgramID
    int programID;

    // Vertex Shader ID
    int vertexShaderID;
    // Fragment Shader ID
    int fragmentShaderID;

    /**
     * Create a new ShaderProgram.
     */
    public ShaderProgram()
    {
        programID = glCreateProgram();
    }

    /**
     * Attach a Vertex Shader to this program.
     * 
     * @param name The file name of the vertex shader.
     */
    public void attachVertexShader(String name)
    {
        // Load the source
        String vertexShaderSource = FileUtil.readFromFile(name);

        // Create the shader and set the source
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSource);

        // Compile the shader
        glCompileShader(vertexShaderID);

        // Check for errors
        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
        {
            System.err.println("Unable to create vertex shader:");

            // Print log
            System.err.println(glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));

            dispose();
            Game.end();
        }

        // Attach the shader
        glAttachShader(programID, vertexShaderID);
    }

    /**
     * Attach a Fragment Shader to this program.
     * 
     * @param name
     *            The file name of the Fragment Shader.
     */
    public void attachFragmentShader(String name)
    {
        // Read the source
        String fragmentShaderSource = FileUtil.readFromFile(name);

        // Create the shader and set the source
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSource);

        // Compile the shader
        glCompileShader(fragmentShaderID);

        // Check for errors
        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
        {
            System.err.println("Unable to create fragment shader:");

            // Print log
            System.err.println(glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));

            dispose();
            Game.end();
        }

        // Attach the shader
        glAttachShader(programID, fragmentShaderID);
    }

    /**
     * Links this program in order to use.
     */
    public void link()
    {
        // Link this program
        glLinkProgram(programID);

        // Check for linking errors
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
        {
            System.err.println("Unable to link shader program:");
            dispose();
            Game.end();
        }
    }

    /**
     * Bind this program to use.
     */
    public void bind()
    {
        glUseProgram(programID);
    }

    /**
     * Unbind the shader program.
     */
    public static void unbind()
    {
        glUseProgram(0);
    }

    /**
     * Dispose the program and shaders.
     */
    public void dispose()
    {
        // Unbind the program
        unbind();

        // Detach the shaders
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);

        // Delete the shaders
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        // Delete the program
        glDeleteProgram(programID);
    }

    /**
     * @return The ID of this program.
     */
    public int getID()
    {
        return programID;
    }

    /**
     * Sets a uniform matrix variable.
     * 
     * @param name
     *            The name of the uniform.
     * @param value
     *            The value of the matrix.
     */
    public void setUniform(String name, Matrix4f value)
    {
        glUniformMatrix4(glGetUniformLocation(programID, name), false, MatrixUtil.toFloatBuffer(value));
    }

    public void setUniform(String name, Vector3f value)
    {
        glUniform3f(glGetUniformLocation(programID, name), value.x, value.y, value.z);
    }

    public void setUniform(String name, float value)
    {
        glUniform1f(glGetUniformLocation(programID, name), value);
    }

}
