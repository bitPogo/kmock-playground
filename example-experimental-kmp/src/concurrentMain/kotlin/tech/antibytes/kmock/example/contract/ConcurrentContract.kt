/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example.contract

interface ConcurrentContract {
    interface ConcurrentThing {
        fun doSomething()
    }

    interface SomethingGenericConcurrent<T> {
        fun doSomething(): T
    }
}
