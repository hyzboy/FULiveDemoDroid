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
        int error = GLES20.glGetError();

        if(error != GLES20.GL_NO_ERROR)
        {
            if(error==GLES20.GL_INVALID_ENUM        )Log.e(TAG,op+": GL_INVALID_ENUM");else
            if(error==GLES20.GL_INVALID_VALUE       )Log.e(TAG,op+": GL_INVALID_VALUE");else
            if(error==GLES20.GL_INVALID_OPERATION   )Log.e(TAG,op+": GL_INVALID_OPERATION");else
            if(error==GLES20.GL_OUT_OF_MEMORY       )Log.e(TAG,op+": GL_OUT_OF_MEMORY");else
                Log.e(TAG, op + ": glError " + error);

            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
