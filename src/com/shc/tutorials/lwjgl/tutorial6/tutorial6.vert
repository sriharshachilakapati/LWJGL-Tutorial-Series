#version 330 core

// Model, View and Projection Matrices
uniform mat4 m_model;
uniform mat4 m_view;
uniform mat4 m_proj;

// Inputs for the position and color
layout(location = 0) in vec3 pos;
layout(location = 1) in vec4 col;

// Output color to pass to the fragment shader
out vec4 color;

void main()
{
    // Pass the color
    color = col;
    
    // Transform the vertex position
    gl_Position =  m_proj * m_view * m_model * vec4(pos, 1.0);
}