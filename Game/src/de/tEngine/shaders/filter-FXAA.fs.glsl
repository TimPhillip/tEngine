#version 400

in vec2 texCoord0;

uniform sampler2D filterTexture;
uniform sampler2D grayscalePostProcess;

vec2 getDirectionVector(vec2 texcoord){
    float colorlo = texture2D(filterTexture,texcoord + vec2(-1,-1)).x;
    float colorro = texture2D(filterTexture,texcoord + vec2(1,-1)).x;
    float colorlu = texture2D(filterTexture,texcoord + vec2(-1,1)).x;
    float colorru = texture2D(filterTexture,texcoord + vec2(1,1)).x;
    float colorme = texture2D(filterTexture,texcoord).x;
    vec2 veclo = vec2(-1*(colorme-colorlo),-1*(colorme-colorlo));
    vec2 vecro = vec2(1*(colorme-colorro),-1*(colorme-colorro));
    vec2 veclu = vec2(-1*(colorme-colorlu),1*(colorme-colorlu));
    vec2 vecru = vec2(1*(colorme-colorru),1*(colorme-colorru));
    
    return (veclo + vecro)+(veclu + vecru);
}

vec4 boxBlur(float intensity){
float otherIntensity = (1-intensity)/9;
vec3 color = texture2D(filterTexture,texCoord0).xyz * intensity;
//Top row
color += texture2D(filterTexture,texCoord0+vec2(-1,-1)).xyz * otherIntensity;
color += texture2D(filterTexture,texCoord0+vec2(0,-1)).xyz * otherIntensity;
color += texture2D(filterTexture,texCoord0+vec2(1,-1)).xyz * otherIntensity;

//middle row
color += texture2D(filterTexture,texCoord0+vec2(-1,0)).xyz * otherIntensity;
color += texture2D(filterTexture,texCoord0+vec2(1,0)).xyz * otherIntensity;

//Bottom row
color += texture2D(filterTexture,texCoord0+vec2(-1,1)).xyz * otherIntensity;
color += texture2D(filterTexture,texCoord0+vec2(0,1)).xyz * otherIntensity;
color += texture2D(filterTexture,texCoord0+vec2(1,1)).xyz * otherIntensity;

return vec4(color.xyz,0);
}


void main(){
    
   vec2 dir = getDirectionVector(texCoord0);
   gl_FragColor = boxBlur(length(dir)*2);
}