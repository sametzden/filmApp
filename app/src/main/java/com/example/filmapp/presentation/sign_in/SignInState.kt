package com.example.filmapp.presentation.sign_in

import coil.compose.AsyncImagePainter

data class SignInState(
    val isSignInSuccessful : Boolean = false,
    val signInError : String? = null
)
