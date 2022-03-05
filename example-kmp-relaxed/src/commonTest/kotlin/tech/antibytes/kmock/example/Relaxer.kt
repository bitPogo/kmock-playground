/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
package tech.antibytes.kmock.example

import tech.antibytes.kmock.Relaxer
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal object Fixture {
    val fixture = kotlinFixture()
}

@Relaxer
internal inline fun <reified T> relax(
    @Suppress("UNUSED_PARAMETER") id: String
): T = Fixture.fixture.fixture()
