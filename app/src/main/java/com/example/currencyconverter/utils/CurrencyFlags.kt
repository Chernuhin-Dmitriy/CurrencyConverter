package com.example.currencyconverter.utils

object CurrencyFlags {

    fun getFlagEmoji(currencyCode: String): String {
        return when (currencyCode) {
            "EUR" -> "🇪🇺" // European Union
            "USD" -> "🇺🇸" // United States
            "GBP" -> "🇬🇧" // United Kingdom
            "JPY" -> "🇯🇵" // Japan
            "AUD" -> "🇦🇺" // Australia
            "CAD" -> "🇨🇦" // Canada
            "CHF" -> "🇨🇭" // Switzerland
            "CNY" -> "🇨🇳" // China
            "SEK" -> "🇸🇪" // Sweden
            "NZD" -> "🇳🇿" // New Zealand
            "MXN" -> "🇲🇽" // Mexico
            "SGD" -> "🇸🇬" // Singapore
            "HKD" -> "🇭🇰" // Hong Kong
            "NOK" -> "🇳🇴" // Norway
            "TRY" -> "🇹🇷" // Turkey
            "RUB" -> "🇷🇺" // Russia
            "INR" -> "🇮🇳" // India
            "BRL" -> "🇧🇷" // Brazil
            "ZAR" -> "🇿🇦" // South Africa
            "KRW" -> "🇰🇷" // South Korea
            "DKK" -> "🇩🇰" // Denmark
            "PLN" -> "🇵🇱" // Poland
            "CZK" -> "🇨🇿" // Czech Republic
            "HUF" -> "🇭🇺" // Hungary
            "BGN" -> "🇧🇬" // Bulgaria
            "RON" -> "🇷🇴" // Romania
            "HRK" -> "🇭🇷" // Croatia
            "ISK" -> "🇮🇸" // Iceland
            "ILS" -> "🇮🇱" // Israel
            "THB" -> "🇹🇭" // Thailand
            "MYR" -> "🇲🇾" // Malaysia
            "PHP" -> "🇵🇭" // Philippines
            "IDR" -> "🇮🇩" // Indonesia
            else -> "🏳️" // Default flag
        }
    }
}