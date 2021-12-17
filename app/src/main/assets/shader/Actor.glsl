precision highp float;
varying vec2 vTextureCoord;
uniform sampler2D sTexture;

float rgb2y(vec3 c)
{
    return (0.299*c.r + 0.587*c.g + 0.114*c.b);
}

vec4 clear_override_color(vec4 color)
{
    vec3 result=color.rgb*color.a;

    float v = (2.0*result.b+result.r)/3.5;

    if(result.g > v) result.g = mix(result.g,v,0.75);

    float dif=rgb2y(color.rgb - result);

    result += mix(0.0, dif, 0.3);

    return vec4(result,color.a);
}

void main()
{
    vec4 color=texture2D(sTexture, vTextureCoord);

    gl_FragColor=clear_override_color(color);
}