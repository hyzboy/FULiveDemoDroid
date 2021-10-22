package com.faceunity.fulivedemo.gl;

import android.opengl.GLES30;
import java.nio.Buffer;

public class GLPBO extends GLClass
{
    private int pbos[]={-1,-1};
    private int active=-1;
    private int width=0;
    private int height=0;

    public GLPBO(String tag,int w,int h)
    {
        super("PBO");

        GLES30.glGenBuffers(2,pbos,2);
        active=1;

        width=w;
        height=h;
    }

    /**
     * 开始获取图像数据
     * @return
     */
    public Buffer Begin()
    {
        active=1-active;

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[active]);
        GLES30.glReadPixels(0, 0, width, height, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);

        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,pbos[1-active]);
        return GLES30.glMapBufferRange(GLES30.GL_PIXEL_UNPACK_BUFFER,0,width*height*4,GLES30.GL_MAP_READ_BIT);
    }

    /**
     * 结束获取
     */
    public void End()
    {
        GLES30.glUnmapBuffer(GLES30.GL_PIXEL_PACK_BUFFER);
        GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER,GLES30.GL_NONE);
    }
}
