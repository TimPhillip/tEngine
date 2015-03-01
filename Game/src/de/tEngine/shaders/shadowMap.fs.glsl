#version 400

out vec4 out_color;

void main(){
    float depth = gl_FragCoord.z;
	
	float moment1 = depth;
	//Fixing surface acne
	float dx = dFdx(depth);
	float dy = dFdy(depth);
	float moment2 = depth * depth + 0.25f * (dx *dx +dy *dy);
	
	gl_FragColor = vec4( moment1,moment2, 0.0, 0.0 );
}