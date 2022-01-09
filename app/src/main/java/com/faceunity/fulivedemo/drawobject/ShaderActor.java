package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderActor extends ShaderModule
{
    private int texture_location=-1;

    private int ChromaCB_location=-1;
    private int ChromaCR_location=-1;
    private int Cutoff_location     =-1;
    private int Feathering_location =-1;
    private int MaskFeathering_location=-1;
    private int Sharpening_location=-1;

    private int pixelWidth_location=-1;
    private int pixelHeight_location=-1;

    private int LumAverage_location =-1;
    private int Despill_location    =-1;
    private int DespillLuminaceAdd_location=-1;

    private float ChromaCB;
    private float ChromaCR;
    private float ColorCutoff           =0.2f;
    private float ColorFeathering       =0.33f;
    private float MaskFeathering        =1.0f;
    private float Sharpening            =0.5f;

    private float pixelWidth            =0;
    private float pixelHeight           =0;

    private float LumAverage            =4.0f;
    private float Despill               =0.75f;
    private float DespillLuminanceAdd   =0.2f;

    @Override
    public boolean init()
    {
        if (!super.init("Actor")) {
            return (false);
        }

        texture_location= GLES20.glGetUniformLocation(mProgram,"sTexture");

        ChromaCB_location       =GLES20.glGetUniformLocation(mProgram,"KeyCB");
        ChromaCR_location       =GLES20.glGetUniformLocation(mProgram,"KeyCR");
        Cutoff_location         =GLES20.glGetUniformLocation(mProgram,"ColorCutoff");
        Feathering_location     =GLES20.glGetUniformLocation(mProgram,"ColorFeathering");
        Sharpening_location     =GLES20.glGetUniformLocation(mProgram,"Sharpening");
        MaskFeathering_location =GLES20.glGetUniformLocation(mProgram,"MaskFeathering");

        pixelWidth_location     =GLES20.glGetUniformLocation(mProgram,"pixelWidth");
        pixelHeight_location    =GLES20.glGetUniformLocation(mProgram,"pixelHeight");

        LumAverage_location         =GLES20.glGetUniformLocation(mProgram,"LumAverage");
        Despill_location            =GLES20.glGetUniformLocation(mProgram,"Despill");
        DespillLuminaceAdd_location =GLES20.glGetUniformLocation(mProgram,"DespillLuminanceAdd");

        SetChromaColor(0.0f,0.75f,0.0f);     //绿色

        return (true);
    }

    public ShaderActor()
    {
        super("ShaderActor");
        init();
    }

    /**
     * 设置抠图颜色
     */
    public void SetChromaColor(float r,float g,float b)
    {
        ChromaCB=(0.5f + -0.168736f*r - 0.331264f*g + 0.5f*b);
        ChromaCR=(0.5f + 0.5f*r - 0.418688f*g - 0.081312f*b);
    }

    /**
     * 颜色裁剪强度
     */
    public void SetColorCutoff(float v)
    {
        ColorCutoff=v;
    }

    /**
     * 边缘羽化强度
     */
    public void SetColorFeathering(float f)
    {
        ColorFeathering=f;
    }

    /**
     * 去溢色最大去除强度(默认0.75)
     * @param despill 最大强度(默认0.75，最小0.0，最大1.0)
     */
    public void SetDespill(float despill){Despill=despill;}

    /**
     * 去溢色亮度恢复比例(去掉绿色后，亮度会有丢失，这里会强制恢复一定值)
     * @param lum_add 比例，默认0.3，最小0.0，最大1.0
     */
    public void SetDespillLumAdd(float lum_add){DespillLuminanceAdd=lum_add;}

    @Override
    public void SetSize(int w, int h, int cw, int ch)
    {
        super.SetSize(w,h,cw,ch);

        pixelWidth=1.0f/cw;
        pixelHeight=1.0f/ch;
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);

        GLES20.glUniform1f(ChromaCB_location,ChromaCB);
        GLES20.glUniform1f(ChromaCR_location,ChromaCR);
        GLES20.glUniform1f(Cutoff_location,ColorCutoff*ColorCutoff);
        GLES20.glUniform1f(Feathering_location,ColorFeathering*ColorFeathering);
        GLES20.glUniform1f(MaskFeathering_location,MaskFeathering);
        GLES20.glUniform1f(Sharpening_location,Sharpening);

        GLES20.glUniform2f(pixelWidth_location,pixelWidth,0.0f);
        GLES20.glUniform2f(pixelHeight_location,0.0f,pixelHeight);

        GLES20.glUniform1f(LumAverage_location,         LumAverage);
        GLES20.glUniform1f(Despill_location,            Despill);
        GLES20.glUniform1f(DespillLuminaceAdd_location, DespillLuminanceAdd);
    }
}
