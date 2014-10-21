#version 330

uniform sampler2D depthtex;

in vec3 lightVec;
in vec3 eyeVec;
in vec3 normal;

in vec2 texture_coordinate; 

uniform float time;

in float shading;

in float factor;

in vec4 worldspacePos;
uniform vec4 eyeposition;
uniform vec4 color;

in vec3 tangents;

uniform sampler2D shadowMap;
uniform mat4 shadowMVP;
uniform mat4 shadowProjectionMatrix;
uniform mat4 biasMatrix;
uniform vec4 gridOffset;

out vec4 texelColor;

void main(void)
{   
    float depthfromtex = texture2D(depthtex, gl_FragCoord.xz).r;
    float thisdepth = gl_FragCoord.y;
    texelColor = vec4(color.rgb,1);
}