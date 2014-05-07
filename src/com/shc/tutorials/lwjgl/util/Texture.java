package com.shc.tutorials.lwjgl.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * A simple class for wrapping up OpenGL Textures
 * 
 * @author Sri Harsha Chilakapati
 */
public class Texture
{
    int width, height, id;
    
    /**
     * A private constructor just to store the values
     */
    private Texture(int width, int height, int id)
    {
        this.width  = width;
        this.height = height;
        this.id     = id;
    }
    
    /**
     * Binds the texture to the context
     */
    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, id);
    }
    
    /**
     * Sets the texture unit to bind this texture
     */
    public void setActiveTextureUnit(int unit)
    {
        glActiveTexture(GL_TEXTURE0 + unit);
    }
    
    /**
     * Loads a Texture from a file name.
     */
    public static Texture loadTexture(String name)
    {
        // Load the image
        BufferedImage bimg = null;
        try
        {
            bimg = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(name));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("Unable to load Texture: " + name);
            Game.end();
        }

        // Gather all the pixels
        int[] pixels = new int[bimg.getWidth() * bimg.getHeight()];
        bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0, bimg.getWidth());

        // Create a ByteBuffer
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

        // Iterate through all the pixels and add them to the ByteBuffer
        for (int y = 0; y < bimg.getHeight(); y++)
        {
            for (int x = 0; x < bimg.getWidth(); x++)
            {
                // Select the pixel
                int pixel = pixels[y * bimg.getWidth() + x];
                // Add the RED component
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                // Add the GREEN component
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                // Add the BLUE component
                buffer.put((byte) (pixel & 0xFF));
                // Add the ALPHA component
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        // Reset the read location in the buffer so that GL can read from
        // beginning.
        buffer.rewind();

        // Generate a texture ID
        int textureID = glGenTextures();
        // Bind the ID to the context
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Send texture data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(), bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        
        // Generate Mipmaps
        glGenerateMipmap(GL_TEXTURE_2D);

        // Return a new Texture.
        return new Texture(bimg.getWidth(), bimg.getHeight(), textureID);
    }
}
