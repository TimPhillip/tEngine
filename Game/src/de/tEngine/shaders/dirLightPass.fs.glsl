#version 400 core

uniform sampler2D gBufferPosition;
uniform sampler2D gBufferDiffuse;
uniform sampler2D gBufferNormal;

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
	float k = max(dot(lightDirection * -1,Normal),0);
	
	//FragColor = (k + 0.25) * vec4(lightColor,1) * vec4(texture(gBufferDiffuse,TexCoord).xyz,1) * lightIntensity;
    FragColor = vec4(1,0,0,1);
}

