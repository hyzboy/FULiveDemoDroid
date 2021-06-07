package com.faceunity.fulivedemo.drawobject;

import com.faceunity.fulivedemo.gl.ShaderModule;

public class ShaderAlphaExternal extends ShaderModule
{
    private final String mFragmentShaderAlphaExternal =
            "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTextureRGB;\n"
                    + "uniform samplerExternalOES sTextureAlpha;\n"
                    + "void main() {\n"
                    + "  gl_FragColor = vec4(texture2D(sTextureRGB,   vTextureCoord).rgb,"
                    + "                      texture2D(sTextureAlpha, vTextureCoord).r);\n"
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
