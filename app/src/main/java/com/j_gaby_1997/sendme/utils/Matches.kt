package com.j_gaby_1997.sendme.utils

import android.util.Patterns

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
