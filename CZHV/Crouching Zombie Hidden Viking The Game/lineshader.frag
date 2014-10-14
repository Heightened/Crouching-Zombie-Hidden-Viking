#version 330

in vec4 gradientColor;

out vec4 texelColor;

void main()
{
    texelColor = gradientColor;
}