package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderActor extends ShaderModule
{
    private final String mFragmentShaderOpaque =
                      "precision highp float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform sampler2D sTexture;\n"
                    + "\n"
                    + "vec3 rgb2yuv(vec3 rgb)\n"
                    + "{\n"
                    + "    return vec3(rgb.r *  0.299 + rgb.g *  0.587 + rgb.b *  0.114,\n"
                    + "            rgb.r * -0.169 + rgb.g * -0.331 + rgb.b *  0.5   ,\n"
                    + "            rgb.r *  0.5   + rgb.g * -0.419 + rgb.b * -0.081 );\n"
                    + "}\n"
                    + "\n"
                    + "float atan2(float y, float x)\n"
                    + "{\n"
                    + "    float t0, t1, t2, t3, t4;\n"
                    + "\n"
                    + "    t3 = abs(x);\n"
                    + "    t1 = abs(y);\n"
                    + "    t0 = max(t3, t1);\n"
                    + "    t1 = min(t3, t1);\n"
                    + "    t3 = float(1) / t0;\n"
                    + "    t3 = t1 * t3;\n"
                    + "\n"
                    + "    t4 = t3 * t3;\n"
                    + "    t0 =         - float(0.013480470);\n"
                    + "    t0 = t0 * t4 + float(0.057477314);\n"
                    + "    t0 = t0 * t4 - float(0.121239071);\n"
                    + "    t0 = t0 * t4 + float(0.195635925);\n"
                    + "    t0 = t0 * t4 - float(0.332994597);\n"
                    + "    t0 = t0 * t4 + float(0.999995630);\n"
                    + "    t3 = t0 * t3;\n"
                    + "\n"
                    + "    t3 = (abs(y) > abs(x)) ? float(1.570796327) - t3 : t3;\n"
                    + "    t3 = (x < 0.0) ?  float(3.141592654) - t3 : t3;\n"
                    + "    t3 = (y < 0.0) ? -t3 : t3;\n"
                    + "\n"
                    + "    return t3*float(180)/float(3.141592654);\n"
                    + "}\n"
                    + "\n"
                    + "const float GREEN_ANGLE=-135.0;\n"
                    + "const float CLAMP_ANGLE=50.0;\n"
                    + "const float OVER_ANGLE=CLAMP_ANGLE+60.0;\n"
                    + "\n"
                    + "float contrast(float origin,float scale)\n"
                    + "{\n"
                    + "    return clamp(0.5+(origin-0.5)*scale,0.0,1.0);\n"
                    + "}\n"
                    + "\n"
                    + "float GetChroma(float angle,float clamp_angle)\n"
                    + "{\n"
                    + "    float gap=abs(angle-GREEN_ANGLE);\n"
                    + "    float alpha=clamp(gap,0.0,clamp_angle)/clamp_angle;\n"
                    + "\n"
                    + "    return contrast(alpha,2.0);\n"
                    + "}\n"
                    + "\n"
                    + "vec3 tosepia(vec3 color,float amount)\n"
                    + "{\n"
                    + "    vec3 sc;\n"
                    + "\n"
                    + "    sc.r=color.r*0.3588+color.g*0.7044+color.b*0.1368;\n"
                    + "    sc.g=color.r*0.2990+color.g*0.5870+color.b*0.1140;\n"
                    + "    sc.b=color.r*0.2392+color.g*0.4696+color.b*0.0912;\n"
                    + "\n"
                    + "    return mix(sc,color,1.0-amount);\n"
                    + "}\n"
                    + "\n"
                    + "void main()\n"
                    + "{\n"
                    + "    vec4 color=texture2D(sTexture, vTextureCoord);\n"
                    + "    vec3 yuv=rgb2yuv(color.rgb);\n"
                    + "    float angle=atan2(yuv.g,yuv.b);\n"
                    + "    float alpha=GetChroma(angle,CLAMP_ANGLE);\n"
                    + "    float over_alpha=GetChroma(angle,OVER_ANGLE);\n"
                    + "    float gap=clamp((alpha-over_alpha)*10.0,0.0,1.0);\n"
                    + "\n"
                    + "    gl_FragColor=vec4(tosepia(color.rgb,gap),color.a);\n"
                    + "}\n";

    private int texture_location=-1;

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderOpaque)) {
            return (false);
        }

        texture_location= GLES20.glGetUniformLocation(mProgram,"sTexture");

        return (true);
    }

    public ShaderActor()
    {
        super("ShaderActor");
        init();
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);
    }
}
