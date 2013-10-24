package com.shc.tutorials.lwjgl.util.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.shc.tutorials.lwjgl.util.FileUtil;
import com.shc.tutorials.lwjgl.util.Texture;

/**
 * A simple 3D Model class which can render models as VBOs
 * 
 * @author Sri Harsha Chilakapati
 */
public class Model
{

    // List of vertices
    private List<Vector3f> vertices;
    // List of normals
    private List<Vector3f> normals;
    // List of texture coords
    private List<Vector2f> texCoords;
    // List of faces
    private List<Face> faces;
    // Map of materials
    private HashMap<String, Material> materials;

    // Is this textured model?
    private boolean isTextured = false;

    // The texture of the model
    private Texture texture;

    // VBO handles
    int vboVertexID;
    int vboNormalID;
    int vboColorID;
    int vboTextureID;

    /**
     * A private constructor just to initialize variables.
     */
    private Model()
    {
        vertices = new ArrayList<Vector3f>();
        normals = new ArrayList<Vector3f>();
        texCoords = new ArrayList<Vector2f>();
        faces = new ArrayList<Face>();
        materials = new HashMap<String, Material>();
    }

    /**
     * Loads a 3D Model from a Wavefront (.OBJ) file.
     * 
     * @param name
     *            The name of the file
     * @return The loaded model
     */
    public static Model loadOBJModel(String name)
    {
        // Create an empty model and a null material
        Model m = new Model();
        Material material = null;

        // Read all the lines from the source file
        String[] lines = FileUtil.readAllLines(name);

        // Iterate and parse the lines
        for (String line : lines)
        {

            // If the line starts with "v" then it is a vertex
            if (line.startsWith("v "))
            {
                // Split the line
                String[] values = line.split(" ");

                // Parse the coordinates
                float x = Float.parseFloat(values[1]);
                float y = Float.parseFloat(values[2]);
                float z = Float.parseFloat(values[3]);

                // Create the vertex
                Vector3f vertex = new Vector3f(x, y, z);
                m.getVertices().add(vertex);
            }
            // If the line starts with "vn" then it is a vertex normal
            else if (line.startsWith("vn "))
            {
                // Split the line
                String[] values = line.split(" ");

                // Parse the values
                float x = Float.parseFloat(values[1]);
                float y = Float.parseFloat(values[2]);
                float z = Float.parseFloat(values[3]);

                // Create the normal
                Vector3f normal = new Vector3f(x, y, z);
                m.getNormals().add(normal);
            }
            // If the line starts with "vt" then it is a texture coord
            else if (line.startsWith("vt "))
            {
                // Split the line
                String[] values = line.split(" ");

                // Parse the values
                float x = Float.parseFloat(values[1]);
                float y = Float.parseFloat(values[2]);

                // Create the texCoord
                Vector2f texCoord = new Vector2f(x, y);
                m.getTextureCoords().add(texCoord);

                m.isTextured = true;
            }
            // If the line starts with "f" then it is a face
            else if (line.startsWith("f "))
            {
                // Split the line
                String[] values = line.split(" ");

                // Parse the vertex indices
                float v1 = Float.parseFloat(values[1].split("/")[0]);
                float v2 = Float.parseFloat(values[2].split("/")[0]);
                float v3 = Float.parseFloat(values[3].split("/")[0]);

                // Create the vertex
                Vector3f vertex = new Vector3f(v1, v2, v3);

                // Parse the normal indices
                float vn1 = Float.parseFloat(values[1].split("/")[2]);
                float vn2 = Float.parseFloat(values[2].split("/")[2]);
                float vn3 = Float.parseFloat(values[3].split("/")[2]);

                // Create the normal
                Vector3f normal = new Vector3f(vn1, vn2, vn3);

                if (m.isTextured)
                {
                    // Parse the texture indices
                    float vt1 = Float.parseFloat(values[1].split("/")[1]);
                    float vt2 = Float.parseFloat(values[2].split("/")[1]);
                    float vt3 = Float.parseFloat(values[3].split("/")[1]);

                    Vector3f texCoords = new Vector3f(vt1, vt2, vt3);

                    // Create the face
                    m.getFaces().add(new Face(vertex, normal, texCoords, material));
                }
                else
                {
                    // Create the face
                    m.getFaces().add(new Face(vertex, normal, null, material));
                }
            }
            // If the line starts with "mtllib" then it specifies the file
            // that contains the material definitions.
            else if (line.startsWith("mtllib "))
            {
                // Parse the material file
                parseMaterial(m, FileUtil.getFileInSameLevelOf(name, line.replaceAll("mtllib ", "").trim()));
            }
            // The command "usemtl" states the name of material to
            // use for upcoming faces
            else if (line.startsWith("usemtl "))
            {
                // Get the material
                material = m.getMaterials().get(line.replaceAll("usemtl ", "").trim());
            }
        }

        // Prepare the VBO for the model
        m.prepareVBO();

        // Return the model
        return m;
    }

