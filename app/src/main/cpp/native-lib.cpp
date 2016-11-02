#include <jni.h>
#include <string>

extern "C"
jstring
Java_lt_petabitas_kainoskalkuliatorius_Kaina_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
