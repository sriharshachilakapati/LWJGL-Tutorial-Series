package com.shc.tutorials.lwjgl.util.model;

import org.lwjgl.util.vector.Vector3f;

/**
 * A simple material class
 * 
 * @author Sri Harsha Chilakapati
 */
public class Material
{

    // The diffuse color
    private Vector3f diffuse;
    // Name of the material
    private String name;

    /**
     * Creates a new material with a name
     */
    public Material(String name)
    {
        diffuse = new Vector3f(1, 1, 1);
        this.name = name;
    }

    /**
     * @return The diffuse color of this material
     */
    public Vector3f getDiffuse()
    {
        return diffuse;
    }

    /**
     * Set the diffuse color
     */
    public void setDiffuse(Vector3f diffuse)
    {
        this.diffuse = diffuse;
    }

    /**
     * @return The name of the material
     */
    public String getName()
    {
        return name;
    }

}
