package com.faceunity.fulivedemo.gl;

import android.opengl.GLES30;

import java.nio.Buffer;

public class GLPBO extends GLClass
{
    private int pbos[]={-1,-1};
    private int pbo_length=0;

    private int active=-1;
    private int width=0;
    private int height=0;

    private boolean init=false;

    public GLPBO(String tag,int w,int h)
    {
        super("PBO");

        ClearGLError();
        GLES30.glGenBuffers(2,pbos,0);

        CheckGLError("Gen PBOs");
        active=0;

        width=w;
        height=h;

        pbo_length=width*height*4;

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[0]);
        GLES30.glBufferData(GLES30.GL_PIXEL_PACK_BUFFER, pbo_length, null, GLES30.GL_STATIC_READ);

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[1]);
        GLES30.glBufferData(GLES30.GL_PIXEL_PACK_BUFFER, pbo_length, null, GLES30.GL_STATIC_READ);

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,0);
        ClearGLError();

        init=true;
    }

    /**
     * 开始获取图像数据
     * @return
     */
    public Buffer Begin()
    {
        ClearGLError();

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[active]);
        GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT0);

        GLES30.glReadPixels(0, 0, width, height, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE,0);
        //CheckGLError("glReadPixels to PBO");

        active=1-active;

        if(init)        //first frame,no data;
        {
            init=false;
            return null;
        }

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[active]);
        CheckGLError("BindPBO Map");

        Buffer buf=GLES30.glMapBufferRange(GLES30.GL_PIXEL_PACK_BUFFER, 0, pbo_length, GLES30.GL_MAP_READ_BIT);
        CheckGLError("Map PBO");

        return buf;
    }

    /**
     * 结束获取
     */
    public void End()
    {
        GLES30.glUnmapBuffer(GLES30.GL_PIXEL_PACK_BUFFER);
        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,0);
        ClearGLError();
    }
}
