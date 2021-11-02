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

    public DrawBitmap()
    {
        super(ObjectType.Bitmap,new ShaderAlpha());

        texture.init(GLES20.GL_TEXTURE_2D);
    }

    @Override
    public void start(){}

    @Override
    public void update(){}

    public void update(Bitmap bmp, int rotate)
    {
        texture.set(bmp);
    }

    @Override
    public void draw()
    {
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
