varying vec3 normal;
varying vec2 texture_coordinate;

uniform float time;

uniform mat4 modelMatrix;

void main()
{

	gl_Position = gl_ModelViewProjectionMatrix*modelMatrix*gl_Vertex;

    normal = gl_NormalMatrix * gl_Normal;
    texture_coordinate = vec2(gl_MultiTexCoord0);

}