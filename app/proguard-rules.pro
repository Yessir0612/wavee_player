# Add project-specific ProGuard rules here.
# By default most rules from sdk/tools/proguard/proguard-android-optimize.txt are applied.

# Kotlinx serialization — keep generated serializers
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.vibewave.**$$serializer { *; }
-keepclassmembers class com.vibewave.** {
    *** Companion;
}
-keepclasseswithmembers class com.vibewave.** {
    kotlinx.serialization.KSerializer serializer(...);
}
