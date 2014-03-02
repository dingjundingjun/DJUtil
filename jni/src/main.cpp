#include <stdlib.h>
#include "jni.h"
#include "..//include/log.h"
extern "C"
{
	#include "../include/recognition.h"

};
static const char *classPathName="com/hanvon/core/StrokeView";

jbyteArray recognition(JNIEnv *env, jobject thiz,jshortArray point)
{
	short *p = env->GetShortArrayElements(point,0);
	char* result = CSAPI(p);

	int i = 0;
	for(i=0;i<1024;i++)
	{
		if(result[i] == 0 && result[i+1] == 0 && result[i+2] == 0 && result[i+3] == 0)
			break;
	}
//		int len = getCharLen(result);
		int len = i +  4;
		jbyteArray retarray = env->NewByteArray(len);
		env->SetByteArrayRegion(retarray, 0, len,(jbyte *)result);


//	int len = i + 2;
//	jcharArray retarray = env->NewCharArray(len);
//	env->SetCharArrayRegion(retarray, 0, len,(jchar *)result);
//	env->CallVoidMethod(thiz, method_onScanResult, retarray);
    return retarray;
}

static JNINativeMethod methods[] = {
		{"recognition", "([S)[B", (jbyteArray*)recognition}
//    {"recognition", "([S)[C", (jcharArray*)recognition},
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




