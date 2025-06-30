# Currency Converter

A simple currency conversion application with real-time support for exchange rates and a transaction system.

## Functionality

### 1. Currencies screen
- **List mode**: Displays cross-currency rates with auto-update every second
- **Amount input mode**: Calculation of required amounts for exchange based on account balance
- Selection of the base currency for calculating exchange rates
- Display of country flags, names and currency balances

<div align="center">
<img src="https://github.com/user-attachments/assets/3cde17ea-8f9b-4b7f-9a7e-eedf1ae64d7f" width="350" alt="currency_screen_canada">
<img src="https://github.com/user-attachments/assets/0e0da885-0b34-4789-9606-530068a7de55" width="348" alt="currency_screen_canada">
</div>

### 2. The Exchange screen
- Confirmation of the currency exchange operation
- Display of the exchange rate and totals
- Saving the transaction to the database

### 3. Transactions Screen
- History of all operations performed
- Display of currency pairs, amounts and transaction times

<div align="center">
<img src="https://github.com/user-attachments/assets/d6e21db7-f650-4789-97cf-e53e3ef2c0ad" width="350" alt="currency_screen_canada">
<img src="https://github.com/user-attachments/assets/15c99678-be9e-4454-8033-6bb8f3bf681a" width="347" alt="currency_screen_canada">
</div>

## Technology

- **UI**: Jetpack Compose
- **Architecture**: Clean Architecture + MVVM
- **DI**: Hilt
- **Database**: Room
- **Network**: Retrofit2
- **Images**: Coil
- **Navigation**: Navigation Component

## Implementation features

- Automatic real-time currency exchange rate updates
- Filtering of currencies by available balance at purchase
- Initial balance: 75,000 rubles
- Validation of transactions with verification of the adequacy of funds

<div align="center">
  
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)

<!-- Архитектура -->
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-FF9800?style=for-the-badge&logo=android&logoColor=white)]()
[![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-4CAF50?style=for-the-badge&logo=android&logoColor=white)]()

<!-- База данных и сеть -->
[![Room](https://img.shields.io/badge/Room-Database-4CAF50?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/androidx/releases/room)
[![Retrofit](https://img.shields.io/badge/Retrofit-Network-FF5722?style=for-the-badge&logo=square&logoColor=white)](https://square.github.io/retrofit)
[![Coil](https://img.shields.io/badge/Coil-Images-9C27B0?style=for-the-badge&logo=android&logoColor=white)](https://coil-kt.github.io/coil)
</div>

