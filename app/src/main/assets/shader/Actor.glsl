precision highp float;
varying vec2 vTextureCoord;
uniform sampler2D sTexture;

float rgb2y(vec3 rgb)
{
    return rgb.r *  0.299 + rgb.g *  0.587 + rgb.b *  0.114;
}

vec3 rgb2ycbcr(vec3 rgb)
{
    vec3 yuv;

    yuv.x = rgb.r *  0.299 + rgb.g *  0.587 + rgb.b *  0.114;
    yuv.y = rgb.r * -0.169 + rgb.g * -0.331 + rgb.b *  0.5  ;
    yuv.z = rgb.r *  0.5   + rgb.g * -0.419 + rgb.b * -0.081;

    return yuv;
}

float atan2(float y, float x)
{
    float t0, t1, t2, t3, t4;

    t3 = abs(x);
    t1 = abs(y);
    t0 = max(t3, t1);
    t1 = min(t3, t1);
    t3 = float(1) / t0;
    t3 = t1 * t3;

    t4 = t3 * t3;
    t0 =         - float(0.013480470);
    t0 = t0 * t4 + float(0.057477314);
    t0 = t0 * t4 - float(0.121239071);
    t0 = t0 * t4 + float(0.195635925);
    t0 = t0 * t4 - float(0.332994597);
    t0 = t0 * t4 + float(0.999995630);
    t3 = t0 * t3;

    t3 = (abs(y) > abs(x)) ? float(1.570796327) - t3 : t3;
    t3 = (x < 0.0) ?  float(3.141592654) - t3 : t3;
    t3 = (y < 0.0) ? -t3 : t3;

    return t3*float(180)/float(3.141592654);
}

const float GREEN_ANGLE=-135.0;
const float CLAMP_ANGLE=50.0;

float contrast(float origin,float scale)
{
    return clamp(0.5+(origin-0.5)*scale,0.0,1.0);
}

float GetChroma(float angle,float clamp_angle)
{
    float gap=abs(angle-GREEN_ANGLE);
    float alpha=clamp(gap,0.0,clamp_angle)/clamp_angle;

    return contrast(alpha,2.0);
}

vec4 clear_override_color(vec3 color,float alpha)
{
    vec3 result=color*alpha;

    float v = (2.0*result.b+result.r)/3.5;

    if(result.g > v) result.g = mix(result.g,v,0.75);

    float dif=rgb2y(color - result);

    result += mix(0.0, dif, 0.3);

    return vec4(result,alpha);
}

void main()
{
    vec3 rgb=texture2D(sTexture,vTextureCoord).rgb;

    vec3 yuv=rgb2ycbcr(rgb);

    float angle=atan2(yuv.g,yuv.b);

    float alpha=GetChroma(angle,CLAMP_ANGLE);

    vec4 fc=clear_override_color(rgb,alpha);

    gl_FragColor=fc;
}