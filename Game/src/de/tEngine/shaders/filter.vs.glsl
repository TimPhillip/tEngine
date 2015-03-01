#version 400

in vec3 position;
in vec2 texCoord;

out vec2 texCoord0;

void main(void)
{
	gl_Position = vec4(position,1);
	texCoord0 = texCoord;
}