package com.shc.tutorials.lwjgl.util;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * A simple 3D Camera class
 * 
 * @author Sri Harsha Chilakapati
 */
public class Camera
{
    // Field Of View
    private float fov;
    // Aspect Ratio
    private float aspect;
    // Near Plane
    private float zNear;
    // Far Plane
    private float zFar;

    // Projection matrix
    private Matrix4f projection;
    // View matrix
    private Matrix4f view;

    // Camera position
    private Vector3f position;
    // Camera rotation
    private Vector3f rotation;

    // Vectors for axes
    private Vector3f xAxis, yAxis, zAxis;

    /**
     * Creates a simple 3D Perspective Camera.
     * 
     * @param fov The field of view in degrees.
     * @param aspect The aspect ratio.
     * @param zNear The near clipping plane.
     * @param zFar The far clipping plane.
     */
    public Camera(float fov, float aspect, float zNear, float zFar)
    {
        // Set the local variables
        this.fov = fov;
        this.aspect = aspect;
        this.zNear = zNear;
        this.zFar = zFar;
        
        // Create matrices
        projection = MatrixUtil.createPerspectiveProjection(fov, aspect, zNear, zFar);
        view = MatrixUtil.createIdentityMatrix();

        // Initialize position and rotation vectors
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);

        // Create normalized axis vectors
        xAxis = new Vector3f(1, 0, 0);
        yAxis = new Vector3f(0, 1, 0);
        zAxis = new Vector3f(0, 0, 1);

        // Enable depth testing
        glEnable(GL_DEPTH_TEST);
    }

    /**
     * Apply the camera's transformations.
     */
    public void apply()
    {
        // Make the view matrix an identity.
        view.setIdentity();

        // Rotate the view
        Matrix4f.rotate((float) Math.toRadians(rotation.x), xAxis, view, view);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), yAxis, view, view);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), zAxis, view, view);

        // Move the camera
        Matrix4f.translate(position, view, view);
    }

    /**
     * Add (x, y, z) to the position
     */
    public void addPosition(float x, float y, float z)
    {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    /**
     * Add a position vector to the camera position
     */
    public void addPosition(Vector3f position)
    {
        addPosition(position.x, position.y, position.z);
    }

    /**
     * Add rotation (rx, ry, rz) to the camera
     */
    public void addRotation(float rx, float ry, float rz)
    {
        rotation.x += rx;
        rotation.y += ry;
        rotation.z += rz;
    }

    /**
     * Add a rotation vector to the camera rotation
     */
    public void addRotation(Vector3f rotation)
    {
        addRotation(rotation.x, rotation.y, rotation.z);
    }

    /**
     * Move the camera with an amount in a direction.
     */
    public void move(float amount, float direction)
    {
        position.z += amount * Math.sin(Math.toRadians(rotation.y + 90 * direction));
        position.x += amount * Math.cos(Math.toRadians(rotation.y + 90 * direction));
    }

    /**
     * Set the position of this Camera to (x, y, z)
     */
    public void setPosition(float x, float y, float z)
    {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    /**
     * Set the rotation of this camera to (rx, ry, rz)
     */
    public void setRotation(float rx, float ry, float rz)
    {
        rotation.x = rx;
        rotation.y = ry;
        rotation.z = rz;
    }

    /**
     * @return the field of view
     */
    public float getFieldOfView()
    {
        return fov;
    }

    /**
     * @return the aspect ratio
     */
    public float getAspectRatio()
    {
        return aspect;
    }

    /**
     * @return the distance of near plane
     */
    public float getNearPlane()
    {
        return zNear;
    }

    /**
     * @return the distance of far plane
     */
    public float getFarPlane()
    {
        return zFar;
    }

    /**
     * @return the projection matrix
     */
    public Matrix4f getProjectionMatrix()
    {
        return projection;
    }

    /**
     * @return the view matrix
     */
    public Matrix4f getViewMatrix()
    {
        return view;
    }

    /**
     * @return the position
     */
    public Vector3f getPosition()
    {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    /**
     * @return the rotation
     */
    public Vector3f getRotation()
    {
        return rotation;
    }

    /**
     * @param rotation the rotation to set
     */
    public void setRotation(Vector3f rotation)
    {
        this.rotation = rotation;
    }

}