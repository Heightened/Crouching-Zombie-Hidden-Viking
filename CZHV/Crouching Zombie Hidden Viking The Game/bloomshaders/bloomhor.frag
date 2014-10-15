#version 330

uniform sampler2D texture;

in vec2 texture_coordinate; 

float offset[4] = float[4]( 0.0, 1.3, 3 ,4.7);
float weight[4] = float[4]( 0.2270270270, 0.3162162162, 0.0702702703 , 0.07);

out vec4 texelColor;

uniform float size;

void main() 
{ 
  
    vec2 uv = texture_coordinate; 
    vec4 tc = texture2D(texture, uv) * weight[0];
    for (int i=1; i<4; i++) 
    {
      tc += texture2D(texture, uv + vec2(offset[i],0.0)/size).rgba  * weight[i];
      tc += texture2D(texture, uv - vec2(offset[i],0.0)/size).rgba * weight[i];
    }
  
  texelColor = tc.rgba;
  texelColor.a = 1;

}