#version 330

varying vec3 normal;
varying vec2 texture_coordinate;
varying vec4 worldPosition;

uniform sampler2D texture;
uniform vec4 color;

uniform float time;


void main()
{
    vec2 texTemp = texture_coordinate;
    texTemp.x += time;
    gl_FragColor = texture2D(texture, texTemp);
    gl_FragColor *= color;
    gl_FragColor.r = worldPosition.x;
    gl_FragColor.g = worldPosition.z;
}