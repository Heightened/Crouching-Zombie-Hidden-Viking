#version 330

varying vec3 normal;
varying vec2 texture_coordinate;
varying vec4 worldPosition;

uniform float time;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

attribute vec4 in_position;
attribute vec3 in_normal;
attribute vec2 in_texcoord;

void main()
{
    worldPosition = modelMatrix*in_position;
	gl_Position = projectionMatrix*viewMatrix*worldPosition;

    normal = gl_NormalMatrix * in_normal;
    texture_coordinate = vec2(in_texcoord);

}