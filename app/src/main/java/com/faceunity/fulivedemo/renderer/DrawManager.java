package com.faceunity.fulivedemo.renderer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import com.faceunity.fulivedemo.async.GL2Event;
import com.faceunity.fulivedemo.async.GL2EventSetBitmap;
import com.faceunity.fulivedemo.async.GL2EventSetLayout;
import com.faceunity.fulivedemo.drawobject.DrawBitmap;
import com.faceunity.fulivedemo.drawobject.DrawObject;
import com.faceunity.fulivedemo.drawobject.DrawText;
import com.faceunity.fulivedemo.drawobject.DrawVideo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DrawManager implements SurfaceTexture.OnFrameAvailableListener
{
    private final int FOREGROUND_OBJECT_COUNT=2;
    private final int BACKGROUND_OBJECT_COUNT=2;
    private final int MAX_DRAW_OBJECT=FOREGROUND_OBJECT_COUNT+BACKGROUND_OBJECT_COUNT;

    private Activity activity;
    private int screen_width,screen_height;
    private DrawObject draw_object[]={null,null,null,null};
    private Queue<GL2Event> event_queue=new LinkedList<GL2Event>();
    private Vector<DrawText> draw_text_list=new Vector<DrawText>();

    private boolean updateSurface = false;

    DrawManager(){}

    public final int GetMaxDrawObject()
    {
        return MAX_DRAW_OBJECT;
    }
    public int GetScreenWidth(){return screen_width;}
    public int GetScreenHeight(){return screen_height;}

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

        synchronized (this)
        {
            if(updateSurface)
            {
                for(int i=0;i<MAX_DRAW_OBJECT;i++)
                    if(draw_object[i]!=null)
                        draw_object[i].update();

                updateSurface = false;
            }
        }
    }

    public void onDrawBackground()
    {
        GLES20.glGetError();        //清空错误

        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        for(int i=0;i<BACKGROUND_OBJECT_COUNT;i++)
        {
            if(draw_object[i]!=null){
                draw_object[i].draw();
            }
        }
    }

    public void onDrawForeground()
    {
        GLES20.glGetError();        //清空错误

        for(int i=BACKGROUND_OBJECT_COUNT;
                i<BACKGROUND_OBJECT_COUNT+FOREGROUND_OBJECT_COUNT;i++)
        {
            if(draw_object[i]!=null){
                draw_object[i].draw();
            }
        }

        for(DrawText dt:draw_text_list)
            dt.draw();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        screen_width=width;
        screen_height=height;

        //创建文字绘制测试对象
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
        {
            DrawVideo dv=new DrawVideo(activity.getApplicationContext(),this);

            dv.SetLayout(0,0,1,1);

            dv.init("/sdcard/Movies/ink_painting.mp4");

            draw_object[3]=dv;
        }
    }

    public void onSurfaceCreated(Activity act,GL10 gl, EGLConfig config)
    {
        activity=act;

        synchronized (this) {
            updateSurface = false;
        }
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

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        updateSurface = true;
    }
}
