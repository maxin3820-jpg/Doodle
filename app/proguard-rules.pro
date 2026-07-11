# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-dontwarn kotlinx.**
-keepclassmembers class kotlinx.** { *; }
-keep class androidx.room.** { *; }
