package com.example.cowrywisetest.utils
import java.util.concurrent.atomic.AtomicBoolean

class Event<out T>(private val content: T) {

    private var _atomicHasBeenHandled = AtomicBoolean(false)

    val hasBeenHandled = _atomicHasBeenHandled.get()

    fun getContentIfNotHandled(): T?{
        return if(_atomicHasBeenHandled.get()) null
        else {
            _atomicHasBeenHandled.set(true)
            content
        }
    }

    fun peekContent(): T = content

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Event<*>) return false
        return content == other.content
    }

    override fun hashCode(): Int {
        return content?.hashCode() ?: 0
    }
}