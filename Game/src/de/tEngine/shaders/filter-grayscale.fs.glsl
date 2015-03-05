#version 400

in vec2 texCoord0;

uniform sampler2D filterTexture;

void main(){
    vec4 color;
    color = vec4(texture2D(filterTexture,texCoord0).xyz,0);
    float cvalue = (color.x + color.y + color.z) / 3;
    color = vec4(cvalue,cvalue,cvalue,0);
    gl_FragColor = color;
}