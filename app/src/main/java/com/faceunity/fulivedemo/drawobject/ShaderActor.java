package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderActor extends ShaderModule
{
    private int texture_location=-1;

    private int ChromaColor_location=-1;
    private int ChromaScope_location=-1;
    private int ChromaLuminance_location=-1;

    private int LumAverage_location =-1;
    private int Despill_location    =-1;
    private int DespillLuminaceAdd_location=-1;

    private float ChromaColor=-135.0f;
    private float ChromaScope=45f;
    private float ChromaLuminance[]=new float[]{0.15f,0.95f};

    private float LumAverage            =4.0f;
    private float Despill               =0.75f;
    private float DespillLuminanceAdd   =0.3f;

    @Override
    public boolean init()
    {
        if (!super.init("Actor")) {
            return (false);
        }

        texture_location= GLES20.glGetUniformLocation(mProgram,"sTexture");

        ChromaColor_location        =GLES20.glGetUniformLocation(mProgram,"ChromaColor");
        ChromaScope_location        =GLES20.glGetUniformLocation(mProgram,"ChromaScope");
        ChromaLuminance_location    =GLES20.glGetUniformLocation(mProgram,"ChromaLuminance");

        LumAverage_location         =GLES20.glGetUniformLocation(mProgram,"LumAverage");
        Despill_location            =GLES20.glGetUniformLocation(mProgram,"Despill");
        DespillLuminaceAdd_location =GLES20.glGetUniformLocation(mProgram,"DespillLuminanceAdd");

        return (true);
    }

    public ShaderActor()
    {
        super("ShaderActor");
        init();
    }

    public float GetChromaScope(){return ChromaScope;}
    public float GetChromaBlack(){return ChromaLuminance[0];}
    public float GetChromaWhite(){return ChromaLuminance[1];}

    /**
     * 设置抠图色彩范围(默认45，最小0，最大180)，45为全部绿色，180为所有颜色
     */
    public void SetChromaScope(float scope){ChromaScope=scope;}

    /**
     * 设置抠图亮度范围
     * @param black 最小亮度，默认0.15，小于0.15的视为黑色不抠图
     * @param white 最大亮度，默认0.95，大于0.95的视为白色不抠图
     */
    public void SetChromaLuminance(float black,float white)
    {
        ChromaLuminance[0]=black;
        ChromaLuminance[1]=white;
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
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);

        GLES20.glUniform1f(ChromaColor_location,ChromaColor);
        GLES20.glUniform1f(ChromaScope_location,ChromaScope);
        GLES20.glUniform2f(ChromaLuminance_location,ChromaLuminance[0],ChromaLuminance[1]);

        GLES20.glUniform1f(LumAverage_location,         LumAverage);
        GLES20.glUniform1f(Despill_location,            Despill);
        GLES20.glUniform1f(DespillLuminaceAdd_location, DespillLuminanceAdd);
    }
}
