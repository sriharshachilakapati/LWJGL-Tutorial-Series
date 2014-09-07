package com.shc.tutorials.lwjgl.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * An implementation of a 6DOF Camera using Quaternions
 *
 * @author Sri Harsha Chilakapati
 */
public class Camera
{
    // Axes for reference
    public static final Vector3f AXIS_X = new Vector3f(1, 0, 0);
    public static final Vector3f AXIS_Y = new Vector3f(0, 1, 0);
    public static final Vector3f AXIS_Z = new Vector3f(0, 0, 1);

    // Position of the Camera and its Orientation
    private Vector3f   position;
    private Quaternion orientation;

    // The projection and view matrices
    private Matrix4f projection;
    private Matrix4f view;

    // Local axes (relative to the Camera)
    private Vector3f up;
    private Vector3f forward;
    private Vector3f right;

    // Projection and View matrix buffers
    private FloatBuffer projBuffer;
    private FloatBuffer viewBuffer;

    /**
     * Direction enumeration with all possible directions
     */
    public static enum Direction
    {
        FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN
    }

    /**
     * Create a camera by using a Perspective Projection matrix.
     *
     * @param fov    The vertical field of view (fovY)
     * @param aspect The aspect ratio of the viewport
     * @param zNear  The near depth clipping plane
     * @param zFar   The far depth clipping plane
     */
    public Camera(float fov, float aspect, float zNear, float zFar)
    {
        // Default position is the origin
        position    = new Vector3f();
        orientation = new Quaternion();

        // Create the default local axes
        up      = new Vector3f(AXIS_Y);
        forward = new Vector3f(AXIS_Z.negate(null));
        right   = new Vector3f(AXIS_X);

        // Create projection and view matrices
        projection = MatrixUtil.createPerspective(fov, aspect, zNear, zFar);
        view       = new Matrix4f();

        // Create the projection and view matrix buffers
        projBuffer = BufferUtils.createFloatBuffer(16);
        viewBuffer = BufferUtils.createFloatBuffer(16);

        // Store the projection matrix in buffer
        projection.store(projBuffer);
        projBuffer.rewind();
    }

    /**
     * Rotates the scene on the x-axis with the specified
     * angle in the anti-clockwise direction.
     *
     * @param angle The angle to rotate (in degrees)
     */
    public void rotateX(float angle)
    {
        Quaternion xRot = QuaternionUtil.createFromAxisAngle(AXIS_X, angle, null);
        Quaternion.mul(xRot, orientation, orientation);

        QuaternionUtil.rotate(up, xRot, up);
        QuaternionUtil.rotate(forward, xRot, forward);

        up.normalise();
        forward.normalise();
    }

    /**
     * Rotates the scene on the y-axis with the specified
     * angle in the anti-clockwise direction.
     *
     * @param angle The angle to rotate (in degrees)
     */
    public void rotateY(float angle)
    {
        Quaternion yRot = QuaternionUtil.createFromAxisAngle(AXIS_Y, angle, null);
        Quaternion.mul(yRot, orientation, orientation);

        QuaternionUtil.rotate(forward, yRot, forward);
        QuaternionUtil.rotate(right, yRot, right);

        forward.normalise();
        right.normalise();
    }

    /**
     * Rotates the scene on the z-axis with the specified
     * angle in the anti-clockwise direction.
     *
     * @param angle The angle to rotate (in degrees)
     */
    public void rotateZ(float angle)
    {
        Quaternion zRot = QuaternionUtil.createFromAxisAngle(AXIS_Z, angle, null);
        Quaternion.mul(zRot, orientation, orientation);

        QuaternionUtil.rotate(up, zRot, up);
        QuaternionUtil.rotate(right, zRot, right);

        up.normalise();
        right.normalise();
    }

    /**
     * Updates the viewing matrix.
     */
    public void update()
    {
        // Rotate the scene and translate the world back
        QuaternionUtil.toRotationMatrix(orientation, view);
        Matrix4f.translate(position.negate(null), view, view);

        // Store the view matrix in the buffer
        view.store(viewBuffer);
        viewBuffer.rewind();
    }

    /**
     * Convenience method to move the camera in all the possible
     * directions up, down, left, right, forward and backward.
     *
     * @param dir    The direction to move in
     * @param amount The amount of distance to move
     */
    public void move(Direction dir, float amount)
    {
        switch (dir)
        {
            case FORWARD:  move(forward, +amount); break;
            case BACKWARD: move(forward, -amount); break;
            case LEFT:     move(right,   -amount); break;
            case RIGHT:    move(right,   +amount); break;
            case UP:       move(up,      +amount); break;
            case DOWN:     move(up,      -amount); break;
        }
    }

    /**
     * Moves the camera in a direction represented by a
     * directional vector by the amount of distance.
     *
     * @param dir    The direction to move in
     * @param amount The amount of distance to move
     */
    public void move(Vector3f dir, float amount)
    {
        // Create a copy of direction
        Vector3f deltaMove = new Vector3f(dir);

        // Normalise the direction and scale it by amount
        deltaMove.normalise();
        deltaMove.scale(amount);

        // Add the delta to the camera's position
        Vector3f.add(position, deltaMove, position);
    }

    /**
     * @return The position of this vector
     */
    public Vector3f getPosition()
    {
        return position;
    }

    /**
     * @return The View Matrix buffer
     */
    public FloatBuffer getViewBuffer()
    {
        return viewBuffer;
    }

    /**
     * @return The Projection matrix buffer
     */
    public FloatBuffer getProjectionBuffer()
    {
        return projBuffer;
    }

    /**
     * Sets the position of the camera
     *
     * @param position The new position of this camera
     */
    public void setPosition(Vector3f position)
    {
        this.position = position;
    }
}
