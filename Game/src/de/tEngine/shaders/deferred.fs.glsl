#version 400 core

in vec2 pass_texCoord;
in vec3 worldNormal;
in vec3 worldPos;

layout (location = 0) out vec3 PositionOut;
layout (location = 1) out vec3 DiffuseOut;
layout (location = 2) out vec3 NormalOut;

uniform sampler2D textureSampler;

uniform vec3 materialColor;
uniform vec2 materialTiles;

void main(void)
{
	vec4 texColor = texture(textureSampler,pass_texCoord * materialTiles) * vec4(materialColor,1);
	if(texColor.a < 0.5)
		discard;
	DiffuseOut = texColor.xyz;
	PositionOut = worldPos;
	NormalOut = normalize(worldNormal);
}

