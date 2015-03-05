#version 400 core

uniform sampler2D gBufferPosition;
uniform sampler2D gBufferDiffuse;
uniform sampler2D gBufferNormal;
uniform sampler2D gBufferMaterial;

uniform sampler2D shadowMap;
uniform mat4 lightViewProj;
uniform vec3 eyePosition;
uniform vec3 lightDirection;
uniform vec3 lightColor;
uniform float lightIntensity;
uniform vec2 screenSize;

out vec4 FragColor;

vec2 CalcTexCoord()
{
	return gl_FragCoord.xy / screenSize;
}

float linstep(float low,float high,float v){
	return clamp((v-low)/(high-low),0,1);
}

float chebyshevUpperBound( float distance,vec3 lightCoords)
{
	// We retrive the two moments previously stored (depth and depth*depth)
	vec2 moments = texture2D(shadowMap,lightCoords.xy).rg;
		
	// Surface is fully lit. as the current fragment is before the light occluder
	if (distance <= moments.x)
		return 1.0 ;
	
	// The fragment is either in shadow or penumbra. We now use chebyshev's upperBound to check
	// How likely this pixel is to be lit (p_max)
	float variance = moments.y - (moments.x*moments.x);
	variance = max(variance,0.00002);
	
	float d = distance - moments.x;
	float p_max = linstep(0.3f,1.0f,variance / (variance + d*d));
	
	return p_max;
}

void main(void)
{
	vec2 TexCoord = CalcTexCoord();
	vec3 Normal = texture(gBufferNormal,TexCoord).xyz;
	vec3 Position = texture(gBufferPosition,TexCoord).xyz;
	
	float diffuseFactor = max(dot(lightDirection * -1,Normal),0);
	vec3 toEye = normalize(eyePosition - Position);
	vec3 r = reflect(lightDirection,Normal);
	
	float specPower = texture2D(gBufferMaterial,TexCoord).y * 10 + 1;
	float specFactor = texture2D(gBufferMaterial,TexCoord).x;
	specFactor *= pow(max(dot(toEye,r),0.0f),specPower);
	
	if(dot(lightDirection * -1,Normal) <= 0){
		specFactor = 0;}
	
	float shadowFactor = 1.0f;
	vec4 LightSpacePos = lightViewProj * vec4(Position,1.0f);
	vec3 LightCoords = LightSpacePos.xyz / LightSpacePos.w;
	
	LightCoords = LightCoords * 0.5f + vec3(0.5f);
	//float Depth = texture(shadowMap,LightCoords.xy).x;
	//if(Depth < (LightCoords.z - 1.0f / 1024)){
		//shadowFactor = 0.5f;
	//}
	shadowFactor = chebyshevUpperBound(LightCoords.z,LightCoords);
	//FragColor = max(min(diffuseFactor,shadowFactor),0.2f) * vec4(lightColor,1) * lightIntensity * vec4(texture(gBufferDiffuse,TexCoord).xyz,1);
	
	vec4 matColor = vec4(texture(gBufferDiffuse,TexCoord).xyz,1);
	vec4 ambient = 0.4f * vec4(lightColor,1) * lightIntensity * matColor;
	vec4 diffuse = diffuseFactor * lightIntensity * vec4(lightColor,1) * matColor;
	vec4 specular = specFactor * vec4(lightColor,1) * matColor * lightIntensity;
	
	FragColor = ambient + shadowFactor * (diffuse + specular);
}



