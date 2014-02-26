#include <stdlib.h>
#include "jni.h"
#include "..//include/log.h"
extern "C"
{
	#include "../include/recognition.h"

};
static const char *classPathName="com/hanvon/core/StrokeView";

jbyteArray recognition(JNIEnv *env, jobject thiz)
{
	char* result = TestCSAPI();
	int len = getCharLen(result);
	jbyteArray retarray = env->NewByteArray(len);
	env->SetByteArrayRegion(retarray, 0, len,(jbyte *)result);
//	env->CallVoidMethod(thiz, method_onScanResult, retarray);
    return retarray;
}

static JNINativeMethod methods[] = {
    {"recognition", "()[B", (jbyteArray*)recognition},
};

/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
                                 JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        LOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        LOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static int registerNatives(JNIEnv* env)
{
    if (!registerNativeMethods(env, classPathName,
                               methods, sizeof(methods) / sizeof(methods[0]))) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
	jint result = -1;
	JNIEnv *env = NULL;

	LOGI("INFO: JNI_OnLoad");

	if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
		LOGE("ERROR: GetEnv failed");
		goto fail;
	}

	if (registerNatives(env) != JNI_TRUE) {
		LOGE("ERROR: registerNatives failed");
		goto fail;
	}
	result = JNI_VERSION_1_6;

fail:
	return result;
}


