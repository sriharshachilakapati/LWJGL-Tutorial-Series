package com.shc.tutorials.lwjgl.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 * Utility class for dealing with Quaternions.
 *
 * @author Sri Harsha Chilakapati
 */
public class QuaternionUtil
{
    /**
     * Creates a rotation matrix from a quaternion and stores
     * the result in the dest matrix.
     *
     * @param q    The quaternion to create the matrix from.
     * @param dest The destination matrix to store the result.
     * @return The rotation matrix obtained from the quaternion
     */
    public static Matrix4f toRotationMatrix(Quaternion q, Matrix4f dest)
    {
        if (dest == null)
            dest = new Matrix4f();
        else
            dest.setIdentity();

        // Normalize the quaternion
        q.normalise();

        // The length of the quaternion
        float s = 2f / q.length();

        // Convert the quaternion to matrix
        dest.m00 = 1 - s * (q.y * q.y + q.z * q.z);
        dest.m10 = s * (q.x * q.y + q.w * q.z);
        dest.m20 = s * (q.x * q.z - q.w * q.y);

        dest.m01 = s * (q.x * q.y - q.w * q.z);
        dest.m11 = 1 - s * (q.x * q.x + q.z * q.z);
        dest.m21 = s * (q.y * q.z + q.w * q.x);

        dest.m02 = s * (q.x * q.z + q.w * q.y);
        dest.m12 = s * (q.y * q.z - q.w * q.x);
        dest.m22 = 1 - s * (q.x * q.x + q.y * q.y);

        return dest;
    }

    /**
     * Returns the conjugate of a Quaternion and stores the
     * result in another 'dest' quaternion.
     *
     * @param src  The source Quaternion
     * @param dest The destination Quaternion
     * @return The Conjugate of the given quaternion
     */
    public static Quaternion conjugate(Quaternion src, Quaternion dest)
    {
        if (dest == null)
            dest = new Quaternion();

        // Only negate the axis
        dest.x = -src.x;
        dest.y = -src.y;
        dest.z = -src.z;
        dest.w = +src.w;

        return dest;
    }

    /**
     * Rotates a vector by a Quaternion and stores the result
     * in another destination vector.
     *
     * @param v    The vector to rotate
     * @param q    The quaternion with rotation
     * @param dest The destination vector
     * @return The rotated vector
     */
    public static Vector3f rotate(Vector3f v, Quaternion q, Vector3f dest)
    {
        if (dest == null)
            dest = new Vector3f();

        // Calculate the conjugate of quaternion
        Quaternion q1 = conjugate(q, null);
        Quaternion qv = new Quaternion(v.x, v.y, v.z, 1);

        // Rotate the quaternion
        Quaternion.mul(q, qv, qv);
        Quaternion.mul(qv, q1, qv);

        // Extract vector from rotated quaternion
        dest.x = qv.x;
        dest.y = qv.y;
        dest.z = qv.z;

        return dest;
    }

    /**
     * Creates a Quaternion from an Axis and an angle.
     *
     * @param axis  The axis to rotate upon
     * @param angle The angle of rotation (in degrees)
     * @param dest  The destination quaternion to store
     * @return The new rotation quaternion
     */
    public static Quaternion createFromAxisAngle(Vector3f axis, float angle, Quaternion dest)
    {
        if (dest == null)
            dest = new Quaternion();

        // Normalize the axis
        axis.normalise();

        // Calculate the halfAngle
        float halfAngle = (float) Math.toRadians(angle/2);

        // Create the quaternion from axis-angle
        dest.x = axis.x * (float)Math.sin(halfAngle);
        dest.y = axis.y * (float)Math.sin(halfAngle);
        dest.z = axis.z * (float)Math.sin(halfAngle);

        dest.w = (float)Math.cos(halfAngle);

        // Normalize the quaternion
        dest.normalise();

        return dest;
    }
}
