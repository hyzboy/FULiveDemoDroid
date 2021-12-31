precision highp float;
varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float ChromaColor;          //-135.0
uniform float ChromaScope;          //45
uniform vec2  ChromaLuminance;      //[0.15 - 0.95]

uniform float LumAverage;           //4.0
uniform float Despill;              //0.75
uniform float DespillLuminanceAdd;  //0.3

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

float contrast(float origin,float scale)
{
    return clamp(0.5+(origin-0.5)*scale,0.0,1.0);
}

float GetChroma(float angle,float clamp_angle)
{
    float gap=abs(angle-ChromaColor);
    float alpha=clamp(gap,0.0,clamp_angle)/clamp_angle;

    return contrast(alpha,2.0);
}

vec4 clear_override_color(vec3 color,float alpha)
{
    vec3 result=color*alpha;

    float v = (2.0*result.b+result.r)/LumAverage;

    if(result.g > v) result.g = mix(result.g,v,Despill);

    float dif=rgb2y(color - result);

    result += mix(0.0, dif, DespillLuminanceAdd);

    return vec4(result,alpha);
}

void main()
{
    vec3 rgb=texture2D(sTexture,vTextureCoord).rgb;

    vec3 yuv=rgb2ycbcr(rgb);

    if(yuv.r<ChromaLuminance.x||yuv.r>ChromaLuminance.y)
    {
        gl_FragColor=vec4(rgb,1.0);
    }
    else
    {
        float angle=atan2(yuv.g, yuv.b);

        float alpha=GetChroma(angle, ChromaScope);

        vec4 fc=clear_override_color(rgb, alpha);

        gl_FragColor=fc;
    }
}