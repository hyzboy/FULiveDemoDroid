package com.faceunity.fulivedemo.gl;

public class Matrix4f
{
    private float mat[]=null;

    public float[] GetData(){return mat;}

    Matrix4f()
    {
        mat=new float[16];
    }

    private void set(float v)
    {
        for(int i=0;i<16;i++)
            mat[i]=v;
    }

    public void identity()
    {
        set(1.0f);
    }

    public void ortho(float w,float h)
    {
        set(1.0f);

        mat[ 0]=2.0f/w;
        mat[ 5]=2.0f/h;
        mat[10]=1.0f;

        mat[12]=-1.0f;
        mat[13]=-1.0f;
        mat[14]=0.0f;
        mat[15]=1.0f;
    }
}
