#version 330

uniform vec4 quadSize;

out vec2 texture_coordinate;

in vec4 in_position;
in vec3 in_normal;
in vec2 in_texcoord;

void main()
{
	gl_Position = in_position;
    gl_Position.x *= quadSize.z;
    gl_Position.y *= quadSize.w;
    
    gl_Position.x += quadSize.x;
    gl_Position.y += quadSize.y;

    texture_coordinate = in_texcoord;
}