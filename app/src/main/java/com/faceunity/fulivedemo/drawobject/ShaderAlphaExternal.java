package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderAlphaExternal extends ShaderModule
{
    private int texture_rgb_location=-1;
    private int texture_alpha_location=-1;

    @Override
    public boolean init() {
        if (!super.init("External_RGB_Alpha")) {
            return (false);
        }

        texture_rgb_location=GLES20.glGetUniformLocation(mProgram,"sTextureRGB");
        texture_alpha_location=GLES20.glGetUniformLocation(mProgram,"sTextureAlpha");

        return (true);
    }

    public ShaderAlphaExternal()
    {
        super("ShaderAlphaExternal");
        init();
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_rgb_location,0);
        GLES20.glUniform1i(texture_alpha_location,1);
    }
}
