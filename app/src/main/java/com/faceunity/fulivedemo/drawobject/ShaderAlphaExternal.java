package com.faceunity.fulivedemo.drawobject;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderAlphaExternal extends ShaderModule
{
    private final String mFragmentShaderAlphaExternal =
            "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
                    + "}\n";

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderAlphaExternal)) {
            return (false);
        }

        return (true);
    }

    public ShaderAlphaExternal()
    {
        super("ShaderAlphaExternal");
        init();
    }
}
