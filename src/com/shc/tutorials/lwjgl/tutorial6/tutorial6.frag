#version 330 core

// Input color
in vec4 color;

// Output color
out vec4 outColor;

void main()
{
    // Pass the color
    outColor = color;
}