#version 330

uniform sampler2D colorTexture;
uniform sampler2D bloomTexture;

in vec2 texture_coordinate; 

out vec4 texelColor;

float LinearizeDepth(float d){
    float n = 1; // camera z near
    float f = 20.0; // camera z far
    return (2.0 * n) / (f + n - d * (f - n));
}

void main() 
{ 
	texelColor = texture2D(colorTexture, texture_coordinate) + texture2D(bloomTexture, texture_coordinate);
	//float depth = LinearizeDepth(texture2D(colorTexture, texture_coordinate).r);
	//texelColor = vec4(depth,depth,depth,depth);
	texelColor.a = 1;
}