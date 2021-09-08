package com.faceunity.fulivedemo.renderer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.faceunity.fulivedemo.async.GL2Event;
import com.faceunity.fulivedemo.async.GL2EventSetBitmap;
import com.faceunity.fulivedemo.async.GL2EventSetLayout;
import com.faceunity.fulivedemo.drawobject.DrawBitmap;
import com.faceunity.fulivedemo.drawobject.DrawGIF;
import com.faceunity.fulivedemo.drawobject.DrawObject;
import com.faceunity.fulivedemo.drawobject.DrawText;
import com.faceunity.fulivedemo.drawobject.DrawTextureAlpha;
import com.faceunity.fulivedemo.drawobject.DrawVideo;
import com.faceunity.fulivedemo.gl.GL2FBO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DrawManager
{
    private GL2FBO fbo=null;

    private final int MAX_SCREEN_WIDTH  =720;   //最大屏幕宽度
    private final int SCREEN_SIZE_ALIGN =16;    //宽高对齐象素

    private final int FOREGROUND_OBJECT_COUNT=2;
    private final int BACKGROUND_OBJECT_COUNT=2;
    private final int MAX_DRAW_OBJECT=FOREGROUND_OBJECT_COUNT+BACKGROUND_OBJECT_COUNT;

    private Activity activity;
    private int screen_width,screen_height;
    private DrawObject draw_object[]={null,null,null,null};
    private Queue<GL2Event> event_queue=new LinkedList<GL2Event>();
    private Vector<DrawText> draw_text_list=new Vector<DrawText>();

    DrawManager(){}

    public final int GetMaxDrawObject()
    {
        return MAX_DRAW_OBJECT;
    }

    private void RunAsyncEvent()
    {
        if(event_queue.size()==0)return;

        for(GL2Event e:event_queue)
            e.run();

        event_queue.clear();
    }

    public void update()
    {
        RunAsyncEvent();

        for(int i=0;i<BACKGROUND_OBJECT_COUNT;i++)
        {
            if(draw_object[i]!=null){
                draw_object[i].update();
            }
        }
    }

    public void onDrawBackground()
    {
        GLES20.glGetError();        //清空错误

        fbo.Begin();

        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        for(int i=0;i<BACKGROUND_OBJECT_COUNT;i++)
        {
            if(draw_object[i]!=null){
                draw_object[i].draw();
            }
        }

        fbo.End();
    }

    public void onDrawForeground()
    {
        GLES20.glGetError();        //清空错误

        fbo.Begin();

        for(int i=BACKGROUND_OBJECT_COUNT;
                i<BACKGROUND_OBJECT_COUNT+FOREGROUND_OBJECT_COUNT;i++)
        {
            if(draw_object[i]!=null){
                draw_object[i].draw();
            }
        }

        for(DrawText dt:draw_text_list)
            dt.draw();

        fbo.End();
    }

    public int GetTextureID()
    {
        return fbo.GetTextureID();
    }

    private int GetAlign(int value)
    {
        return ((value+ SCREEN_SIZE_ALIGN -1)/ SCREEN_SIZE_ALIGN)* SCREEN_SIZE_ALIGN;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) throws IOException
    {
        screen_width=width;
        screen_height=height;

        int fbo_width;
        int fbo_height;

        if(screen_width>MAX_SCREEN_WIDTH)
        {
            fbo_width=MAX_SCREEN_WIDTH;

            double tmp=MAX_SCREEN_WIDTH;
            tmp/=(double)screen_width;
            tmp*=(double)screen_height;

            fbo_height=GetAlign((int)tmp);
        }
        else
        {
            fbo_width=GetAlign(screen_width);
            fbo_height=GetAlign(screen_height);
        }

        fbo=new GL2FBO();
        fbo.init(fbo_width,fbo_height);

        //创建文字绘制测试对象
        if(draw_text_list.size()<=0)
        {
            DrawText dt=new DrawText();

            dt.setSurfaceSize(width,height);

            dt.setColor(1,1,0,1);
            dt.setPosition(10,10);                  //设置绘制位置
            dt.setSize(24);                              //设置字符大小
            dt.setText("Hello,World! 你好，世界！");      //设置文本

            dt.refresh();                               //刷新内容

            draw_text_list.add(dt);
        }

        //创建视频测试
        if(draw_object[0]==null)
        {
            {
                DrawVideo dv = new DrawVideo(activity.getApplicationContext());
                dv.SetLayout(0, 0, 1, 1);
                //dv.SetDirection(QuadUV.Direction.Vert);

                //假设影片为1920x1080
                //屏幕为1080x1920
                //则实际显示为，高度从1080被放大到了1920即1920/1080=1.777倍，而宽度被从1920缩小到了1080，同为1.777倍。所需做的是，将宽度放大1.777*1.777=3.16倍

                final float movie_width=1920;         //影片宽
                final float movie_height=1080;        //影片高

                //影片高放大倍数
                final float v_zoom_out=screen_height/movie_height;
                //影片宽缩小倍数
                final float h_zoom_in=movie_width/screen_width;

                //最终影片宽所需放大倍数
                final float v_scale=h_zoom_in*v_zoom_out;

                dv.SetScale(v_scale,1.0f);

                dv.SetOffset(1.0f,0);       //设定偏移，-1为最左，+1为最右

                dv.init("/sdcard/Vedio/the boys 720P.mp4");

                draw_object[0] = dv;
            }

//            {
//                DrawVideoAlpha dv = new DrawVideoAlpha(activity.getApplicationContext());
//                dv.SetLayout(0, 0, 1, 1);
//                dv.SetDirection(QuadUV.Direction.Vert);
//                dv.init("/sdcard/Movies/2.mp4", "/sdcard/Movies/2_alpha.mp4");
//
//                draw_object[1] = dv;
//            }

//            {
//                DrawTextureAlpha dv=new DrawTextureAlpha();
//                dv.SetLayout(0, 0, 1, 1);
//                dv.SetDirection(QuadUV.Direction.Vert);
//
//                draw_object[0]=dv;
//            }
//
//            {
//                DrawGIF gif=new DrawGIF();
//
//                gif.Load("/sdcard/Pictures/Img00000285.GIF");
//
//                float gif_width=gif.GetGifWidth();
//                float gif_height=gif.GetGifHeight();
//
//                //使用自定义显示范围
//                {
//                    final float cus_w=0.4f;      //自定义显示范围宽度
//                    final float cus_h=0.4f;      //自定义显示范围高度
//
//                    gif.SetCustomViewScope(0.1f,0.1f,cus_w,cus_h);                //设定自定义显示范围
//
//                    gif_width*=cus_w;
//                    gif_height*=cus_h;
//                }
//
//                float scale=1.0f;
//                float fw=gif_width/(float)screen_width;                   //求出以浮点比例为准的宽
//                float fh=gif_height/(float)screen_height;                 //求出以浮点比例为准的高
//
//                while(fw<0.5f&&fh<0.5f)     //小于一半，太小了，放大
//                {
//                    fw*=2.0f;
//                    fh*=2.0f;
//                }
//
//                gif.SetLayout((1.0f-fw)/2.0f,(1.0f-fh)/2.0f,fw,fh);           //距中
//
//                gif.start();
//                draw_object[1] = gif;
//            }
        }
    }

    public void setFaceUnityTextureID(int id)
    {
        if(draw_object[0].isTextureAlpha())
            ((DrawTextureAlpha)draw_object[0]).setTextureID(id);
    }

    public void onSurfaceCreated(Activity act,GL10 gl, EGLConfig config)
    {
        activity=act;
    }

    public boolean setBitmap(int index,Bitmap bmp,int rotate)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return(false);

        DrawObject obj=draw_object[index];

        if(obj==null)
        {
            DrawBitmap obj_bmp=new DrawBitmap();
            obj_bmp.SetLayout(0,0,1,1);       //设定为全屏

            draw_object[index]=obj_bmp;
            obj_bmp.update(bmp,rotate);
        }
        else
        {
            if(obj.isBitmap())
                return(false);

            DrawBitmap obj_bmp= (DrawBitmap) obj;
            obj_bmp.update(bmp,rotate);
        }

        return(true);
    }

    public void AsyncSetBitmap(int index,Bitmap bmp,int rotate)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return;

        DrawObject obj=draw_object[index];

        if(obj==null)return;

        event_queue.add(new GL2EventSetBitmap(obj,bmp,rotate));
    }

    public boolean setLayout(int index,float left,float top,float width,float height)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return(false);
        DrawObject obj=draw_object[index];
        if(obj==null)return(false);

        obj.SetLayout(left,top,width,height);
        return(true);
    }

    public void AsyncSetLayout(int index,float left,float top,float width,float height)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return;
        DrawObject obj=draw_object[index];
        if(obj==null)return;

        event_queue.add(new GL2EventSetLayout(obj,left,top,width,height));
    }
}
