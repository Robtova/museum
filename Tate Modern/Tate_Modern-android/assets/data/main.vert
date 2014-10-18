uniform mat4 u_projTrans;
uniform float u_fog_end;
uniform float u_fog_start;
uniform vec4 u_fog_color;
uniform vec4 u_ambient;

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_fog_color;
varying float fogFactor;

void main()
{
	v_texCoords = a_texCoord0;
	gl_Position =  u_projTrans * a_position;
	a_position * u_projTrans;
	
	v_fog_color = u_fog_color;
	float f = (u_fog_end - gl_Position.z) / (u_fog_end - u_fog_start);
	fogFactor = clamp(f * 0.7, 0.0f, 1.0f);
	v_color = a_color;
	v_color *= u_ambient;
}