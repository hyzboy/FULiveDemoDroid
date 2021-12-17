precision highp float;

varying vec2 vTextureCoord;
uniform vec4 vColor;
uniform sampler2D sTexture;

void main()
{
  float alpha=texture2D(sTexture,vTextureCoord).a;
  gl_FragColor = vec4(vColor.rgb,vColor.a*alpha);
}