package com.faceunity.fulivedemo.gl;

public class QuadUV extends GL2FloatBuffer
{
    /*
        2--3
        |  |
        0--1
     */

    private final float LB[]={0.0f,1.0f};
    private final float RB[]={1.0f,1.0f};
    private final float LT[]={0.0f,0.0f};
    private final float RT[]={1.0f,0.0f};

    private final float QuadUVData[][] =        {LB,RB,LT,RT};

    private float vp_w=0.0f,vp_h=0.0f;

    private float scale_x=1.0f,scale_y=1.0f;
    private float offset_x=0.0f,offset_y=0.0f;

    private float FinalData[]=new float[8];

    public QuadUV()
    {
        ProcData();
        init(FinalData);
    }

    /**
     * 设定显示区域宽高(如全屏则为全屏实际象素宽高)
     */
    public void SetViewport(float w,float h)
    {
        vp_w=w;
        vp_h=h;
    }

    private float ComputeScale(float op,float scale,float offset)
    {
        float nl=1.0f/scale;
        float gap=(1.0f-nl)*0.5f;

        return (0.5f+(op-0.5f)*nl)+(offset*gap);
    }

    private void ScaleData(float[][] origin)
    {
        int pos=0;

        for(int i=0;i<4;i++)
        {
            FinalData[pos]=ComputeScale(origin[i][0],scale_x,offset_x);
            ++pos;
            FinalData[pos]=ComputeScale(origin[i][1],scale_y,offset_y);
            ++pos;
        }
    }

    private void ProcData()
    {
        ScaleData(QuadUVData);
    }

    private void updateData()
    {
        ProcData();
        setData(FinalData);
    }

    public void setScale(float x,float y)
    {
        scale_x=x;
        scale_y=y;

        updateData();
    }

    public void setOffset(float ox,float oy)
    {
        offset_x=ox;
        offset_y=oy;

        updateData();
    }

    public void setCustom(float l,float t,float w,float h)
    {
//        direction=Direction.Custom;

/*
        2--3
        |  |
        0--1
     */
        final float b=t+h;
        final float r=l+w;

        FinalData[0]=l;FinalData[1]=b;
        FinalData[2]=r;FinalData[3]=b;
        FinalData[4]=l;FinalData[5]=t;
        FinalData[6]=r;FinalData[7]=t;

        setData(FinalData);
    }
}
