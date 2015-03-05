#version 400

in vec2 texCoord0;

uniform sampler2D filterTexture;
uniform sampler2D grayscalePostProcess;
uniform vec2 invertedTextureSize;


void main(){
    float FXAA_SPAN_MAX = 8.0;
    float FXAA_REDUCE_MIN = 1.0/128.0;
    float FXAA_REDUCE_MUL = 1.0/8.0;
    vec2 spanMax = vec2(FXAA_SPAN_MAX,FXAA_SPAN_MAX);
    vec2 spanMaxN = vec2(-FXAA_SPAN_MAX,-FXAA_SPAN_MAX);
     vec2 os = invertedTextureSize.xy;
    float colorTL = texture2D(grayscalePostProcess,texCoord0 + vec2(-1,-1)*os).y;
    float colorTR = texture2D(grayscalePostProcess,texCoord0 + vec2(1,-1)*os).y;
    float colorBL = texture2D(grayscalePostProcess,texCoord0 + vec2(-1,1)*os).y;
    float colorBR = texture2D(grayscalePostProcess,texCoord0 + vec2(1,1)*os).y;
    float colorM = texture2D(grayscalePostProcess,texCoord0).y;
   
    vec2 dif;
    dif.x = -((colorTL + colorTR) - (colorBL+colorBR));
    dif.y = (colorTL + colorBL) - (colorBR+colorTR);
    
    float reduce =  min((colorTL + colorTR + colorBL + colorBR) * 0.25 * FXAA_REDUCE_MUL,FXAA_REDUCE_MIN);
    float minScale = 1.0/(min(abs(dif.x),abs(dif.y)) +reduce);
    dif = max(spanMaxN, min(dif * minScale,spanMax)) * os ;
    
   vec3 result1 = 0.5 * (
    texture2D(filterTexture,texCoord0.xy + (dif * vec2(1.0/3.0 - 0.5))).xyz + 
    texture2D(filterTexture,texCoord0.xy + (dif * vec2(2.0/3.0 - 0.5))).xyz
    );
    
    vec3 result2 = result1 * 0.5 +  0.25 * (
    texture2D(filterTexture,texCoord0.xy + (dif * vec2(0.0/3.0 - 0.5))).xyz + 
    texture2D(filterTexture,texCoord0.xy + (dif * vec2(3.0/3.0 - 0.5))).xyz
    );
    
     float gray2 = 0.5 * 
     (texture2D(grayscalePostProcess,texCoord0+(dif * vec2(-0.5))).y+
      texture2D(grayscalePostProcess,texCoord0+(dif * vec2(3-0.5))).y);
   
   float lumaMin = min(min(colorM,min(colorTL,colorTR)),min(colorBL,colorBR));
   float lumaMax = max(max(colorM,max(colorTL,colorTR)),max(colorBL,colorBR));
   
   if(gray2 < lumaMin || gray2 > lumaMax){
   gl_FragColor=vec4(result1,1.0);
   }else{
   gl_FragColor = vec4(result2,1.0);
   }
}