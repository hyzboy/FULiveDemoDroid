package com.faceunity.fulivedemo.async;

import android.graphics.Bitmap;

import com.faceunity.fulivedemo.drawobject.DrawBitmap;
import com.faceunity.fulivedemo.drawobject.DrawObject;

public class GL2EventSetBitmap extends GL2Event
{
    private Bitmap bmp;
    private int rotate;

    public GL2EventSetBitmap(DrawObject obj, Bitmap b, int r)
    {
        super(obj);

        bmp=b;
        rotate=r;
    }

    @Override
    public void run() {
        ((DrawBitmap)draw_object).setBitmap(bmp,rotate);
    }
}
