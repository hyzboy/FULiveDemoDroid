#extension GL_OES_EGL_image_external : require
precision highp float;

varying vec2 vTextureCoord;
uniform samplerExternalOES sTextureRGB;
uniform samplerExternalOES sTextureAlpha;

void main()
{
  vec3  rgb  =texture2D(sTextureRGB,   vTextureCoord).rgb;
  float alpha=texture2D(sTextureAlpha, vTextureCoord).r;

  gl_FragColor = vec4(rgb,alpha);
}