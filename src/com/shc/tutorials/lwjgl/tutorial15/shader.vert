// The Vertex Color
varying vec4 vColor;
// The Vertex Position
varying vec3 vPosition;
// The Vertex Normal
varying vec3 vNormal;

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
    
    // Pass the vertex to the fragment shader
    vPosition = (mView * gl_Vertex).xyz;
    
    // Pass the normal to the fragment shader
    vNormal = normalize(mNormal * vec4(gl_Normal, 1.0)).xyz;
    
    // Calculate the transformed vertex
    gl_Position = mProjection * mView * gl_Vertex;
}