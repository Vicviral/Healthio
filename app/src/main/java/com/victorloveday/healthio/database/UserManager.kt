package com.victorloveday.healthio.database

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.victorloveday.healthio.utils.constants.Constant.REQUIRED_AGE_TO_USE_APP
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {
    private val dataStore = context.createDataStore(name = "user_prefs")

    companion object {
        val NAME_KEY = preferencesKey<String>("USER_NAME")
        val GENDER_KEY = preferencesKey<String>("USER_GENDER")
        val AGE_KEY = preferencesKey<Int>("USER_AGE")
        val WEIGHT_KEY = preferencesKey<Int>("USER_WEIGHT")
    }

    suspend fun storeWeight(weight: Int) {
        dataStore.edit {
            it[WEIGHT_KEY] = weight
        }
    }

    suspend fun storeUser(name: String, age: Int, gender: String) {
        dataStore.edit {
            it[NAME_KEY] = name
            it[GENDER_KEY] = gender
            it[AGE_KEY] = age
        }
    }

    val userNameFlow: Flow<String> = dataStore.data.map {
        it[NAME_KEY] ?: ""
    }
    val userGenderFlow: Flow<String> = dataStore.data.map {
        it[GENDER_KEY] ?: ""
    }
    val userAgeFlow: Flow<Int> = dataStore.data.map {
        val age = it[AGE_KEY] ?: 0

        if (age < REQUIRED_AGE_TO_USE_APP) {
            Toast.makeText(context, "Sorry.  You're underage. You can't use this app for now.", Toast.LENGTH_SHORT).show()
        }

        age
    }
    val userWeightFlow: Flow<Int> = dataStore.data.map {
        it[WEIGHT_KEY] ?: 0
    }
}