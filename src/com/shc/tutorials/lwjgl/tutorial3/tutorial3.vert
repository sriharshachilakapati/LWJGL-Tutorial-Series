#version 330 core

// Inputs from OpenGL
layout(location = 0) in vec2 pos;
layout(location = 1) in vec4 col;

// The color to pass to the fragment shader
out vec4 passColor;

void main()
{
    gl_Position = vec4(pos, 0.0, 1.0);
    
    // Pass the color
    passColor = col;
}