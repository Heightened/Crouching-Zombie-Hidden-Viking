#version 330

uniform sampler2D colorTexture;
uniform sampler2D bloomTexture;

in vec2 texture_coordinate; 

out vec4 texelColor;

void main() 
{ 
	texelColor = texture2D(colorTexture, texture_coordinate) + texture2D(bloomTexture, texture_coordinate);
	texelColor.a = 1;
}