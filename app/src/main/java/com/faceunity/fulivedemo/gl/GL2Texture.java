package com.faceunity.fulivedemo.gl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class GL2Texture extends com.faceunity.fulivedemo.gl.GLClass
{
    protected int texture_id=-1;
    private int texture_type=GLES20.GL_TEXTURE_2D;
    private int width=0,height=0;

    public GL2Texture()
    {
        super("GL2Texture");
    }
    public GL2Texture(String tag)
    {
        super(tag);
    }

    private void CreateTexture()
    {
        int[] textures=new int[1];

        ClearGLError();
        GLES20.glGenTextures(1,textures,0);
        CheckGLError("glGenTextures");

        texture_id =textures[0];

        bind();

        GLES20.glTexParameteri(texture_type, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        CheckGLError("glTexParameteri(MIN_FILTER,LINEAR)");

        GLES20.glTexParameteri(texture_type, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        CheckGLError("glTexParameteri(MAG_FILTER,LINEAR)");

        GLES20.glTexParameteri(texture_type, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        CheckGLError("glTexParameteri(WRAP_S,CLAMP_TO_EDGE)");

        GLES20.glTexParameteri(texture_type, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        CheckGLError("glTexParameteri(WRAP_T,CLAMP_TO_EDGE)");
    }

    public void init(int tt)
    {
        texture_type=tt;
        CreateTexture();
    }

    public void init(int tt,Bitmap bmp)
    {
        init(tt);

        ClearGLError();
        GLUtils.texImage2D(texture_type,0,bmp,0);
        CheckGLError("texImage2D");
        width=bmp.getWidth();
        height=bmp.getHeight();
    }

    public void bind()
    {
        ClearGLError();
        GLES20.glEnable(texture_type);
        //CheckGLError("glEnable("+texture_type+")");
        GLES20.glBindTexture(texture_type, texture_id);
        //CheckGLError("glBindTexture("+texture_type+")");
        ClearGLError();
    }

    public void bind(int index)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+index);
        bind();
    }

    public void unbind(int index)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+index);
        GLES20.glBindTexture(texture_type,0);
        GLES20.glDisable(texture_type);
    }

    public int getID() {return texture_id;}
    public int getType(){return texture_type;}

    public void set(Bitmap bmp)
    {
        int[] old_textures = new int[1];
        old_textures[0] = texture_id;

        CreateTexture();
        ClearGLError();
        GLUtils.texImage2D(texture_type,0,bmp,0);
        CheckGLError("texImage2D");
        width=bmp.getWidth();
        height=bmp.getHeight();

        if(old_textures[0]!=-1)
            GLES20.glDeleteTextures(1, old_textures, 0);
    }
}
