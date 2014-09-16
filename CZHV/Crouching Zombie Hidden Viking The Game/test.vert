varying vec3 normal;
varying vec2 texture_coordinate;

uniform float time;

void main()
{

	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;

    normal = gl_NormalMatrix * gl_Normal;
    texture_coordinate = vec2(gl_MultiTexCoord0);

}