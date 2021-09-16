package com.faceunity.fulivedemo.gl;

public class QuadUV extends GL2FloatBuffer
{
    public enum Direction
    {
        Horz,   //横屏
        Vert,   //坚屏
        R180,   //转180度
        R180V,  //坚屏转180度
        Custom, //自定义
    }

    /*
        2--3
        |  |
        0--1
     */

    private final float LB[]={0.0f,1.0f};
    private final float RB[]={1.0f,1.0f};
    private final float LT[]={0.0f,0.0f};
    private final float RT[]={1.0f,0.0f};

    private final float R180UVData[][]=         {LT,RT,LB,RB};
    private final float R180MirrorUVData[][]=   {RT,LT,RB,LB};
    private final float R180UVDataV[][]=        {RT,RB,LT,LB};
    private final float R180MirrorUVDataV[][]=  {LT,LB,RT,RB};

    private final float QuadUVData[][] =        {LB,RB,LT,RT};
    private final float QuadUVDataMirror[][] =  {RB,LB,RT,LT};
    private final float QuadUVDataV[][] =       {LT,LB,RT,RB};
    private final float QuadUVDataMirrorV[][] = {LB,LT,RB,RT};

    private boolean mirror=false;
    private Direction direction=Direction.Horz;
    private float scale_x=1.0f,scale_y=1.0f;
    private float offset_x=0.0f,offset_y=0.0f;

    private float FinalData[]=new float[8];

    public QuadUV(boolean m)
    {
        mirror=m;

        ProcData();
        init(FinalData);
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
            FinalData[pos]=ComputeScale(origin[pos][0],scale_x,offset_x);
            ++pos;
            FinalData[pos]=ComputeScale(origin[pos][1],scale_y,offset_y);
            ++pos;
        }
    }

    private void ProcData()
    {
        if(direction==Direction.Horz)
        {
            if(mirror)
                ScaleData(QuadUVDataMirror);
            else
                ScaleData(QuadUVData);
        }
        else if(direction==Direction.Vert)
        {
            if(mirror)
                ScaleData(QuadUVDataMirrorV);
            else
                ScaleData(QuadUVDataV);
        }
        else if(direction==Direction.R180)
        {
            if(mirror)
                ScaleData(R180MirrorUVData);
            else
                ScaleData(R180UVData);
        }
        else if(direction==Direction.R180V)
        {
            if(mirror)
                ScaleData(R180MirrorUVDataV);
            else
                ScaleData(R180UVDataV);
        }
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

    public void setCustom(float l,float t,float w,float h)
    {
        direction=Direction.Custom;

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
