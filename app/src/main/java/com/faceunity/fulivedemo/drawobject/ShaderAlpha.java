package com.faceunity.fulivedemo.drawobject;

import android.opengl.GLES20;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderAlpha extends ShaderModule {

    private final String mFragmentShaderOpaque =
                      "precision highp float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform sampler2D sTexture;\n"
                    + "void main() {\n"
                    + "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
                    + "}\n";

    private int texture_location=-1;

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderOpaque)) {
            return (false);
        }

        texture_location=GLES20.glGetUniformLocation(mProgram,"sTexture");

        return (true);
    }

    public ShaderAlpha()
    {
        super("ShaderAlpha");
        init();
    }

    @Override
    public void begin()
    {
        super.begin();

        GLES20.glUniform1i(texture_location,0);
    }
}
