#version 400 core

in vec2 pass_texCoord;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform float isGlowing;
uniform vec3 matColor;

void main(void)
{
    vec4 texColor = texture(textureSampler,pass_texCoord);
	out_color = texColor * vec4(matColor,1.0);
}

