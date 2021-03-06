package com.faceunity.fulivedemo.gl;

import android.opengl.GLES20;
import android.util.Log;

import com.android.glutil.YUV;

import java.nio.Buffer;

public class GL2FBO extends GLClass
{
    private int width,height;
    private int fbo[]={-1};
    private int texture[]={-1};
    private int depth_rb[]={-1};

    private GLPBO pbo=null;
    private byte[] nv21_data=null;

    public GL2FBO()
    {
        super("GL2FBO");
    }

    public void init(int w,int h)
    {
        width=w;
        height=h;

        GLES20.glGenFramebuffers(1, fbo, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo[0]);

        //texture
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB565, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, texture[0], 0);

        //depth renderbuffer
//        GLES20.glGenRenderbuffers(1, depth_rb, 0);
//        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depth_rb[0]);
//        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
//        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, depth_rb[0]);

        int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        {
            pbo = new GLPBO("pbo_fbo", width, height);

            int y_bytes = width * height;
            int vu_bytes = y_bytes / 2;

            nv21_data = new byte[y_bytes + vu_bytes];
        }

        if(status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            Log.d("FBORenderer", "Framebuffer incomplete. Status: " + status);

            throw new RuntimeException("Error creating FBO");
        }
    }

    public void Begin()
    {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo[0]);
    }

    public void End()
    {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public int GetTextureID()
    {
        return texture[0];
    }
    public int GetWidth(){return width;}
    public int GetHeight(){return height;}

    public byte[] GetNV21()
    {
        Begin();
        Buffer buf=pbo.Begin();

        if(buf!=null)
        {
            YUV.abgr2nv21(nv21_data, buf, width, height);

            pbo.End();
            End();
            return nv21_data;
        }
        else
        {
            pbo.End();
            End();
            return null;
        }
    }
}
