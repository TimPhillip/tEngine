#version 400

in vec2 texCoord0;

uniform sampler2D filterTexture;

void main(){
    vec3 color;
    color = texture2D(filterTexture,texCoord0).xyz;
    vec3 luma = vec3(0.299f,0.587f,0.114) ;
    float cvalue = dot(color,luma);
    gl_FragColor = vec4(cvalue,cvalue,cvalue,0);
   
}