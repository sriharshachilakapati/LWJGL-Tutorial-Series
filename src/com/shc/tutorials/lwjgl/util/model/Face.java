package com.shc.tutorials.lwjgl.util.model;

import org.lwjgl.util.vector.Vector3f;

/**
 * A Face in a Model. Each face is a triangle.
 *  
 * @author Sri Harsha Chilakapati
 */
public class Face
{

    // Vertex indices
    private Vector3f vertex;
    // Normal indices
    private Vector3f normal;
    // The Material
    private Material material;

    /**
     * Create a new Face with vertex indices, normal indices and material
     */
    public Face(Vector3f vertex, Vector3f normal, Material material)
    {
        this.vertex = vertex;
        this.normal = normal;
        this.material = material;

        if (material == null)
        {
            // If there is no material, create a default one
            material = new Material("Default");
        }
    }

    /**
     * @return The vertex indices
     */
    public Vector3f getVertex()
    {
        return vertex;
    }

    /**
     * @return The normal indices
     */
    public Vector3f getNormal()
    {
        return normal;
    }

    /**
     * @return The material of the face
     */
    public Material getMaterial()
    {
        return material;
    }

}