    /**
     * Parses the material library file
     */
    private static void parseMaterial(Model m, String name)
    {
        // The material to be created
        Material material = null;

        // Read all the lines from the library
        String[] lines = FileUtil.readAllLines(name);

        // Parse the library by iterating over all lines
        for (String line : lines)
        {
            // "newmtl" defines a new material
            if (line.startsWith("newmtl "))
            {
                if (material != null)
                {
                    // Add the previous material to the list
                    m.getMaterials().put(material.getName(), material);
                }

                // Create a new material
                material = new Material(line.split(" ", 2)[1]);
            }
            // "Kd" defines the diffuse color of the material
            else if (line.startsWith("Kd "))
            {
                // Split the line
                String[] values = line.split(" ");

                // Parse the values
                float r = Float.parseFloat(values[1]);
                float g = Float.parseFloat(values[2]);
                float b = Float.parseFloat(values[3]);

                // Create the color
                Vector3f color = new Vector3f(r, g, b);

                // Set the color of the material
                material.setDiffuse(color);
            }
            // "map_Kd" defines a texture file
            else if (line.startsWith("map_Kd "))
            {
                // Load the texture.
                m.setTexture(Texture.loadTexture("resources/textures/" + line.replaceAll("map_Kd", "").trim()));
            }
        }

        // Add the material to the list
        m.materials.put(material.getName(), material);
    }

    /**
     * Prepares a VBO for this model to render
     */
    private void prepareVBO()
    {
        // Create the handles
        vboNormalID = glGenBuffers();
        vboVertexID = glGenBuffers();
        vboColorID = glGenBuffers();

        if (isTextured)
        {
            vboTextureID = glGenBuffers();
        }

        // Create the buffers
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9 * faces.size());
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9 * faces.size());
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(9 * faces.size());

        FloatBuffer textureBuffer = null;
        if (isTextured)
        {
            textureBuffer = BufferUtils.createFloatBuffer(6 * faces.size());
        }

        // Iterate over each face in the model and add them to the VBO
        for (Face face : faces)
        {
            // Retrieve the material of the face
            Material material = face.getMaterial();

            // Get the first vertex of the face
            Vector3f v1 = vertices.get((int) face.getVertex().x - 1);
            vertexBuffer.put(v1.x).put(v1.y).put(v1.z);
            // Get the color of the vertex
            colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);

            // Get the second vertex of the face
            Vector3f v2 = vertices.get((int) face.getVertex().y - 1);
            vertexBuffer.put(v2.x).put(v2.y).put(v2.z);
            // Get the color of the face
            colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);

            // Get the third vertex of the face
            Vector3f v3 = vertices.get((int) face.getVertex().z - 1);
            vertexBuffer.put(v3.x).put(v3.y).put(v3.z);
            // Get the color of the face
            colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);

            // Get the first normal of the face
            Vector3f n1 = normals.get((int) face.getNormal().x - 1);
            normalBuffer.put(n1.x).put(n1.y).put(n1.z);

            // Get the second normal of the face
            Vector3f n2 = normals.get((int) face.getNormal().y - 1);
            normalBuffer.put(n2.x).put(n2.y).put(n2.z);

            // Get the third normal of the face
            Vector3f n3 = normals.get((int) face.getNormal().z - 1);
            normalBuffer.put(n3.x).put(n3.y).put(n3.z);

            if (isTextured)
            {
                // Get the first texCoords of the face
                Vector2f t1 = texCoords.get((int) face.getTexCoord().x - 1);
                textureBuffer.put(t1.x).put(1 - t1.y);

                // Get the second texCoords of the face
                Vector2f t2 = texCoords.get((int) face.getTexCoord().y - 1);
                textureBuffer.put(t2.x).put(1 - t2.y);

                // Get the third texCoords of the face
                Vector2f t3 = texCoords.get((int) face.getTexCoord().z - 1);
                textureBuffer.put(t3.x).put(1 - t3.y);
            }
        }

        // Rewind the buffers
        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer.rewind();

        if (isTextured)
        {
            textureBuffer.rewind();
        }

        // Create the vertex VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create the normal VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalID);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Create the color VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        if (isTextured)
        {
            // Create the texture VBO
            glBindBuffer(GL_ARRAY_BUFFER, vboTextureID);
            glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
    }

    /**
     * Renders this model on the screen
     */
    public void render()
    {
        // Enable client states
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        if (isTextured)
        {
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, texture.id);

            glEnableClientState(GL_TEXTURE_COORD_ARRAY);

            // Bind the texture buffer
            glBindBuffer(GL_ARRAY_BUFFER, vboTextureID);
            glTexCoordPointer(2, GL_FLOAT, 0, 0);
        }

        // Bind the normal buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalID);
        glNormalPointer(GL_FLOAT, 0, 0);

        // Bind the color buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glColorPointer(3, GL_FLOAT, 0, 0);

        // Bind the vertex buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        // Draw all the faces as triangles
        glDrawArrays(GL_TRIANGLES, 0, 9 * faces.size());

        // Disable client states
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);

        if (isTextured)
        {
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        }
    }

    /**
     * Dispose this model
     */
    public void dispose()
    {
        glDeleteBuffers(vboVertexID);
        glDeleteBuffers(vboNormalID);
        glDeleteBuffers(vboColorID);
    }

    /**
     * @return The list of vertices
     */
    public List<Vector3f> getVertices()
    {
        return vertices;
    }

    /**
     * @return The list of normals
     */
    public List<Vector3f> getNormals()
    {
        return normals;
    }

    /**
     * @return The texture coordinates
     */
    public List<Vector2f> getTextureCoords()
    {
        return texCoords;
    }

    /**
     * @return The list of faces
     */
    public List<Face> getFaces()
    {
        return faces;
    }

    /**
     * @return The HashMap of materials
     */
    public HashMap<String, Material> getMaterials()
    {
        return materials;
    }

    /**
     * @return True if texture exists, else False
     */
    public boolean isTextured()
    {
        return isTextured;
    }

    /**
     * @return the texture
     */
    public Texture getTexture()
    {
        return texture;
    }

    /**
     * @param texture
     *            the texture to set
     */
    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

}
