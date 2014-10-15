#version 330

uniform vec4 color;

uniform sampler2D texture;

in vec2 texture_coordinate;
out vec4 texelColor;

void main()
{
    texelColor = texture2D(texture, texture_coordinate);
    //texelColor = vec4(texture_coordinate,1,1);
}