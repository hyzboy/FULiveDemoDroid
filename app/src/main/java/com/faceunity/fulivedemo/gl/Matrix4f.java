package com.faceunity.fulivedemo.gl;

public class Matrix4f
{
    private float mat[]=null;

    private float vp_w=1.0f,vp_h=1.0f;
    private float width=0,height=0;

    public float[] GetData(){return mat;}

    Matrix4f()
    {
        mat=new float[16];

        identity();
    }

    private void set(float v)
    {
        for(int i=0;i<16;i++)
            mat[i]=v;
    }

    public void SetViewport(float w,float h)
    {
        vp_w=w;
        vp_h=h;
    }

    public void SetImageSize(float w,float h)
    {
        width=w;
        height=h;
    }

    public void identity()
    {
        set(0.0f);

        float scale=1.0f;

        if(width!=0.0f||height!=0.0f)
        {
            float vp_rate = vp_w / vp_h;
            float rate = width / height;

            if (vp_rate > rate)
            {
                //假设屏幕这21:9，则vp_rate=2.33
                //而摄像机为16:9，则rate=1.77
                //这种情况需要将画面等比放大2.33/1.77倍之后，上下空出一定范围

                scale = vp_rate / rate;
            }
            else
            {
                //假设屏幕为16:10,,,则vp_rate=16/10=1.6
                //而摄像机为16:9 则rate=1.77
                //这种情况需将画面等比放大1.77/1.6倍之后，左右空出一定范围

                scale = rate/vp_rate;        //10/9=1.11
            }
        }

        mat[0]=mat[5]=scale;
        mat[10]=mat[15]=1.0f;
    }

    public void ortho(float w,float h)
    {
        set(0.0f);

        mat[ 0]=2.0f/w;
        mat[ 5]=2.0f/h;
        mat[10]=1.0f;

        mat[12]=-1.0f;
        mat[13]=-1.0f;
        mat[14]=0.0f;
        mat[15]=1.0f;
    }
}
