/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example.contract

import kotlin.test.Test
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils

@OptIn(KMockExperimental::class)
@KMock(ExampleContractJs.JsDecoder::class)
class JsDecoderAutoSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils JsDecoder`() {
        JsDecoderMock() fulfils ExampleContractJs.JsDecoder::class
    }
}
