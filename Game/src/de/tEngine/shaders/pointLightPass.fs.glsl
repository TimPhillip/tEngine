#version 400 core

uniform sampler2D gBufferPosition;
uniform sampler2D gBufferDiffuse;
uniform sampler2D gBufferNormal;

uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform vec3 lightAttenuation;
uniform float lightIntensity;

out vec4 FragColor;

vec2 CalcTexCoord()
{
	return gl_FragCoord.xy / vec2(1280,720);
}

void main(void)
{
	vec2 TexCoord = CalcTexCoord();
	vec3 toLightVec = lightPosition - texture(gBufferPosition,TexCoord).xyz;
	float d = length(toLightVec);
	toLightVec = normalize(toLightVec);
	vec3 Normal = texture(gBufferNormal,TexCoord).xyz;
	float k = max(dot(toLightVec,Normal),0);
	
	FragColor = k * vec4(lightColor,1) * vec4(texture(gBufferDiffuse,TexCoord).xyz,1) * lightIntensity / (lightAttenuation.x + lightAttenuation.y * d + lightAttenuation.z * d *d);
}

