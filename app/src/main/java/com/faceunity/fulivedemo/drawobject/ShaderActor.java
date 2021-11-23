package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderActor extends ShaderModule
{
    private final String mFragmentShaderActor =
                      "precision highp float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform sampler2D sTexture;\n"
                    + "\n"
                    + "float rgb2y(vec3 c)\n"
                    + "{\n"
                    + "    return (0.299*c.r + 0.587*c.g + 0.114*c.b);\n"
                    + "}\n"
                    + "\n"
                    + "void main()\n"
                    + "{\n"
                    + "    vec4 color=texture2D(sTexture, vTextureCoord);\n"
                    + "\n"
                    + "    vec3 result=color.rgb*color.a;\n"
                    + "    float v = (2.0*result.b+result.r)/3.5;\n"
                    + "    if(result.g > v) result.g = mix(result.g,v,0.75);\n"
                    + "    vec3 dif = (color.rgb - result);\n"
                    + "    float desaturatedDif = rgb2y(dif);\n"
                    + "    result += mix(0.0, desaturatedDif, 0.3);\n"
                    + "\n"
                    + "    gl_FragColor=vec4(result,color.a);\n"
                    + "}\n";

    private int texture_location=-1;

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderActor)) {
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
