package com.faceunity.fulivedemo.gl;

public class QuadUV extends GL2FloatBuffer
{
    private float QuadUVData[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f};

    private float QuadUVDataMirror[] = {
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f};

    public QuadUV(boolean mirror)
    {
        if(mirror)
            init(QuadUVDataMirror);
        else
            init(QuadUVData);
    }
}
