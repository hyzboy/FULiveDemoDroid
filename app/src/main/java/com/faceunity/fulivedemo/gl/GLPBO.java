package com.faceunity.fulivedemo.gl;

import android.opengl.GLES30;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class GLPBO extends GLClass
{
    private int pbos[]={-1,-1};
    private int active=-1;
    private int width=0;
    private int height=0;

    public GLPBO(String tag,int w,int h)
    {
        super("PBO");

        ClearGLError();
        GLES30.glGenBuffers(2,pbos,0);

        CheckGLError("Gen PBOs");
        active=1;

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[0]);
        CheckGLError("Bind PBO 0");
        GLES30.glBufferData(GLES30.GL_PIXEL_PACK_BUFFER,width*height*4,null,GLES30.GL_DYNAMIC_READ);
        CheckGLError("BufferData PBO 0");

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[1]);
        CheckGLError("Bind PBO 1");
        GLES30.glBufferData(GLES30.GL_PIXEL_PACK_BUFFER,width*height*4,null,GLES30.GL_DYNAMIC_READ);
        CheckGLError("BufferData PBO 1");

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,0);
        CheckGLError("Clear PBO Bind");

        width=w;
        height=h;
    }

    /**
     * 开始获取图像数据
     * @return
     */
    public ByteBuffer Begin()
    {
        active=1-active;

        ClearGLError();

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[active]);
        CheckGLError("BindPBO Read");

//        GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT0);
//        CheckGLError("ReadBuffer from color 0");

        GLES30.glReadPixels(0, 0, width, height, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, 0);
        CheckGLError("glReadPixels to PBO");

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,0);
        CheckGLError("Clear PBO Bind");

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[1-active]);
        CheckGLError("BindPBO Map");

        Buffer buf=GLES30.glMapBufferRange(GLES30.GL_PIXEL_PACK_BUFFER,0,width*height*4,GLES30.GL_MAP_READ_BIT);
        CheckGLError("Map PBO");

        return (ByteBuffer)buf;
    }

    /**
     * 结束获取
     */
    public void End()
    {
        GLES30.glUnmapBuffer(GLES30.GL_PIXEL_PACK_BUFFER);
        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,0);
    }
}
