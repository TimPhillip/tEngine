#version 400

in vec2 texCoord0;

uniform vec3 blurScale;
uniform sampler2D filterTexture;

void main(){
	vec4 color;
    color += texture2D(filterTexture,texCoord0 + vec2(-3.0) * blurScale.xy) * (1.0f / 64.0f);
    color += texture2D(filterTexture,texCoord0 + vec2(-2.0) * blurScale.xy) * (6.0f / 64.0f);
    color += texture2D(filterTexture,texCoord0 + vec2(-1.0) * blurScale.xy) * (15.0f / 64.0f);
    color += texture2D(filterTexture,texCoord0 + vec2(0.0) * blurScale.xy)  * (20.0f / 64.0f);
    color += texture2D(filterTexture,texCoord0 + vec2(1.0) * blurScale.xy)  * (15.0f / 64.0f);
    color += texture2D(filterTexture,texCoord0 + vec2(2.0) * blurScale.xy)  * (6.0f / 64.0f);
    color += texture2D(filterTexture,texCoord0 + vec2(3.0) * blurScale.xy)  * (1.0f / 64.0f);
    gl_FragColor = color;
}