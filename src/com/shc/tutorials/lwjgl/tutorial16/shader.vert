// The Vertex Color
varying vec4 vColor;

// The view matrix
uniform mat4 mView;
// The projection matrix
uniform mat4 mProjection;
// The normal matrix
uniform mat4 mNormal;

void main()
{
    // Pass the color to the fragment shader
    vColor = gl_Color;
    
    // Pass the texture coords
    gl_TexCoord[0] = gl_MultiTexCoord0;
    
    // Calculate the transformed vertex
    gl_Position = mProjection * mView * gl_Vertex;
}