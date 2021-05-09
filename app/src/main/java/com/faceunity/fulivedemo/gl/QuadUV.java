package com.faceunity.fulivedemo.gl;

import com.faceunity.fulivedemo.gl.GL2FloatBuffer;

public class QuadUV extends GL2FloatBuffer {

    private static final float QuadUVData[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f};

    public QuadUV()
    {
        init(QuadUVData);
    }
}
