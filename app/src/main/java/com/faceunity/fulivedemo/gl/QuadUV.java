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
    private Direction direction=Direction.Horz;
    private float scale_x=1.0f,scale_y=1.0f;
    private float offset_x=0.0f,offset_y=0.0f;

    private float FinalData[]=new float[8];

    public QuadUV(boolean m)
    {
        mirror=m;

        if(mirror)
            init(QuadUVDataMirror);
        else
            init(QuadUVData);
    }

    private void ScaleData(float[] origin)
    {
        int pos=0;

        for(int i=0;i<4;i++)
        {
            FinalData[pos]=offset_x+0.5f+(origin[pos]-0.5f)/scale_x;
            ++pos;
            FinalData[pos]=offset_y+0.5f+(origin[pos]-0.5f)/scale_y;
            ++pos;
        }

        setData(FinalData);
    }

    private void updateData()
    {
        if(direction==Direction.Horz)
        {
            if(mirror)
                ScaleData(QuadUVDataMirror);
            else
                ScaleData(QuadUVData);
        }
        else
        {
            if(mirror)
                ScaleData(QuadUVDataMirrorV);
            else
                ScaleData(QuadUVDataV);
        }
    }

    public void setScale(float x,float y)
    {
        scale_x=x;
        scale_y=y;

        updateData();
    }

    public void setDirection(Direction dir)
    {
        direction=dir;

        updateData();
    }

    public void setOffset(float ox,float oy)
    {
        offset_x=ox;
        offset_y=oy;

        updateData();
    }
}
