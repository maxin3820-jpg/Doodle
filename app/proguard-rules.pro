# Kotlin
-keepattributes *Annotation*
-dontwarn kotlinx.**
-keepclassmembers class kotlinx.** { *; }

# Room
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# Hilt / Dagger
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# DataStore
-keep class androidx.datastore.** { *; }

# Compose — keep lambda classes stable
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Suppress warnings
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
