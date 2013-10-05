uniform mat4 projection;
uniform mat4 view;

varying vec4 vColor;

void main()
{
    vColor = gl_Color;
    gl_Position = projection * view * gl_Vertex;
}