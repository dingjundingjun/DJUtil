#ifndef __USCANNER_LOG_H__
#define __USCANNER_LOG_H__

#include <android/log.h>

#define LOG_TAG "handwrite_native"

#if 1
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#else
#define LOGV(...) do { } while(0)
#define LOGD(...) do { } while(0)
#define LOGI(...) do { } while(0)
#define LOGW(...) do { } while(0)
#define LOGE(...) do { } while(0)

#define LOGEE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif

#endif /* __USCANNER_LOG_H__ */
