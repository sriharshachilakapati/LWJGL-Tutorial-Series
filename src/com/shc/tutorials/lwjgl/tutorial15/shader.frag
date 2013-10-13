// The Vertex Color
varying vec4 vColor;
// The Vertex Position
varying vec3 vPosition;
// The Vertex Normal
varying vec3 vNormal;

// The Position of light
uniform vec3 lightPos;

// The Color of light
const vec3 lightColor = vec3(1.0, 1.0, 1.0);
// Intensity of light
const float lightIntensity = 2.0;
// The ambientCoefficient
const float ambientCoefficient = 0.05;
// Shinines of the model
const float shininess = 128.0;

void main()
{
    // Vector from surface to light
    vec3 surfaceToLight = normalize(lightPos - vPosition);
    
    // ambient light
    vec3 ambient = ambientCoefficient * vColor.rgb * lightIntensity;

    // diffuse light
    float diffuseCoefficient = max(0.0, dot(vNormal, surfaceToLight));
    vec3 diffuse = diffuseCoefficient * vColor.rgb * lightIntensity;
    
    // specular light
    float specularCoefficient = 0.0;
    if(diffuseCoefficient > 0.0)
    {
        specularCoefficient = pow(max(0.0, dot(surfaceToLight, reflect(-surfaceToLight, vNormal))), shininess);
    }
    vec3 specular = specularCoefficient * vec3(1.0, 1.0, 1.0) * lightIntensity;
    
    // Send the color to opengl
    gl_FragColor = vColor + vec4(ambient, 1.0) + vec4(diffuse, 1.0) * vec4(lightColor, 1.0) + vec4(specular, 1.0);
}