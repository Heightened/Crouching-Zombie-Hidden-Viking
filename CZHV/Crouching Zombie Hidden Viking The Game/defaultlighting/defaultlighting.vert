#version 330

varying vec2 texture_coordinate;
varying vec3 normal;

uniform float time;

varying float shading;

varying float factor;

varying vec4 worldspacePos;
varying vec3 tangents;

attribute vec4 in_position;
attribute vec3 in_normal;
attribute vec2 in_texcoord;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{

	worldspacePos = modelMatrix*in_position;
	gl_Position = projectionMatrix*viewMatrix*worldspacePos;

	normal =  in_normal;
	tangents =  mat3(modelMatrix)*vec3(1,0,0);

  	texture_coordinate = vec2(in_texcoord);
}