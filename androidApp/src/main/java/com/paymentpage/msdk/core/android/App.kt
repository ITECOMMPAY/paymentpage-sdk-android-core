package com.paymentpage.msdk.core.android

import android.app.Application
import com.paymentpage.msdk.core.MSDKCoreSession
import com.paymentpage.msdk.core.MSDKCoreSessionConfig


class App : Application() {
    private val config = MSDKCoreSessionConfig.release("sdk.ecommpay.com", "paymentpage.ecommpay.com")
    //for mocking requests
    //private val config = MSDKCoreSessionConfig.mockFullSuccessFlow()

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