#include <jni.h>
#include <libyuv.h>

extern "C" JNIEXPORT void JNICALL Java_com_android_glutil_YUV_abgr2nv21(JNIEnv* env, jobject obj,jbyteArray nv21,jobject argb,jint width,jint height)
{
    jboolean is_copy = false;

    uint8_t *nv21_ptr = (uint8_t * )(env->GetByteArrayElements(nv21, &is_copy));
    uint8_t *argb_ptr = (uint8_t * )(env->GetDirectBufferAddress(argb));

    uint8_t *vu_ptr=nv21_ptr+width*height;

    libyuv::ABGRToNV21( argb_ptr,   width*4,
                        nv21_ptr,   width,
                        vu_ptr,     width,
                        width,      height);
}
