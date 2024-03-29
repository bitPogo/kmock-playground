/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example

import co.touchlab.stately.concurrency.AtomicReference
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.MockShared
import tech.antibytes.kmock.example.contract.ConcurrentContract
import tech.antibytes.kmock.example.contract.ConcurrentThingMock
import tech.antibytes.kmock.example.contract.ExampleContract
import tech.antibytes.kmock.example.contract.ExampleContract.SampleDomainObject
import tech.antibytes.kmock.example.contract.ExampleContract.SampleLocalRepository
import tech.antibytes.kmock.example.contract.ExampleContract.SampleRemoteRepository
import tech.antibytes.kmock.example.contract.SampleDomainObjectMock
import tech.antibytes.kmock.example.contract.SampleLocalRepositoryMock
import tech.antibytes.kmock.example.contract.SampleRemoteRepositoryMock
import tech.antibytes.kmock.verification.NonFreezingVerifier
import tech.antibytes.kmock.verification.Verifier
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.kmock.verification.verify
import tech.antibytes.kmock.verification.verifyOrder
import tech.antibytes.kmock.verification.verifyStrictOrder
import tech.antibytes.util.test.coroutine.AsyncTestReturnValue
import tech.antibytes.util.test.coroutine.clearBlockingTest
import tech.antibytes.util.test.coroutine.defaultTestContext
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeoutInScope
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@MockShared(
    "concurrentTest",
    SampleRemoteRepository::class,
    SampleLocalRepository::class,
    SampleDomainObject::class,
    ExampleContract.DecoderFactory::class,
    ConcurrentContract.ConcurrentThing::class
)
class SampleControllerAutoConcurrentStubSpec {
    private val fixture = kotlinFixture()
    private var verifier = Verifier()
    private var local: SampleLocalRepositoryMock = kmock(verifier)
    private var remote: SampleRemoteRepositoryMock = kmock(verifier)
    private var domainObject: SampleDomainObjectMock = kmock(verifier, relaxed = true)

    @BeforeTest
    fun setUp() {
        verifier.clear()
        local._clearMock()
        remote._clearMock()
        domainObject._clearMock()
        clearBlockingTest()
    }

    @Test
    fun `It fulfils SampleController`() {
        SampleController(local, remote) fulfils ExampleContract.SampleController::class
    }

    @Test
    fun `Given fetchAndStore it fetches and stores DomainObjects`(): AsyncTestReturnValue {
        // Given
        remote._fetch.returnValue = domainObject
        local._store.returnValue = domainObject

        // When
        val controller = SampleController(local, remote)
        return runBlockingTestWithTimeout {
            val actual = controller.fetchAndStore(fixture.fixture())

            // Then
            actual mustBe domainObject

            verify(exactly = 1) { remote._fetch.hasBeenCalled() }
            verify(exactly = 1) { local._store.hasBeenCalled() }

            verifier.verifyStrictOrder {
                remote._fetch.hasBeenCalled()
                domainObject._id.wasGotten()
                domainObject._id.wasSet()
                domainObject._id.wasGotten()
                domainObject._value.wasGotten()
                local._store.hasBeenCalled()
            }

            verifier.verifyOrder {
                remote._fetch.hasBeenCalled()
                domainObject._id.wasSetTo("42")
                local._store.hasBeenCalled()
            }
        }
    }

    @Test
    fun `Given find it fetches a DomainObjects`(): AsyncTestReturnValue {
        // Given
        val idOrg = fixture.fixture<String>()
        val id = fixture.fixture<String>()
        val number = fixture.fixture<Int>()

        domainObject._id.getValue = id
        domainObject._value.getValue = number

        remote._find.returnValue = domainObject
        local._contains.sideEffect = { true }
        local._fetch.returnValue = domainObject

        // When
        val controller = SampleController(local, remote)
        val doRef = AtomicReference(domainObject)
        val contextRef = AtomicReference(defaultTestContext)

        return runBlockingTestWithTimeoutInScope(defaultTestContext) {
            // When
            controller.find(idOrg)
                .onEach { actual -> actual mustBe doRef.get() }
                .launchIn(CoroutineScope(contextRef.get()))

            delay(20)

            verify(exactly = 1) { local._contains.hasBeenStrictlyCalledWith(idOrg) }
            verify(exactly = 1) { local._fetch.hasBeenStrictlyCalledWith(id) }
            verify(exactly = 1) { remote._find.hasBeenStrictlyCalledWith(idOrg) }

            verifier.verifyStrictOrder {
                local._contains.hasBeenStrictlyCalledWith(idOrg)
                remote._find.hasBeenStrictlyCalledWith(idOrg)
                domainObject._id.wasGotten()
                local._fetch.hasBeenStrictlyCalledWith(id)
                domainObject._id.wasSet()
            }

            verifier.verifyOrder {
                local._contains.hasBeenCalledWithout("abc")
            }
        }
    }

    @Test
    fun `Given find it fetches blocking a DomainObjects`() {
        // Given
        val idOrg = fixture.fixture<String>()
        val instance = DomainObject("test", 21)
        val verifier = NonFreezingVerifier()
        val local: SampleLocalRepositoryMock = kmock(verifier, relaxed = true, freeze = false)
        val remote: SampleRemoteRepositoryMock = kmock(verifier, relaxed = true, freeze = false)

        val domainObject: SampleDomainObjectMock = kspy(
            instance,
            verifier,
            freeze = false
        )

        remote._find.returnValue = domainObject
        local._contains.sideEffect = { true }
        local._fetch.returnValue = domainObject

        // When
        val controller = SampleController(local, remote)

        // When
        controller.findBlocking(idOrg)

        verify(exactly = 1) { local._contains.hasBeenStrictlyCalledWith(idOrg) }
        verify(exactly = 1) { local._fetch.hasBeenCalled() }
        verify(exactly = 1) { remote._find.hasBeenStrictlyCalledWith(idOrg) }

        verifier.verifyStrictOrder {
            local._contains.hasBeenStrictlyCalledWith(idOrg)
            remote._find.hasBeenStrictlyCalledWith(idOrg)
            domainObject._id.wasGotten()
            local._fetch.hasBeenCalled()
            domainObject._id.wasSet()
        }

        verifier.verifyOrder {
            local._contains.hasBeenCalledWithout("abc")
        }
    }

    @OptIn(KMockExperimental::class)
    @Test
    fun `Given a arbitrary SourceSetThing it is mocked`() {
        // Given
        val concurrentThing = kmock<ConcurrentThingMock>(relaxUnitFun = true)

        // When
        concurrentThing.doSomething()

        // Then
        assertProxy { concurrentThing._doSomething.hasBeenCalled() }
    }

    private class DomainObject(
        override var id: String,
        override val value: Int
    ) : SampleDomainObject
}
