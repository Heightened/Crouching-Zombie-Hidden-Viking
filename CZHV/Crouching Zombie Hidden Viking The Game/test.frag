#version 330

varying vec3 normal;
varying vec2 texture_coordinate;

uniform sampler2D texture;

uniform float time;


void main()
{
    vec2 texTemp = texture_coordinate;
    texTemp.x += time;
    gl_FragColor = texture2D(texture, texTemp);
}