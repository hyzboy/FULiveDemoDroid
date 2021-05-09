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

    public void bind(int sampler_location)
    {
        vertices.position(0);
        GLES20.glVertexAttribPointer(sampler_location, 2, GLES20.GL_FLOAT, false, 0, vertices);
        GLES20.glEnableVertexAttribArray(sampler_location);
    }
}
