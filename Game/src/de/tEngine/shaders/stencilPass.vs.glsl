#version 400

in vec3 Position;

uniform mat4 worldViewProj;

void main(){
    gl_Position = worldViewProj * vec4(Position,1);
}