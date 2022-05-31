/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example.contract

import co.touchlab.stately.concurrency.AtomicReference
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import platform.Foundation.NSData
import platform.Foundation.create
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.example.SampleController
import tech.antibytes.kmock.example.contract.ExampleContract.SampleDomainObject
import tech.antibytes.kmock.example.contract.ExampleContract.SampleLocalRepository
import tech.antibytes.kmock.example.contract.ExampleContract.SampleRemoteRepository
import tech.antibytes.kmock.example.kmock
import tech.antibytes.kmock.example.kspy
import tech.antibytes.kmock.verification.NonFreezingVerifier
import tech.antibytes.kmock.verification.Verifier
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.kmock.verification.asyncVerify
import tech.antibytes.kmock.verification.asyncVerifyOrder
import tech.antibytes.kmock.verification.asyncVerifyStrictOrder
import tech.antibytes.kmock.verification.verify
import tech.antibytes.kmock.verification.verifyOrder
import tech.antibytes.kmock.verification.verifyStrictOrder
import tech.antibytes.util.test.coroutine.AsyncTestReturnValue
import tech.antibytes.util.test.coroutine.clearBlockingTest
import tech.antibytes.util.test.coroutine.defaultTestContext
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeoutInScope
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(KMockExperimental::class)
@KMock(
    SampleRemoteRepository::class,
    SampleLocalRepository::class,
    SampleDomainObject::class,
    ExampleContract.DecoderFactory::class,
    IosContract.IosThing::class,
    ConcurrentContract.SomethingGenericConcurrent::class
)
class SampleControllerAutoIosStubSpec {
    private val fixture = kotlinFixture()
    private var verifier = Verifier()
    private var local: SampleLocalRepositoryMock = kmock(verifier)
    private var remote: SampleRemoteRepositoryMock = kmock(verifier)
    private var domainObject: SampleDomainObjectMock = kmock(verifier)

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
        val url = fixture.fixture<String>()
        val id = fixture.listFixture<String>(size = 2)
        val number = fixture.fixture<Int>()

        domainObject.propertyProxyOf(domainObject::id).getMany = id
        domainObject.propertyProxyOf(domainObject::value).get = number

        remote.asyncFunProxyOf(remote::fetch).returnValue = domainObject
        local.asyncFunProxyOf(local::store).returnValue = domainObject

        // When
        val controller = SampleController(local, remote)
        return runBlockingTestWithTimeout {
            val actual = controller.fetchAndStore(url)

            // Then
            actual mustBe domainObject

            asyncVerify(exactly = 1) { remote.asyncFunProxyOf(remote::fetch).hasBeenStrictlyCalledWith(url) }
            asyncVerify(exactly = 1) { local.asyncFunProxyOf(local::store).hasBeenStrictlyCalledWith(id[1], number) }

            verifier.asyncVerifyStrictOrder {
                remote.asyncFunProxyOf(remote::fetch).hasBeenStrictlyCalledWith(url)
                domainObject.propertyProxyOf(domainObject::id).wasGotten()
                domainObject.propertyProxyOf(domainObject::id).wasSet()
                domainObject.propertyProxyOf(domainObject::id).wasGotten()
                domainObject.propertyProxyOf(domainObject::value).wasGotten()
                local.asyncFunProxyOf(local::store).hasBeenStrictlyCalledWith(id[1], number)
            }

            verifier.asyncVerifyOrder {
                remote.asyncFunProxyOf(remote::fetch).hasBeenCalledWith(url)
                domainObject.propertyProxyOf(domainObject::id).wasSetTo("42")
                local.asyncFunProxyOf(local::store).hasBeenCalledWith(id[1])
            }
        }
    }

    @Test
    fun `Given find it fetches a DomainObjects`(): AsyncTestReturnValue {
        // Given
        val idOrg = fixture.fixture<String>()
        val id = fixture.fixture<String>()
        val number = fixture.fixture<Int>()

        domainObject.propertyProxyOf(domainObject::id).get = id
        domainObject.propertyProxyOf(domainObject::value).get = number

        remote.syncFunProxyOf(remote::find).returnValue = domainObject
        local.syncFunProxyOf(local::contains).sideEffect = { true }
        local.syncFunProxyOf(local::fetch).returnValue = domainObject

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

            verify(exactly = 1) { local.syncFunProxyOf(local::contains).hasBeenStrictlyCalledWith(idOrg) }
            verify(exactly = 1) { local.syncFunProxyOf(local::fetch).hasBeenStrictlyCalledWith(id) }
            verify(exactly = 1) { remote.syncFunProxyOf(remote::find).hasBeenStrictlyCalledWith(idOrg) }

            verifier.verifyStrictOrder {
                local.syncFunProxyOf(local::contains).hasBeenStrictlyCalledWith(idOrg)
                remote.syncFunProxyOf(remote::find).hasBeenStrictlyCalledWith(idOrg)
                domainObject.propertyProxyOf(domainObject::id).wasGotten()
                local.syncFunProxyOf(local::fetch).hasBeenStrictlyCalledWith(id)
                domainObject.propertyProxyOf(domainObject::id).wasSet()
            }

            verifier.verifyOrder {
                local.syncFunProxyOf(local::contains).hasBeenCalledWithout("abc")
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

        remote.syncFunProxyOf(remote::find).returnValue = domainObject
        local.syncFunProxyOf(local::contains).sideEffect = { true }
        local.syncFunProxyOf(local::fetch).returnValue = domainObject

        // When
        val controller = SampleController(local, remote)

        // When
        controller.findBlocking(idOrg)

        verify(exactly = 1) { local.syncFunProxyOf(local::contains).hasBeenStrictlyCalledWith(idOrg) }
        verify(exactly = 1) { local.syncFunProxyOf(local::fetch).hasBeenCalled() }
        verify(exactly = 1) { remote.syncFunProxyOf(remote::find).hasBeenStrictlyCalledWith(idOrg) }

        verifier.verifyStrictOrder {
            local.syncFunProxyOf(local::contains).hasBeenStrictlyCalledWith(idOrg)
            remote.syncFunProxyOf(remote::find).hasBeenStrictlyCalledWith(idOrg)
            domainObject.propertyProxyOf(domainObject::id).wasGotten()
            local.syncFunProxyOf(local::fetch).hasBeenCalled()
            domainObject.propertyProxyOf(domainObject::id).wasSet()
        }

        verifier.verifyOrder {
            local.syncFunProxyOf(local::contains).hasBeenCalledWithout("abc")
        }
    }

    @Test
    fun `Given a arbitrary SourceSetThing it is mocked`() {
        // Given
        val bytes = ByteArray(23)
        val iosThing = kmock<IosThingMock>(relaxUnitFun = true)
        iosThing.syncFunProxyOf(iosThing::doSomething).returnValue = memScoped {
            NSData.create(
                bytes = allocArrayOf(bytes),
                length = bytes.size.toULong()
            )
        }

        // When
        iosThing.doSomething()

        // Then
        assertProxy { iosThing.syncFunProxyOf(iosThing::doSomething).hasBeenCalled() }
    }

    private class DomainObject(
        override var id: String,
        override val value: Int
    ) : SampleDomainObject
}