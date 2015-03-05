#version 400 core

in vec2 pass_texCoord;
in vec3 worldNormal;
in vec3 worldTangent;
in vec3 worldPos;

layout (location = 0) out vec3 PositionOut;
layout (location = 1) out vec3 DiffuseOut;
layout (location = 2) out vec3 NormalOut;
layout (location = 3) out vec3 MaterialOut;

uniform sampler2D textureSampler;
uniform sampler2D normalMapSampler;
uniform sampler2D dispMapSampler;
uniform sampler2D specularMapSampler;

uniform vec3 materialColor;
uniform vec2 materialTiles;
uniform vec3 eyePosition;
uniform float dispScale;

mat3 CalcTBN(){
	vec3 Normal = normalize(worldNormal);
    vec3 Tangent = normalize(worldTangent);
    Tangent = normalize(Tangent - dot(Tangent, Normal) * Normal);
    vec3 Bitangent = cross(Tangent, Normal);

	return mat3(Tangent, Bitangent, Normal);
}

vec3 CalcBumpedNormal(mat3 TBN, vec2 texCoords)
{
	vec3 BumpMapNormal = texture(normalMapSampler, texCoords * materialTiles).xyz;
    BumpMapNormal = 2.0 * BumpMapNormal - vec3(1.0, 1.0, 1.0);
    vec3 NewNormal;
    
    NewNormal = TBN * BumpMapNormal;
    NewNormal = normalize(NewNormal);
    return NewNormal;
}

void main(void)
{
	vec3 toEye = normalize(eyePosition - worldPos);
	mat3 TBN = CalcTBN();
	vec2 texCoords = pass_texCoord + (toEye * TBN).xy * (texture2D(dispMapSampler,pass_texCoord).r * dispScale);
	vec4 texColor = texture(textureSampler,texCoords * materialTiles) * vec4(materialColor,1);
	//alpha cutout
	if(texColor.a < 0.5)
		discard;
	DiffuseOut = texColor.xyz;
	PositionOut = worldPos;
	NormalOut = CalcBumpedNormal(TBN,texCoords);
	MaterialOut = vec3(texture2D(specularMapSampler,texCoords).xy,0);
}

