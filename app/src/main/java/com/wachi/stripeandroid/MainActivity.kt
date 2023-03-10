package com.wachi.stripeandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.wachi.stripeandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val paymentLauncher =
            PaymentLauncher.create(
                this,
                // set publish key here
                ""
            ) { paymentResult ->
                Toast.makeText(this, paymentResult.toString(), Toast.LENGTH_LONG).show()
            }


        // Confirm the PaymentIntent with the card widget
        binding.payButton.setOnClickListener {
            binding.cardFormWidget.cardParams?.let { params ->
                val confirmParams = ConfirmSetupIntentParams.create(
                    paymentMethodCreateParams = PaymentMethodCreateParams.createCard(params),
                    clientSecret = binding.etClientSecret.text.toString(),
                )
                paymentLauncher.confirm(confirmParams)
            }
        }
    }
}