package vision.genesis.android.mvp.models.domain

import android.content.Context
import vision.genesis.android.mvp.models.data.AccountInfo


class AccountInteractor {
    companion object {
        val DEFAULT_KEY = "48329482sdfsdf..fsdfs123"
        private const val PREFERENCES = "userPreferences"
        private const val KEY_ADDRESS = "publicAddressKeyValue"

        val account = AccountInfo("Account 1", "http://arsich.ru/traders_api/avatars/7.png", DEFAULT_KEY, 0.01f, 1.18f)

        fun saveAddress(context: Context, address: String) {
            val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            preferences.edit().putString(KEY_ADDRESS, address).apply()
            account.key = address
        }

        fun loadAddress(context: Context) {
            val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val address = preferences.getString(KEY_ADDRESS, DEFAULT_KEY)
            account.key = address
        }

        fun isAddressDefault() = account.key == DEFAULT_KEY

        fun saveTokens(tokens: Float) {
            account.availableTokens = tokens
        }
    }
}