uniform int loops;
uniform float stepSize, sliceSize, viewWidth, viewHeight;
uniform sampler3D intensityVol;
uniform sampler2D backFace;
uniform vec3 clearColor;
uniform bool doBlur;
void main()
{
    // get normalized pixel coordinate in view port (e.g. [0,1]x[0,1])
    vec2 pixelCoord = gl_FragCoord.st;
    pixelCoord.x /= viewWidth;
    pixelCoord.y /= viewHeight;
    // starting position of the ray is stored in the texture coordinate
    vec3 start = gl_TexCoord[1].xyz;
    vec3 backPosition = texture2D(backFace,pixelCoord).xyz;
    vec3 dir = backPosition - start;
    float len = length(dir);
    dir = normalize(dir);
    vec3 deltaDir = dir * stepSize;
    vec4 colorSample,colAcc = vec4(0.0,0.0,0.0,0.0);
    float lengthAcc = 0.0;
    float opacityCorrection = stepSize/sliceSize;
    //ray dithering http://marcusbannerman.co.uk/index.php/home/42-articles/97-vol-render-optimizations.html
    vec3 samplePos = start.xyz + deltaDir* (fract(sin(gl_FragCoord.x * 12.9898 + gl_FragCoord.y * 78.233) * 43758.5453));
    //offset to eight locations surround target: permute top/bottom, anterior/posterior, left/right
    float dx = 0.5; //distance from target voxel
    vec3 vTAR = vec3( dx, dx, dx)*sliceSize;
    vec3 vTAL = vec3( dx, dx,-dx)*sliceSize;
    vec3 vTPR = vec3( dx,-dx, dx)*sliceSize;
    vec3 vTPL = vec3( dx,-dx,-dx)*sliceSize;
    vec3 vBAR = vec3(-dx, dx, dx)*sliceSize;
    vec3 vBAL = vec3(-dx, dx,-dx)*sliceSize;
    vec3 vBPR = vec3(-dx,-dx, dx)*sliceSize;
    vec3 vBPL = vec3(-dx,-dx,-dx)*sliceSize;
    for(int i = 0; i < loops; i++) {
        if (doBlur) {
            colorSample = texture3D(intensityVol,samplePos+vTAR);
            colorSample += texture3D(intensityVol,samplePos+vTAL);
            colorSample += texture3D(intensityVol,samplePos+vTPR);
            colorSample += texture3D(intensityVol,samplePos+vTPL);
            colorSample += texture3D(intensityVol,samplePos+vBAR);
            colorSample += texture3D(intensityVol,samplePos+vBAL);
            colorSample += texture3D(intensityVol,samplePos+vBPR);
            colorSample += texture3D(intensityVol,samplePos+vBPL);
            colorSample *= 0.125; //average of 8 sample locations
        } else
            colorSample = texture3D(intensityVol,samplePos);
        colorSample.a = 1.0-pow((1.0 - colorSample.a), opacityCorrection);
        colorSample.rgb *= colorSample.a;
        //accumulate color
        colAcc = (1.0 - colAcc.a) * colorSample + colAcc;
        samplePos += deltaDir;
        lengthAcc += stepSize;
        // terminate if opacity > 95% or the ray is outside the volume
        if ( lengthAcc >= len || colAcc.a > 0.95 ) break;
    }
    colAcc.rgb = mix(clearColor,colAcc.rgb,colAcc.a);
    gl_FragColor = colAcc;
}
