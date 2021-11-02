package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;
import android.opengl.GLU;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class DrawTextureAlpha extends DrawObject
{
    private int textureID=-1;
    private ShaderModule shader=new ShaderAlpha();

    public DrawTextureAlpha()
    {
        super(DrawObject.ObjectType.TextureAlpha);
    }

    @Override
    public void start(){}

    @Override
    public void update(){}

    public void setTextureID(int id)
    {
        textureID=id;
    }

    @Override
    public void draw()
    {
        if(textureID==-1)return;

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        shader.begin();

            shader.SetDirection(direction);
            shader.SetMirror(mirror);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glEnable(GLES20.GL_TEXTURE_2D);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        render_layout.bind(shader.maPositionHandle);
        texture_uv.bind(shader.maTexCoordHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
