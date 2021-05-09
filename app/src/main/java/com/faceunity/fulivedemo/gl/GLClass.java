package com.faceunity.fulivedemo.gl;

import android.opengl.GLES20;
import android.util.Log;

public class GLClass
{
    String TAG="GLClass";

    public GLClass(String tag)
    {
        TAG=tag;
    }

    protected void ClearGLError()
    {
        GLES20.glGetError();
    }

    protected void CheckGLError(String op)
    {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
