package com.jsevilla.movieviewer.data.base

import com.jsevilla.movieviewer.data.di.mapperDataModule
import com.jsevilla.movieviewer.data.di.repositoryModule
import com.jsevilla.movieviewer.data.mock.fakeNetworkModule
import com.jsevilla.movieviewer.domain.di.useCasesModule
import com.jsevilla.movieviewer.domain.entity.Failure
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

/**
 * Created by Jose Sevilla on 20/08/2020.
 * jose1.sevilla23@gmail.com
 *
 * Movie Viewer
 * Lima, Peru.
 **/

abstract class BaseUseCaseUniTest : AutoCloseKoinTest() {
    @Before
    fun before() {
        stopKoin()
        startKoin {
            modules(
                arrayListOf(
                    fakeNetworkModule,
                    mapperDataModule,
                    repositoryModule,
                    useCasesModule
                )
            )
        }
    }

    protected fun <T> printUseCaseSuccessObject(someDataObject: T) {
        println("Use case invocation: Success!")
        println(someDataObject)
    }

    protected fun <T> printUseCaseSuccessList(someList: List<T>) {
        println("Use case invocation: Success!")
        println("List size: ${someList.size}")
        println("List content:")
        someList.forEach { println(it) }
    }

    protected fun printUseCaseFailure(error: Failure) {
        println("Use case invocation: Failure :(")
        when (error) {
            is Failure.None -> throw Exception("Ups! Something went REALLY wrong. Contact support.")
            is Failure.NetworkConnectionLostSuddenly -> throw SSLException("The internet connection is suddenly lost.")
            is Failure.SSLError -> throw SSLHandshakeException("Verify the SSL.")
            is Failure.TimeOut -> throw SocketTimeoutException("Time out exception. The server took too long to answer.")
            is Failure.UnauthorizedOrForbidden -> throw Exception("Force a log out. server throw 401 - 403")
            is Failure.ServerBodyError -> throw Exception("Service Error Response (Error Body) -> CODE: ${error.code} - MESSAGE: ${error.message}")
            is Failure.DataToDomainMapperFailure -> throw IllegalArgumentException("DataToDomainMapperFailure: ${error.mapperException}")
            is Failure.ServiceUncaughtFailure -> throw Exception("500 - ServiceUncaughtFailure: ${error.uncaughtFailureMessage}")
        }
    }
}
