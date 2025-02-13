package com.example.filmapp.presentation.sign_in

import android.app.PendingIntent.CanceledException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.filmapp.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait

class GoogleAuthUiClient(
    private  val context : Context,
    private  val oneTapClient: SignInClient
) {
    private  val auth = Firebase.auth


    suspend fun signIn() : IntentSender?{
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch (e : Exception){
            e.printStackTrace()
            if(e is CanceledException) throw e
            null

        }
        return  result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
            data = user?.run {
                displayName?.let {
                    UserData(
                        userId = uid,
                        username = it,
                        profilePictureUrl = photoUrl?.toString()
                    )
                }
            },
                errorMessage = null
        )

        }catch (e:Exception){
            e.printStackTrace()
            if(e is CanceledException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e : Exception)
        {
            e.printStackTrace()
            if(e is CanceledException) throw e

        }
    }
    fun getSignedInUser() : UserData? = auth.currentUser?.run {
        displayName?.let {
            UserData(
                userId = uid,
                username = it,
                profilePictureUrl = photoUrl?.toString()
            )
        }
    }



    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

}