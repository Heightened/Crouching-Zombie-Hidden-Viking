#version 330

varying vec3 normal;
varying vec2 texture_coordinate;

uniform float time;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

attribute vec4 in_position;
attribute vec3 in_normal;
attribute vec2 in_texcoord;

void main()
{

	gl_Position = projectionMatrix*viewMatrix*modelMatrix*in_position;

    normal = gl_NormalMatrix * in_normal;
    texture_coordinate = vec2(in_texcoord);

}