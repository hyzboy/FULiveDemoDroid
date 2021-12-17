precision highp float;
varying vec2 vTextureCoord;
uniform sampler2D sTexture;

float rgb2y(vec3 c)
{
    return (0.299*c.r + 0.587*c.g + 0.114*c.b);
}

void main()
{
    vec4 color=texture2D(sTexture, vTextureCoord);

    vec3 result=color.rgb*color.a;
    float v = (2.0*result.b+result.r)/3.5;
    if(result.g > v) result.g = mix(result.g,v,0.75);
    vec3 dif = (color.rgb - result);
    float desaturatedDif = rgb2y(dif);
    result += mix(0.0, desaturatedDif, 0.3);

    gl_FragColor=vec4(result,color.a);
}