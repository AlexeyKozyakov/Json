package io.github.alexeykozyakov.json.reflect

import kotlin.reflect.KCallable
import kotlin.reflect.jvm.isAccessible

internal fun<T> KCallable<T>.allowAccessAndCall(vararg args: Any?): T {
    isAccessible = true
    return call(*args)
}
