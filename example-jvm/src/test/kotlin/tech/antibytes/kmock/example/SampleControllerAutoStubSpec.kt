/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example

import co.touchlab.stately.concurrency.AtomicReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.kmock.MagicStub
import tech.antibytes.kmock.Verifier
import tech.antibytes.kmock.example.contract.ExampleContract
import tech.antibytes.kmock.example.contract.ExampleContract.SampleDomainObject
import tech.antibytes.kmock.example.contract.ExampleContract.SampleLocalRepository
import tech.antibytes.kmock.example.contract.ExampleContract.SampleRemoteRepository
import tech.antibytes.kmock.example.contract.SampleDomainObjectStub
import tech.antibytes.kmock.example.contract.SampleLocalRepositoryStub
import tech.antibytes.kmock.example.contract.SampleRemoteRepositoryStub
import tech.antibytes.kmock.verify
import tech.antibytes.kmock.verifyOrder
import tech.antibytes.kmock.verifyStrictOrder
import tech.antibytes.kmock.wasCalledWithArguments
import tech.antibytes.kmock.wasCalledWithArgumentsStrict
import tech.antibytes.kmock.wasCalledWithoutArguments
import tech.antibytes.kmock.wasGotten
import tech.antibytes.kmock.wasSet
import tech.antibytes.kmock.wasSetTo
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

@MagicStub(
    SampleRemoteRepository::class,
    SampleLocalRepository::class,
    SampleDomainObject::class,
)
class SampleControllerAutoStubSpec {
    private val fixture = kotlinFixture()
    private var verifier = Verifier()
    private var local = SampleLocalRepositoryStub(verifier)
    private var remote = SampleRemoteRepositoryStub(verifier)
    private var domainObject = SampleDomainObjectStub(verifier)

    @BeforeEach
    fun setUp() {
        verifier.clear()
        local.clear()
        remote.clear()
        domainObject.clear()
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

        domainObject.idProp.getMany = id
        domainObject.valueProp.get = number

        remote.fetchFun.returnValue = domainObject
        local.storeFun.returnValue = domainObject

        // When
        val controller = SampleController(local, remote)
        return runBlockingTestWithTimeout {
            val actual = controller.fetchAndStore(url)

            // Then
            actual mustBe domainObject

            verify(exactly = 1) { remote.fetchFun.wasCalledWithArgumentsStrict(url) }
            verify(exactly = 1) { local.storeFun.wasCalledWithArgumentsStrict(id[1], number) }

            verifier.verifyStrictOrder {
                wasCalledWithArgumentsStrict(remote.fetchFun, url)
                wasGotten(domainObject.idProp)
                wasSet(domainObject.idProp)
                wasGotten(domainObject.idProp)
                wasGotten(domainObject.valueProp)
                wasCalledWithArgumentsStrict(local.storeFun, id[1], number)
            }

            verifier.verifyOrder {
                wasCalledWithArguments(remote.fetchFun, url)
                wasSetTo(domainObject.idProp, "42")
                wasCalledWithArguments(local.storeFun, id[1])
            }
        }
    }

    @Test
    fun `Given find it fetches a DomainObjects`(): AsyncTestReturnValue {
        // Given
        val idOrg = fixture.fixture<String>()
        val id = fixture.fixture<String>()
        val number = fixture.fixture<Int>()

        domainObject.idProp.get = id
        domainObject.valueProp.get = number

        remote.findFun.returnValue = domainObject
        local.containsFun.sideEffect = { true }
        local.fetchFun.returnValue = domainObject

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

            verify(exactly = 1) { local.containsFun.wasCalledWithArgumentsStrict(idOrg) }
            verify(exactly = 1) { local.fetchFun.wasCalledWithArgumentsStrict(id) }
            verify(exactly = 1) { remote.findFun.wasCalledWithArgumentsStrict(idOrg) }

            verifier.verifyStrictOrder {
                wasCalledWithArgumentsStrict(local.containsFun, idOrg)
                wasCalledWithArgumentsStrict(remote.findFun, idOrg)
                wasGotten(domainObject.idProp)
                wasCalledWithArgumentsStrict(local.fetchFun, id)
                wasSet(domainObject.idProp)
            }

            verifier.verifyOrder {
                wasCalledWithoutArguments(local.containsFun, "abc")
            }
        }
    }
}
