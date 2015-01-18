#version 400

in vec3 Position;

uniform mat4 LightView;
uniform mat4 LightProj;
uniform mat4 World;

void main(){
    gl_Position = LightProj * LightView * World * vec4(Position,1);
}