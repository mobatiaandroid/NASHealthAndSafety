package com.nas.healthandsafety.constants

import java.util.concurrent.atomic.AtomicInteger

object NotificationID {
    private val c = AtomicInteger(0)
    val iD: Int
        get() = c.incrementAndGet()
}