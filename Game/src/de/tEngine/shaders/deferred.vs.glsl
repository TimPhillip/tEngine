#version 400 core

in vec3 position;
in vec2 texCoord;
in vec3 normal;
in vec3 tangent;

out vec2 pass_texCoord;
out vec3 worldNormal;
out vec3 worldTangent;
out vec3 worldPos;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void)
{
	vec4 worldPosition = worldMatrix * vec4(position,1);
	gl_Position= projectionMatrix * viewMatrix * worldPosition;
	pass_texCoord = texCoord;
    worldNormal = (worldMatrix * vec4(normal,0.0)).xyz;
    worldTangent = (worldMatrix * vec4(tangent,0)).xyz;
	worldPos = worldPosition.xyz;
}