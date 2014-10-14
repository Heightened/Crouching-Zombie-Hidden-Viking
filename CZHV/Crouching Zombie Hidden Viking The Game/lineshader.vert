#version 330

uniform vec4 quadSize;

in vec4 in_position;
in vec3 in_normal;
in vec2 in_texcoord;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec4 startPos;
uniform vec4 endPos;

uniform vec4 color;

varying vec4 gradientColor;

void main()
{
	vec4 finalpos = vec4(0,0,0,1);
    finalpos.x = in_position.x*startPos.x + in_position.y*endPos.x;
    finalpos.y = in_position.x*startPos.y + in_position.y*endPos.y;
    finalpos.z = in_position.x*startPos.z + in_position.y*endPos.z;

    gradientColor = color*in_position.x;
    gradientColor.w = 1;

	gl_Position = projectionMatrix*viewMatrix*finalpos;

}