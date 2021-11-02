package com.faceunity.fulivedemo.gl;

import android.opengl.GLES20;
import android.util.Log;

public abstract class ShaderModule extends GLClass
{
    private final String TAG = "ShaderModule";

    private final String mVertexShader =
                      "attribute vec2 aPosition;\n"
                    + "attribute vec2 aTextureCoord;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform int direction;\n"
                    + "uniform int mirror;\n"
                    + "void main()\n"
                    + "{\n"
                    + "  float x=aTextureCoord.x;\n"
                    + "  float y=aTextureCoord.y;\n"
                    + "\n"
                    + "  if(direction==0)\n"
                    + "  {\n"
                    + "    x=aTextureCoord.x;\n"
                    + "    y=aTextureCoord.y;\n"
                    + "  }\n"
                    + "  else\n"
                    + "  {\n"
                    + "    x=1.0f-aTextureCoord.y;\n"
                    + "    y=1.0f-aTextureCoord.x;\n"
                    + "  }\n"
                    + "\n"
                    + "  vTextureCoord = vec2(x,y);\n"
                    + "\n"
                    + "  if(mirror==1)\n"
                    + "    gl_Position = vec4(-aPosition.x,aPosition.y,0.0f,1.0f);\n"
                    + "  else\n"
                    + "    gl_Position = vec4(aPosition,0.0f,1.0f);\n"
                    + "}\n";

    protected int mProgram = -1;

    public int maPositionHandle;
    public int maTexCoordHandle;
    public int maDirectionHandle;
    public int maMirrorHandle;

    public ShaderModule(String tag)
    {
        super(tag);
    }

    private int loadShader(int shaderType, String source) {
        GLES20.glGetError();
        
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0)
        {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);

            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

            if (compiled[0] == 0)
            {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        else
            CheckGLError("CreateShader");

        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertex_shader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertex_shader == 0) {
            return 0;
        }
        int fragment_shader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragment_shader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0)
        {
            GLES20.glAttachShader(program, vertex_shader);
            CheckGLError("glAttachShader vertex");
            GLES20.glAttachShader(program, fragment_shader);
            CheckGLError("glAttachShader fragment");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public boolean init(String mFragmentShader) {
        mProgram = createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) return (false);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        CheckGLError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            return (false);
        }

        maTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        CheckGLError("glGetAttribLocation aTextureCoord");
        if (maTexCoordHandle == -1) {
            return (false);
        }

        maDirectionHandle = GLES20.glGetUniformLocation(mProgram,"direction");
        CheckGLError("glGetUniformLocation direction");
        if(maDirectionHandle==-1)
            return(false);

        maMirrorHandle = GLES20.glGetUniformLocation(mProgram,"mirror");
        CheckGLError("glGetUniformLocation mirror");
        if(maMirrorHandle==-1)
            return(false);

        return (true);
    }

    public abstract boolean init();

    public void begin()
    {
        ClearGLError();
        GLES20.glUseProgram(mProgram);
        CheckGLError("after glUseProgram()");
    }

    public void end() {
        GLES20.glUseProgram(0);
    }

    public enum Direction
    {
        Horz,   //横屏
        Vert   //坚屏
    }

    public void SetDirection(Direction dir)
    {
        GLES20.glUniform1i(maDirectionHandle,dir==Direction.Horz?0:1);
    }
    public void SetMirror(boolean mirror)
    {
        GLES20.glUniform1i(maMirrorHandle,mirror?1:0);
    }
}
