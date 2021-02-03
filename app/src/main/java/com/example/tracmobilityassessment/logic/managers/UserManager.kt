package com.example.tracmobilityassessment.logic.managers

import android.content.Context
import com.example.tracmobilityassessment.R
import com.example.tracmobilityassessment.data.Result
import com.example.tracmobilityassessment.data.model.User
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.GsonBuilder
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class UserManager {

    companion object {

        fun getCurrentUser(context: Context): User? {
            val userJson: String? =
                SharedPreferencesManager(context).getString(context.getString(R.string.shared_preferences_current_user))
            return GsonBuilder().create().fromJson(userJson, User::class.java)
        }

        fun login(
            fullName: String,
            email: String,
            context: Context,
            imageUrl: String?
        ): Result<User> {
            return try {
                val user = User(
                    fullName = fullName,
                    email = email,
                    imagePhoto = imageUrl
                )
                GsonBuilder().create()
                val json: String = GsonBuilder().create().toJson(user)
                SharedPreferencesManager(context).saveString(
                    context.getString(R.string.shared_preferences_current_user),
                    json
                )
                Result.Success(user)
            } catch (e: Throwable) {
                Result.Error(IOException("Error logging in", e))
            }
        }

        fun logout(context: Context) {
            SharedPreferencesManager(context).saveString(
                context.getString(R.string.shared_preferences_current_user),
                null
            )
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

            if (((user?.providerData?.get(1)?.providerId) == context.getString(R.string.facebook_provider_id))) {
                LoginManager.getInstance().logOut()
                FirebaseAuth.getInstance().signOut()
            } else if (((user?.providerData?.get(1)?.providerId) == context.getString(R.string.google_provider_id))) FirebaseAuth.getInstance()
                .signOut()

        }
    }
}