#version 330

uniform sampler2D texture;
uniform sampler2D normsamp;
uniform sampler2D specsamp;

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

struct Light
{
  vec4 worldpos;
  vec4 color;//3color + 1 cutoffangle ( -1 for point lights);
  vec4 speccolor;//3color + 1 shadowmap index (0-15)
  //TODO: upload shadow map matrices in uniform buffer,
  //transform worldspacePos with this matrix and compare to shadow map(w divide for perspective lights)
  //for interactive shadows. use 4 matrices or simply choose "brightest" light
  vec4 radius;//1 radius 3 spotdirection
};

layout(std140) uniform lightblock{
    Light lights[128];
};
layout(std140) uniform indexblock{
    vec4 indices[256];
};
//layout(std140) uniform shadowblock{
//    mat4 shadowMVP[16];
//};
float LinearizeDepth(float d){
    float n = 1; // camera z near
    float f = 300.0; // camera z far
    return (2.0 * n) / (f + n - d * (f - n));
}

void main(void)
{   
    mat4 shadowMatrix = biasMatrix*shadowProjectionMatrix*shadowMVP;
    vec4 shadowVertex = shadowMatrix*worldspacePos;
    shadowVertex = shadowVertex/shadowVertex.w;
    float shadowMapDepth = texture2D(shadowMap, shadowVertex.xz ).r;
    float shadow = 1;
    shadow = (shadowMapDepth - shadowVertex.y + 0.00001)*10000000 + 0.5;
    shadow = clamp(shadow,0,1);

   // shadow = LinearizeDepth(shadowMapDepth);



    int cellx = int((worldspacePos.x-gridOffset.x)/0.25);
    int cellz = int((worldspacePos.z-gridOffset.z)/0.25);
    float temp = float((cellx*cellz))/256;
    vec4 lightindex = indices[cellx+cellz*16];
    
    vec3 Tangent = normalize(tangents);
    Tangent = normalize(Tangent - dot(Tangent, normal) * normal);
    vec3 Bitangent = cross(Tangent, normal);
   // Light lights[3] = Light[3](
   //   Light(vec4(50,20,0,0), vec4(1,0,0,0),vec4(0.75,0,0,0),vec4(150.0,0,0,0)),
   //   Light(vec4(50,20,50,0), vec4(0,0,1,0),vec4(0,0,0.75,0),vec4(100.0,0,0,0)),
   //   Light(vec4(50,20,-50,0), vec4(1,1,0,0),vec4(0.75,0.75,0,0),vec4(100.0,0,0,0)));
      
   // lights[0] = lights2[0];//dummy
	vec4 final_color = vec4(0,0,0,1);//dummy
    vec4 eyevec = normalize(eyeposition - worldspacePos);
    vec3 BumpMapNormal = texture2D(normsamp,texture_coordinate).rgb*2 - 1;//texture2D(normsamp,texture_coordinate);
    BumpMapNormal = vec3(0,1,0);
	mat3 TBN = mat3(Tangent, Bitangent, normal);
    vec3 N = TBN * BumpMapNormal;
    N = normalize(normal);
    vec3 E = normalize(eyevec.rgb);
    
   // float spec = texture2D(specsamp,texture_coordinate).r;
    
    for (int i = 0; i < 4; i++){
        Light light = lights[int(lightindex[i])];
        vec4 lightvec = light.worldpos - worldspacePos;
        lightvec.w = 0;
        float distToLight = length(lightvec);
        float diffuseDist = 1 - smoothstep(light.radius.r/2, light.radius.r, distToLight);
        float specDist = 1 - smoothstep(light.radius.r, light.radius.r*1.2, distToLight);
        vec3 L = normalize(lightvec.rgb);
        float lambertTerm = max(0,dot(N,L));
        if (int(lightindex[i]) == 1){
          lambertTerm = min(shadow, lambertTerm);
        }
        //spotlights
        float spotEffect =  dot(normalize(light.radius.gba), normalize(-L));
        float spotfactordiffuse = smoothstep(light.color.a-0.1,light.color.a,spotEffect);
        final_color.rgb += light.color.rgb*lambertTerm*spotfactordiffuse*diffuseDist;
        
        vec3 H = normalize( L + E );//half vector
        float NdotH = dot( N, H );//blinn spec
        float specular = pow(max(NdotH, 0.0),15);
        final_color.rgb += light.speccolor.rgb*specular*min(1,lambertTerm)*spotfactordiffuse*diffuseDist;;
    }	

	texelColor = texture2D(texture,texture_coordinate);
    texelColor = vec4(final_color.rgb*color.rgb,1);
    //texelColor = vec4(shadow, shadow, shadow,1);
    //texelColor = vec4(final_color.rgb,1);
    //texelColor = vec4(color.rgb,1);
    //texelColor.r = lights[1].color.a;
  //texelColor.g = color.r;
   // texelColor.b = 0;
}