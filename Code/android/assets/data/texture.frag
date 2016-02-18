
uniform sampler2D   s_texture1;                        


varying vec2        v_texCoord;
varying vec3        v_eyeSpaceNormal;
varying vec4        v_position;
varying vec3        v_lightDir, v_eyeVec;

void main() {   

	vec3 N = normalize(v_eyeSpaceNormal);
    vec3 E = normalize(v_eyeVec); 
    vec3 L = normalize(v_lightDir);
    
    vec3 reflectV = reflect(-L, N);

    vec4 c = texture2D(s_texture1, v_texCoord);
    vec4 ambientTerm = c * 0.4;
    vec4 diffuseTerm = c * 0.6 * max(dot(N, L), 0.0f);

    float matShine = 100f;
    vec4 specularTerm = vec4(.4,.4,.4,1) * pow(max(dot(reflectV, E), 0.0f), matShine);
    
    gl_FragColor =  ambientTerm + diffuseTerm + specularTerm;
    
}
