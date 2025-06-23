package com.example.currencyconverter.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.model.CurrencyInfo

object CurrencyUtils {

    fun getCurrencyFlagUrl(currencyCode: String): String {
        return "https://flagcdn.com/${currencyCode.lowercase().take(2)}.svg"
    }

    fun getCurrencyInfo(context: Context, currencyCode: String): CurrencyInfo {
        return when (currencyCode) {
            "EUR" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_eur),
                symbol = context.getString(R.string.symbol_eur)
            )
            "AUD" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_aud),
                symbol = context.getString(R.string.symbol_aud)
            )
            "BGN" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_bgn),
                symbol = context.getString(R.string.symbol_bgn)
            )
            "BRL" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_brl),
                symbol = context.getString(R.string.symbol_brl)
            )
            "CAD" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_cad),
                symbol = context.getString(R.string.symbol_cad)
            )
            "CHF" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_chf),
                symbol = context.getString(R.string.symbol_chf)
            )
            "CNY" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_cny),
                symbol = context.getString(R.string.symbol_cny)
            )
            "CZK" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_czk),
                symbol = context.getString(R.string.symbol_czk)
            )
            "DKK" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_dkk),
                symbol = context.getString(R.string.symbol_dkk)
            )
            "GBP" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_gbp),
                symbol = context.getString(R.string.symbol_gbp)
            )
            "HKD" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_hkd),
                symbol = context.getString(R.string.symbol_hkd)
            )
            "HRK" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_hrk),
                symbol = context.getString(R.string.symbol_hrk)
            )
            "HUF" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_huf),
                symbol = context.getString(R.string.symbol_huf)
            )
            "IDR" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_idr),
                symbol = context.getString(R.string.symbol_idr)
            )
            "ILS" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_ils),
                symbol = context.getString(R.string.symbol_ils)
            )
            "INR" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_inr),
                symbol = context.getString(R.string.symbol_inr)
            )
            "ISK" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_isk),
                symbol = context.getString(R.string.symbol_isk)
            )
            "JPY" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_jpy),
                symbol = context.getString(R.string.symbol_jpy)
            )
            "KRW" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_krw),
                symbol = context.getString(R.string.symbol_krw)
            )
            "MXN" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_mxn),
                symbol = context.getString(R.string.symbol_mxn)
            )
            "MYR" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_myr),
                symbol = context.getString(R.string.symbol_myr)
            )
            "NOK" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_nok),
                symbol = context.getString(R.string.symbol_nok)
            )
            "NZD" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_nzd),
                symbol = context.getString(R.string.symbol_nzd)
            )
            "PHP" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_php),
                symbol = context.getString(R.string.symbol_php)
            )
            "PLN" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_pln),
                symbol = context.getString(R.string.symbol_pln)
            )
            "RON" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_ron),
                symbol = context.getString(R.string.symbol_ron)
            )
            "RUB" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_rub),
                symbol = context.getString(R.string.symbol_rub)
            )
            "SEK" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_sek),
                symbol = context.getString(R.string.symbol_sek)
            )
            "SGD" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_sgd),
                symbol = context.getString(R.string.symbol_sgd)
            )
            "THB" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_thb),
                symbol = context.getString(R.string.symbol_thb)
            )
            "TRY" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_try),
                symbol = context.getString(R.string.symbol_try)
            )
            "USD" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_usd),
                symbol = context.getString(R.string.symbol_usd)
            )
            "ZAR" -> CurrencyInfo(
                code = currencyCode,
                name = context.getString(R.string.currency_zar),
                symbol = context.getString(R.string.symbol_zar)
            )
            else -> CurrencyInfo(
                code = currencyCode,
                name = currencyCode,
                symbol = currencyCode
            )
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatAmount(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            amount.toInt().toString()
        } else {
            String.format("%.2f", amount)
        }
    }
}