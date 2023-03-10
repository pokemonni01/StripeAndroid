package com.wachi.stripeandroid

import android.app.Application
import com.stripe.android.PaymentConfiguration

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            ""
        )
    }
}