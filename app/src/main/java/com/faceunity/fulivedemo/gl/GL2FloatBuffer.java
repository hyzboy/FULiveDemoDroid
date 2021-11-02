package com.faceunity.fulivedemo.gl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GL2FloatBuffer {
    protected FloatBuffer vertices=null;

    public void init(int size)
    {
        vertices = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void setData(float[] buf)
    {
        vertices.clear();
        vertices.put(buf).position(0);
    }

    public void init(float[] buf)
    {
        init(buf.length);
        setData(buf);
    }

    public FloatBuffer GetBuffer()
    {
        vertices.position(0);
        return vertices;
    }
}
