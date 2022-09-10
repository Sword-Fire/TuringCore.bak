package net.geekmc.turingcore.extender

inline fun <reified T : Enum<T>> nullableEnumValueOf(str: String): T? {
    return try {
        enumValueOf<T>(str)
    } catch (e: Exception) {
        null
    }
}