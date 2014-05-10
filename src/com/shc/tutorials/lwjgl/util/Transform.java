package com.shc.tutorials.lwjgl.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Class for handling transformations using matrices.
 * 
 * @author Sri Harsha Chilakapati
 */
public class Transform
{
    // The Matrix for this transform
    private Matrix4f mat;
    // The FloatBuffer to send the values to OpenGL
    private FloatBuffer fbuffer;
    
    /**
     * Creates a new Transform that does nothing.
     */
    public Transform()
    {
        // Create an identity matrix
        mat = new Matrix4f();
        mat.setIdentity();
        
        // Create a FloatBuffer for matrix
        fbuffer = BufferUtils.createFloatBuffer(16);
    }
    
    /**
     * Resets the transformations that are done previously
     */
    public Transform reset()
    {
        mat.setIdentity();
        
        return this;
    }
    
    /**
     * Translate by (x, y, z) in the space
     */
    public Transform translate(float x, float y, float z)
    {
        Matrix4f.translate(new Vector3f(x, y, z), mat, mat);
        
        return this;
    }
    
    /**
     * Rotate by (rx, ry, rz) in the space
     */
    public Transform rotate(float rx, float ry, float rz)
    {
        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), mat, mat);
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), mat, mat);
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), mat, mat);
        
        return this;
    }
    
    /**
     * @return The matrix used by this transform
     */
    public Matrix4f getMatrix()
    {
        return mat;
    }
    
    /**
     * @return A FloatBuffer containing the matrix of this transform
     */
    public FloatBuffer getFloatBuffer()
    {
        fbuffer.clear();
        mat.store(fbuffer);
        fbuffer.rewind();
        
        return fbuffer;
    }
}
