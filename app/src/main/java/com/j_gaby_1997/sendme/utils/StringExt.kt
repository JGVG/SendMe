package es.iessaladillo.pedrojoya.profile.utils

import android.util.Patterns

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
