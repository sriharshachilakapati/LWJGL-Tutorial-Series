package com.shc.tutorials.lwjgl.util.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import com.shc.tutorials.lwjgl.util.FileUtil;

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
    // List of faces
    private List<Face> faces;
    // Map of materials
    private HashMap<String, Material> materials;

    // VBO handles
    int vboVertexID;
    int vboNormalID;
    int vboColorID;

    /**
     * A private constructor just to initialize variables.
     */
    private Model()
    {
        vertices = new ArrayList<Vector3f>();
        normals = new ArrayList<Vector3f>();
        faces = new ArrayList<Face>();
        materials = new HashMap<String, Material>();
    }

    /**
     * Loads a 3D Model from a Wavefront (.OBJ) file.
     * 
     * @param name The name of the file
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

                // Create the face
                m.getFaces().add(new Face(vertex, normal, material));
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

        // Create the buffers
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9 * faces.size());
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9 * faces.size());
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(9 * faces.size());

        // Iterate over each face in the model and add them to the VBO
        for (Face face : faces)
        {
            // Retrieve the material of the face
            Material material = face.getMaterial();

            // Get the first vertex of the face
            Vector3f v1 = vertices.get((int) face.getVertex().x - 1);
            vertexBuffer.put(v1.x).put(v1.y).put(v1.z);
            // Get the color of the vertex
            colorBuffer.put(material.getDiffuse().x)
                       .put(material.getDiffuse().y)
                       .put(material.getDiffuse().z);

            // Get the second vertex of the face
            Vector3f v2 = vertices.get((int) face.getVertex().y - 1);
            vertexBuffer.put(v2.x).put(v2.y).put(v2.z);
            // Get the color of the face
            colorBuffer.put(material.getDiffuse().x)
                       .put(material.getDiffuse().y)
                       .put(material.getDiffuse().z);

            // Get the third vertex of the face
            Vector3f v3 = vertices.get((int) face.getVertex().z - 1);
            vertexBuffer.put(v3.x).put(v3.y).put(v3.z);
            // Get the color of the face
            colorBuffer.put(material.getDiffuse().x)
                       .put(material.getDiffuse().y)
                       .put(material.getDiffuse().z);

            // Get the first normal of the face
            Vector3f n1 = normals.get((int) face.getNormal().x - 1);
            normalBuffer.put(n1.x).put(n1.y).put(n1.z);

            // Get the second normal of the face
            Vector3f n2 = normals.get((int) face.getNormal().y - 1);
            normalBuffer.put(n2.x).put(n2.y).put(n2.z);

            // Get the third normal of the face
            Vector3f n3 = normals.get((int) face.getNormal().z - 1);
            normalBuffer.put(n3.x).put(n3.y).put(n3.z);
        }

        // Rewind the buffers
        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer.rewind();

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

}
