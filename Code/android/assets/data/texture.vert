
attribute vec4      a_position; 
attribute vec4      a_normal; 
attribute vec2      a_texCoord;

uniform mat4        u_norm_mat;
uniform mat4        u_mvp_mat;                             
uniform mat4        u_mv_mat;  
uniform mat4        u_v_mat;     

uniform vec3        u_lightPos;

varying vec2        v_texCoord;
varying vec3        v_eyeSpaceNormal;
varying vec3        v_lightDir, v_eyeVec;

void main() {
    vec4 p = vec4(u_mv_mat * a_position);
//    vec4 ld = vec4(u_v_mat * vec4(u_lightPos.xyz - p.xyz,0));
    vec4 ld = vec4(u_v_mat * vec4(u_lightPos,0)) - p;
    v_lightDir = ld.xyz;
	v_eyeVec = -p.xyz;

    v_texCoord = a_texCoord;
    v_eyeSpaceNormal = vec3(u_norm_mat * a_normal);
    gl_Position = u_mvp_mat * a_position;
}
