/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
package tech.antibytes.kmock.example

import kotlin.native.concurrent.ThreadLocal
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.Relaxer

@ThreadLocal
internal object Fixture {
    val fixture = kotlinFixture()
}

@Relaxer
internal inline fun <reified T> relax(
    @Suppress("UNUSED_PARAMETER") id: String
): T = Fixture.fixture.fixture()
