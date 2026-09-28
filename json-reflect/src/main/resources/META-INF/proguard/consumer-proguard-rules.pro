# JsonModel implementation name, constructors, properties and fields
-keepnames class ** implements io.github.alexeykozyakov.json.reflect.JsonModel

-keepclassmembers class ** implements io.github.alexeykozyakov.json.reflect.JsonModel {
    <init>(...);
    <fields>;
    *** get*();
    *** is*();
}

# JsonName and JsonSkip annotations
-keep @interface io.github.alexeykozyakov.json.reflect.JsonName
-keep @interface io.github.alexeykozyakov.json.reflect.JsonSkip

-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations,RuntimeInvisibleParameterAnnotations

# JsonMapper implementation, its functions, and instance fields

-keep, allowoptimization, includedescriptorclasses class ** implements io.github.alexeykozyakov.json.reflect.JsonMapper {
    *** toJson(...);
    *** fromJson(...);
    *** INSTANCE;
    *** $$INSTANCE;
}
