precision highp float;

varying vec2 vTextureCoord;
uniform sampler2D sTexture;

void main()
{
  gl_FragColor = vec4(texture2D(sTexture, vTextureCoord).rgb,1.0);
}