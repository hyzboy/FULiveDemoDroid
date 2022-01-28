precision highp float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

uniform float KeyCB;
uniform float KeyCR;
uniform float ColorCutoff;          //0.22
uniform float ColorFeathering;      //0.33
uniform float MaskFeathering;       //1
uniform float Sharpening;           //0.5

uniform vec2 pixelWidth;
uniform vec2 pixelHeight;

uniform float LumAverage;           //4.0
uniform float Despill;              //0.75
uniform float DespillLuminanceAdd;  //0.3

float rgb2y(vec3 rgb)
{
    return rgb.r *  0.299 + rgb.g *  0.587 + rgb.b *  0.114;
}

float rgb2cb(vec3 c)
{
    return (0.5 + -0.168736*c.r - 0.331264*c.g + 0.5*c.b);
}

float rgb2cr(vec3 c)
{
    return (0.5 + 0.5*c.r - 0.418688*c.g - 0.081312*c.b);
}

float maskedTex2D(vec2 uv)
{
    vec3 color = texture2D(sTexture,uv).rgb;

    float cb = rgb2cb(color);
    float cr = rgb2cr(color);

    float temp = (KeyCB-cb)*(KeyCB-cb)+(KeyCR-cr)*(KeyCR-cr);
    if (temp < ColorCutoff) return 0.0;
    if (temp < ColorFeathering) return (temp-ColorCutoff)/(ColorFeathering-ColorCutoff);
    return 1.0;
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
    vec3 color=texture2D(sTexture,vTextureCoord).rgb;

    float c = maskedTex2D(vTextureCoord);
    float r = maskedTex2D(vTextureCoord + pixelWidth);
    float l = maskedTex2D(vTextureCoord - pixelWidth);
    float d = maskedTex2D(vTextureCoord + pixelHeight);
    float u = maskedTex2D(vTextureCoord - pixelHeight);
    float rd = maskedTex2D(vTextureCoord + pixelWidth + pixelHeight) * 0.707;
    float dl = maskedTex2D(vTextureCoord - pixelWidth + pixelHeight) * 0.707;
    float lu = maskedTex2D(vTextureCoord - pixelHeight - pixelWidth) * 0.707;
    float ur = maskedTex2D(vTextureCoord + pixelWidth - pixelHeight) * 0.707;
    float blurContribution = (r + l + d + u + rd + dl + lu + ur + c) * 0.12774655;
    float smoothedMask = smoothstep(Sharpening, 1.0, mix(c, blurContribution, MaskFeathering));

    gl_FragColor=clear_override_color(color,smoothedMask);
}