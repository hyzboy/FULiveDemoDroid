package com.faceunity.fulivedemo.drawobject;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.GL2Texture;

/**
 * 绘制位图
 */
public class DrawBitmap extends DrawObject
{
    private GL2Texture texture=new GL2Texture();
    private float surface_width=-1;
    private float surface_height=-1;
    private float bmp_width=-1;
    private float bmp_height=-1;
    private float draw_left=0.0f;
    private float draw_top=0.0f;
    private float scale_width=1.0f;
    private float scale_height=1.0f;

    public DrawBitmap()
    {
        super(ObjectType.Bitmap,new ShaderAlpha());

        texture.init(GLES20.GL_TEXTURE_2D);
    }

    public void setSurfaceSize(int w,int h)
    {
        surface_width=w;
        surface_height=h;
    }

    private void updateLayout()
    {
        render_layout.set(  draw_left/surface_width,
                            draw_top/surface_height,
                            (bmp_width*scale_width)/surface_width,
                            (bmp_height*scale_height)/surface_height);
    }

    public void setBitmap(Bitmap bmp,int rotate)
    {
        texture.set(bmp);
        bmp_width=bmp.getWidth();
        bmp_height=bmp.getHeight();

        updateLayout();
    }

    public void setLayout(float l,float t,float w,float h)
    {
        draw_left=l;
        draw_top=t;
        scale_width=w;
        scale_height=h;

        updateLayout();
    }

    @Override
    public void start(){}

    @Override
    public void update(){}

    @Override
    public void draw()
    {
        ClearGLError();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        shader.begin();
            texture.bind(0);
            shader.BindPosition(render_layout);
            shader.BindTexCoord(texture_uv);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();

        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
