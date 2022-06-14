package com.ecommpay.msdk.test.android

import android.app.Application
import com.ecommpay.msdk.core.MSDKCoreSession
import com.ecommpay.msdk.core.MSDKCoreSessionConfig


class App : Application() {
    private val config = MSDKCoreSessionConfig.prodWithDebug()
    //for mocking requests
    //private val config = MSDKCoreSessionConfig.mock(MSDKCoreMockConfig.initWithSuccessPayment())

    companion object {
        private var msdkSession: MSDKCoreSession? = null

        @JvmStatic
        fun getMsdkSession(): MSDKCoreSession {
            return msdkSession as MSDKCoreSession
        }
    }

    override fun onCreate() {
        super.onCreate()
        //at the first we must create MsdCoreSession object
        msdkSession = MSDKCoreSession(config)
    }
}