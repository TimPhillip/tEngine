#version 400 core

uniform sampler2D gBufferPosition;
uniform sampler2D gBufferDiffuse;
uniform sampler2D gBufferNormal;

uniform sampler2D shadowMap;
uniform mat4 lightViewProj;

uniform vec3 lightDirection;
uniform vec3 lightColor;
uniform float lightIntensity;
uniform vec2 screenSize;

out vec4 FragColor;

vec2 CalcTexCoord()
{
	return gl_FragCoord.xy / screenSize;
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
	float p_max = variance / (variance + d*d);
	
	return p_max;
}

void main(void)
{
	vec2 TexCoord = CalcTexCoord();
	vec3 Normal = texture(gBufferNormal,TexCoord).xyz;
	vec3 Position = texture(gBufferPosition,TexCoord).xyz;
	float k = max(dot(lightDirection * -1,Normal),0);
	
	float shadowFactor = 1.0f;
	vec4 LightSpacePos = lightViewProj * vec4(Position,1.0f);
	vec3 LightCoords = LightSpacePos.xyz / LightSpacePos.w;
	
	LightCoords = LightCoords * 0.5f + vec3(0.5f);
	//float Depth = texture(shadowMap,LightCoords.xy).x;
	//if(Depth < (LightCoords.z - 1.0f / 1024)){
		//shadowFactor = 0.5f;
	//}
	shadowFactor = clamp(chebyshevUpperBound(LightCoords.z,LightCoords),0.4f,1.0f);
	FragColor = (k ) *shadowFactor * vec4(lightColor,1) * vec4(texture(gBufferDiffuse,TexCoord).xyz,1) * lightIntensity;
}



