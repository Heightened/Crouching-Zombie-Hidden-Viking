#version 330

uniform vec4 color;

out vec4 texelColor;

void main()
{
    texelColor = color;
}