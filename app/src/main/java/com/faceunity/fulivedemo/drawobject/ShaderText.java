package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderText extends ShaderModule
{
    private int color_location=-1;
    private int texture_location=-1;

    @Override
    public boolean init() {
        if (!super.init("Text")) {
            return (false);
        }

        ClearGLError();
        color_location= GLES20.glGetUniformLocation(mProgram,"vColor");
        CheckGLError("glGetUniformLocation vColor");
        if (color_location == -1) {
            return (false);
        }
        texture_location=GLES20.glGetUniformLocation(mProgram,"sTexture");
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

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);
    }
}
