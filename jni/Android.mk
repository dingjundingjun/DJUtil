LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := hwrecogs
LOCAL_SRC_FILES := libHWRecog.a
include $(PREBUILT_STATIC_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_MODULE    := cnocr_mips
#LOCAL_SRC_FILES := libcnocr_mips.a
#include $(PREBUILT_STATIC_LIBRARY)

#LOCAL_CPP_EXTENSION := .cpp
include $(CLEAR_VARS)
LOCAL_MODULE    := libhandwrite

LOCAL_C_INCLUDES := \
    $(JNI_H_INCLUDE) \
    $(LOCAL_PATH)/include $(LOCAL_PATH)
    
LOCAL_SRC_FILES := \
    src/main.cpp \
    src/recognition.c \
LOCAL_CXXFLAGS := -DHAVE_PTHREADS
CFLAGS := -fpic -g 
LOCAL_STATIC_LIBRARIES := hwrecogs
#LOCAL_STATIC_LIBRARIES := cnocr_mips
LOCAL_LDLIBS:=-L$(SYSROOT)/usr/lib -llog
include $(BUILD_SHARED_LIBRARY)