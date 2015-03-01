#version 400

out vec4 out_color;

void main(){
    float depth = gl_FragCoord.z;
	
	float moment1 = depth;
	float moment2 = depth * depth;
	
	gl_FragColor = vec4( moment1,moment2, 0.0, 0.0 );
}