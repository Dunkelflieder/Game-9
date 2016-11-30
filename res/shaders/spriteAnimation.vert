uniform float frames;
uniform float currentFrame;

void mainSurface(inout vec2 uv, inout vec3 position, inout vec3 normal) {
	uv.x = (currentFrame + uv.x) / frames;
}
