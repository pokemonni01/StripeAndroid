package com.wachi.stripeandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.stripe.android.model.*
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.wachi.stripeandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var paymentLauncher: PaymentLauncher
    lateinit var paymentSheet: PaymentSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        paymentLauncher =
            PaymentLauncher.create(
                this,
                // set publish key here
                ""
            ) { paymentResult ->
                Toast.makeText(this, paymentResult.toString(), Toast.LENGTH_LONG).show()
                if (paymentResult is PaymentResult.Failed)  {
                    paymentResult.throwable.printStackTrace()
                }
            }


        // Confirm the PaymentIntent with the card widget
        binding.payButton.setOnClickListener {
//            binding.cardFormWidget.cardParams?.let { params ->
//                doPaymentIntent(params)
                doPaymentSheet()
//            }
        }
    }

    private fun doSetupIntent(cardParams: CardParams) {
        val confirmParams = ConfirmSetupIntentParams.create(
            paymentMethodCreateParams = PaymentMethodCreateParams.createCard(cardParams),
            clientSecret = binding.etClientSecret.text.toString(),
        )
        paymentLauncher.confirm(confirmParams)
    }

    private fun doPaymentIntent(cardParams: CardParams) {
        val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
            clientSecret = binding.etClientSecret.text.toString(),
            paymentMethodCreateParams = PaymentMethodCreateParams.createCard(cardParams),
        )
        paymentLauncher.confirm(confirmParams)
    }

    private fun doPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            binding.etClientSecret.text.toString(),
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }
    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                paymentSheetResult.error.printStackTrace()
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
            }
        }
    }
}