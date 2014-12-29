#version 400

in vec3 position;
in vec2 texCoord;
in vec3 normal;

out vec2 pass_texCoord;
out vec3 worldNormal;
out vec3 toLightDir[20];
out float lightDistance[20];

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

//Light
uniform vec3 lightPosition[20];

void main(void)
{
	vec4 worldPosition = worldMatrix * vec4(position,1);
	gl_Position= projectionMatrix * viewMatrix * worldPosition;
	pass_texCoord = texCoord;
	worldNormal = (worldMatrix * vec4(normal,0.0)).xyz;
	for(int i = 0; i < 20; i++){
	toLightDir[i] = lightPosition[i] - worldPosition.xyz;
	lightDistance[i] = toLightDir[i].length();
	}
}