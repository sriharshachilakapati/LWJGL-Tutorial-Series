// The color of the vertex
varying vec4 vColor;

// The texture
uniform sampler2D texture;

void main()
{
    // Calculate the texture color
    vec4 texColor = texture2D(texture, gl_TexCoord[0].st);
    
    // Combine the texture color and the vertex color
    gl_FragColor = vec4(texColor.rgb * vColor.rgb, texColor.a * vColor.a);
}