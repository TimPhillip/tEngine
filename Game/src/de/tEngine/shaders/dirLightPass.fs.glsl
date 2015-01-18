#version 400 core

uniform sampler2D gBufferPosition;
uniform sampler2D gBufferDiffuse;
uniform sampler2D gBufferNormal;

uniform sampler2D shadowMap;
uniform mat4 lightViewProj;

uniform vec3 lightDirection;
uniform vec3 lightColor;
uniform float lightIntensity;

out vec4 FragColor;

vec2 CalcTexCoord()
{
	return gl_FragCoord.xy / vec2(1280,720);
}

void main(void)
{
	vec2 TexCoord = CalcTexCoord();
	vec3 Normal = texture(gBufferNormal,TexCoord).xyz;
	vec3 Position = texture(gBufferPosition,TexCoord).xyz;
	float k = max(dot(lightDirection * -1,Normal),0);
	
	float shadowFactor = 1.0f;
	vec4 LightSpacePos = lightViewProj * vec4(Position,1.0f);
	vec3 LightCoords = LightSpacePos.xyz / LightSpacePos.w;
	
	LightCoords = LightCoords * 0.5f + vec3(0.5f);
	float Depth = texture(shadowMap,LightCoords.xy).x;
	if(Depth < (LightCoords.z)){
		shadowFactor = 0.5f;
	}
	
	FragColor = (k + 0.25) *shadowFactor * vec4(lightColor,1) * vec4(texture(gBufferDiffuse,TexCoord).xyz,1) * lightIntensity;
}

