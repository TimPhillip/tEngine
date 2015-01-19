#version 400 core

in vec2 pass_texCoord;

out vec4 out_color;

uniform sampler2D textureSampler;

void main(void)
{
	float depth = texture(textureSampler,pass_texCoord).x;
	depth = 1.0f - (depth * 0.8f);
	out_color = vec4(depth);
}

