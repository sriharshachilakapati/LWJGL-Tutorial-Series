#version 330 core

// Color which was passed
in vec4 passColor;

// Output color
out vec4 color;

void main()
{
    color = passColor;
}