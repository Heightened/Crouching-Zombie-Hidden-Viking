#version 330

attribute vec4 in_position;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{
	gl_Position = projectionMatrix*viewMatrix*modelMatrix*in_position;
}