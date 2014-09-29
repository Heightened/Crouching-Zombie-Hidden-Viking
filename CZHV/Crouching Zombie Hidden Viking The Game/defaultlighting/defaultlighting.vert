#version 330

out vec2 texture_coordinate;
out vec3 normal;

uniform float time;

out float shading;

out float factor;

out vec4 worldspacePos;
out vec3 tangents;

in vec4 in_position;
in vec3 in_normal;
in vec2 in_texcoord;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{

	worldspacePos = modelMatrix*in_position;
	gl_Position = projectionMatrix*viewMatrix*worldspacePos;

	normal =  mat3(modelMatrix)*in_normal;
	tangents =  mat3(modelMatrix)*vec3(1,0,0);

  	texture_coordinate = vec2(in_texcoord);
}