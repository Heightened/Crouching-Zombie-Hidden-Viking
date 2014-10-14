#version 330

varying vec4 gradientColor;

out vec4 texelColor;

void main()
{
    texelColor = gradientColor;
}