package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderText extends ShaderModule
{
    private final String fragment_shader_text=
                    "precision highp float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform vec4 vColor;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "void main()\n" +
                    "{\n" +
                    "  float alpha=texture2D(sTexture,vTextureCoord).a;\n" +
                    "  gl_FragColor = vec4(vColor.rgb,vColor.a*alpha);\n" +
                    "}\n";

    private int color_location=-1;

    @Override
    public boolean init() {
        if (!super.init(fragment_shader_text)) {
            return (false);
        }

        ClearGLError();
        color_location= GLES20.glGetUniformLocation(mProgram,"vColor");
        CheckGLError("glGetUniformLocation vColor");
        if (color_location == -1) {
            return (false);
        }
        return (true);
    }

    public ShaderText()
    {
        super("ShaderText");
        init();
    }

    public void SetTextColor(float color[])
    {
        ClearGLError();
        GLES20.glUniform4fv(color_location,1,color,0);
        CheckGLError("SetTextColor,glUniform4fv()");
    }
}
