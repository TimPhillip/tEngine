#version 400

in vec3 Position;
in vec2 TexCoord;

out vec2 pass_texCoord;

uniform mat4 Transformation;

void main(void)
{
	gl_Position= Transformation * vec4(Position,1.0f);
	pass_texCoord = TexCoord;
}