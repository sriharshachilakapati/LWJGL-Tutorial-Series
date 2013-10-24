package com.shc.tutorials.lwjgl.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

/**
 * A Simple utility class to deal with matrices
 * 
 * @author Sri Harsha Chilakapati
 */
public class MatrixUtil
{

    /**
     * Converts a Matrix into a FloatBuffer
     */
    public static FloatBuffer toFloatBuffer(Matrix4f mat)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        mat.store(buffer);
        buffer.flip();
        return buffer;
    }

    /**
     * Generates a Perspective Projection Matrix
     * 
     * @param fov
     *            The vertical field of view
     * @param aspect
     *            The aspect ratio
     * @param zNear
     *            The near plane
     * @param zFar
     *            The far plane
     * @return
     */
    public static Matrix4f createPerspectiveProjection(float fov, float aspect, float zNear, float zFar)
    {
        Matrix4f mat = new Matrix4f();

        float yScale = 1f / (float) Math.tan(Math.toRadians(fov / 2f));
        float xScale = yScale / aspect;
        float frustumLength = zFar - zNear;

        mat.m00 = xScale;
        mat.m11 = yScale;
        mat.m22 = -((zFar + zNear) / frustumLength);
        mat.m23 = -1;
        mat.m32 = -((2 * zFar * zNear) / frustumLength);
        mat.m33 = 0;

        return mat;
    }

    /**
     * @return A new identity matrix.
     */
    public static Matrix4f createIdentityMatrix()
    {
        Matrix4f mat = new Matrix4f();
        mat.setIdentity();
        return mat;
    }

}
