#version 330

varying vec3 normal;
varying vec2 texture_coordinate;

uniform vec4 color;


void main()
{
    gl_FragColor = color;
}