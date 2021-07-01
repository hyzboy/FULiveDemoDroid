package com.faceunity.fulivedemo.gl;

public class QuadUV extends GL2FloatBuffer
{
    public enum Direction
    {
        Horz,   //横屏
        Vert   //坚屏
    }

    /*
        2--3
        |  |
        0--1
     */

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

    private float QuadUVDataV[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,};

    private float QuadUVDataMirrorV[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f};

    private boolean mirror=false;

    public QuadUV(boolean m)
    {
        mirror=m;

        if(mirror)
            init(QuadUVDataMirror);
        else
            init(QuadUVData);
    }

    public void setDirection(Direction dir)
    {
        if(dir==Direction.Horz)
        {
            if(mirror)
                setData(QuadUVDataMirror);
            else
                setData(QuadUVData);
        }
        else
        {
            if(mirror)
                setData(QuadUVDataMirrorV);
            else
                setData(QuadUVDataV);
        }
    }
}
