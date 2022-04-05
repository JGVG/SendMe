package com.j_gaby_1997.sendme.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

// - AUTH METHODS FROM FIREBASE -
fun signUpUser(email: String, password: String): Task<AuthResult> = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
fun logInUser(email: String, password: String): Task<AuthResult> = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
fun signOut() = FirebaseAuth.getInstance().signOut()