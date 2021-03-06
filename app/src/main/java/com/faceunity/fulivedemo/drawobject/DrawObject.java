package com.faceunity.fulivedemo.drawobject;

import com.faceunity.fulivedemo.gl.GLClass;
import com.faceunity.fulivedemo.gl.QuadUV;
import com.faceunity.fulivedemo.gl.RenderLayout;
import com.faceunity.fulivedemo.gl.ShaderModule;

/**
 * 绘制对象基类
 */
public abstract class DrawObject extends GLClass
{
    protected enum ObjectType
    {
        Bitmap,
        Video,
        VideoAlpha,
        VideoAlphaLR,
        Text,
        TextureAlpha,
        Actor,
        GIF,
    };

    private ObjectType type;

    protected ShaderModule shader=null;
    protected ShaderModule.Direction direction=ShaderModule.Direction.Horz;
    protected boolean mirror=false;
    protected boolean flip=false;

    protected QuadUV texture_uv=null;

    protected RenderLayout render_layout=new RenderLayout();

    public DrawObject(ObjectType ot,ShaderModule sm)
    {
        super("DrawObject:"+ot.name());
        type=ot;
        shader=sm;
        texture_uv=new QuadUV();
        render_layout.init();
    }

    public final ObjectType GetObjectType()
    {
        return type;
    }

    public boolean isBitmap(){return type==ObjectType.Bitmap;}
    public boolean isVideo(){return type==ObjectType.Video;}
    public boolean isVideoAlpha(){return type==ObjectType.VideoAlpha;}
    public boolean isVideoAlphaLR(){return type==ObjectType.VideoAlphaLR;}
    public boolean isText(){return type==ObjectType.Text;}
    public boolean isTextureAlpha(){return type==ObjectType.TextureAlpha;}
    public boolean isGIF(){return type==ObjectType.GIF;}
    public boolean isActor(){return type==ObjectType.Actor;}

    public void SetDirection(ShaderModule.Direction dir){direction=dir;}                            ///<设置屏幕方向
    public void SetMirror(boolean mir){mirror=mir;}                                                 ///<是否镜像
    public void SetFlip(boolean f){flip=f;}                                                         ///<是否翻转

    public void SetLayout(float l,float t,float w,float h)
    {
        render_layout.set(l,t,w,h);
    }
    //public void SetDirection(QuadUV.Direction dir){texture_uv.setDirection(dir);}
    public void SetScale(float x,float y){texture_uv.setScale(x,y);}
    public void SetOffset(float x,float y){texture_uv.setOffset(x,y);}

    public void SetCustomViewScope(float l,float t,float w,float h)
    {
        texture_uv.setCustom(l,t,w,h);
    }

    public void SetSize(int w, int h, int cw, int ch)
    {
        shader.SetSize(w, h, cw,ch);
    }

    public abstract void start();
    public abstract void update();
    public abstract void draw();
}
