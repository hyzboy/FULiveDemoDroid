package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderOpaque extends ShaderModule
{
    private int texture_location=-1;

    @Override
    public boolean init() {
        if (!super.init("Opaque")) {
            return (false);
        }

        texture_location=GLES20.glGetUniformLocation(mProgram,"sTexture");

        return (true);
    }

    public ShaderOpaque()
    {
        super("ShaderOpaque");
        init();
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);
    }
}
