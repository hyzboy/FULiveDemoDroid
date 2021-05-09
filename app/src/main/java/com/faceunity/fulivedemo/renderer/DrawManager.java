package com.faceunity.fulivedemo.renderer;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.faceunity.fulivedemo.async.GL2Event;
import com.faceunity.fulivedemo.async.GL2EventSetBitmap;
import com.faceunity.fulivedemo.async.GL2EventSetLayout;
import com.faceunity.fulivedemo.drawobject.DrawBitmap;
import com.faceunity.fulivedemo.drawobject.DrawObject;
import com.faceunity.fulivedemo.drawobject.DrawText;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DrawManager
{
    private final int MAX_DRAW_OBJECT=4;
    private int screen_width,screen_height;
    private DrawObject draw_object[]={null,null,null,null};
    private int draw_index[];
    private Queue<GL2Event> event_queue=new LinkedList<GL2Event>();
    private Vector<DrawText> draw_text_list=new Vector<DrawText>();

    DrawManager()
    {
        draw_index=new int[MAX_DRAW_OBJECT];

        for(int i=0;i<MAX_DRAW_OBJECT;i++)
            draw_index[i]=i;
    }

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

    public void onDrawFrame(GL10 gl)
    {
        RunAsyncEvent();

        GLES20.glGetError();        //清空错误

        //GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        //GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        int index;

        for(int i=0;i<MAX_DRAW_OBJECT;i++)
        {
            index=draw_index[i];

            if(index<0||index>MAX_DRAW_OBJECT) {
                continue;
            }
            if(draw_object[index]!=null){
                draw_object[index].draw();
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
            dt.setSize(24);                             //设置字符大小
            dt.setText("Hello,World! 你好，世界！");      //设置文本

            dt.refresh();                               //刷新内容

            draw_text_list.add(dt);
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        // Do nothing.
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
    /**
     * 设置绘制顺序
     * @param order
     */
    public void setDrawOrder(int[] order)
    {
        for(int i=0;i<MAX_DRAW_OBJECT;i++)
            draw_index[i]=order[i];
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
