#version 400 core

in vec2 pass_texCoord;
in vec3 worldNormal;
in vec3 worldTangent;
in vec3 worldPos;

layout (location = 0) out vec3 PositionOut;
layout (location = 1) out vec3 DiffuseOut;
layout (location = 2) out vec3 NormalOut;

uniform sampler2D textureSampler;
uniform sampler2D normalMapSampler;

uniform vec3 materialColor;
uniform vec2 materialTiles;

vec3 CalcBumpedNormal()
{
    vec3 Normal = normalize(worldNormal);
    vec3 Tangent = normalize(worldTangent);
    Tangent = normalize(Tangent - dot(Tangent, Normal) * Normal);
    vec3 Bitangent = cross(Tangent, Normal);
    vec3 BumpMapNormal = texture(normalMapSampler, pass_texCoord).xyz;
    BumpMapNormal = 2.0 * BumpMapNormal - vec3(1.0, 1.0, 1.0);
    vec3 NewNormal;
    mat3 TBN = mat3(Tangent, Bitangent, Normal);
    NewNormal = TBN * BumpMapNormal;
    NewNormal = normalize(NewNormal);
    return NewNormal;
}

void main(void)
{
	vec4 texColor = texture(textureSampler,pass_texCoord * materialTiles) * vec4(materialColor,1);
	if(texColor.a < 0.5)
		discard;
	DiffuseOut = texColor.xyz;
	PositionOut = worldPos;
	NormalOut = CalcBumpedNormal();
}

