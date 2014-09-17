#version 330

varying vec3 normal;
varying vec2 texture_coordinate;

uniform vec4 quadSize;

attribute vec4 in_position;
attribute vec3 in_normal;
attribute vec2 in_texcoord;

void main()
{
	gl_Position = in_position;
    gl_Position.x *= quadSize.z;
    gl_Position.y *= quadSize.w;
    
    gl_Position.x += quadSize.x;
    gl_Position.y += quadSize.y;

    normal = gl_NormalMatrix * in_normal;
    texture_coordinate = vec2(in_texcoord);

}