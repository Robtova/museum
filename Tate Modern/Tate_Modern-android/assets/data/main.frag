#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_fog_color;
varying float fogFactor;

uniform sampler2D u_texture;
void main()
{
	vec4 col = v_color * texture2D(u_texture, v_texCoords);
	if(col.a < 0.2) discard;
	gl_FragColor = mix(v_fog_color, col, fogFactor);
}