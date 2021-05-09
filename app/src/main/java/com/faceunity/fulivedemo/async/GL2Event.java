package com.faceunity.fulivedemo.async;

import com.faceunity.fulivedemo.drawobject.DrawObject;

public abstract class GL2Event {

    protected DrawObject draw_object;

    public GL2Event(DrawObject obj)
    {
        draw_object=obj;
    }

    public abstract void run();
};

