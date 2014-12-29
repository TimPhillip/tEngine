#version 400 core

in vec2 pass_texCoord;
in vec3 worldNormal;
in vec3 toLightDir[20];
in float lightDistance[20];

out vec4 out_color;

uniform sampler2D textureSampler;
uniform float isGlowing;
uniform vec3 matColor;

//Light
uniform vec3 lightColor[20];
uniform float lightRange[20];

vec3 ComputePointLight(int i,vec3 uNormal)
{
	vec3 light;
	if(lightDistance[i] > 5){
	light = vec3(0,0,0);}else{
	vec3 unitLight = normalize(toLightDir[i]);
	float lightIntensity = dot(uNormal,unitLight);
	lightIntensity = max(lightIntensity,0.01f);
	light = (lightIntensity * lightColor[i]);}
	return light;
}

void main(void)
{
	vec4 texColor = texture(textureSampler,pass_texCoord);
	if(texColor.a < 0.5)
		discard;
	if(isGlowing == 0){
		vec3 diffuseLight = vec3(0,0,0);
		vec3 unitNormal = normalize(worldNormal);
		for(int i =0; i <20; i++){
			diffuseLight += ComputePointLight(i,unitNormal);				
		}
		out_color = texColor * vec4(matColor,1.0) * vec4(diffuseLight,1.0f);
	}else
	{
		out_color = texColor * vec4(matColor,1.0);
	}
}

