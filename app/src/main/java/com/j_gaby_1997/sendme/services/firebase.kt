package com.j_gaby_1997.sendme.services

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.j_gaby_1997.sendme.data.entity.Message
import com.j_gaby_1997.sendme.data.entity.User
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun signUpUser(email: String, password: String): Task<AuthResult> = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

fun logInUser(email: String, password: String): Task<AuthResult> = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

fun signOut() = FirebaseAuth.getInstance().signOut()

fun deleteContact(selecteds: MutableList<String>, senderEmail: String){
    val senderRef = FirebaseFirestore.getInstance().collection("USUARIOS").document(senderEmail).collection("CONTACTS")

    for(receiverEmail in selecteds){
        senderRef.document(receiverEmail).collection("MENSAJES").get().addOnSuccessListener { doc ->
            doc.forEach{
                it.reference.delete()
            }
            senderRef.document(receiverEmail).delete()
        }
    }
} //Delete the contacts of a specific user.

@RequiresApi(Build.VERSION_CODES.O)
fun createUserDoc(email: String, name: String, isOnline: Boolean = false) {

   val uri = Uri.parse("android.resource://com.j_gaby_1997.sendme/drawable/default_avatar")

    val avatarsRef = Firebase.storage.reference.child("avatars/${email}/${uri.lastPathSegment}")
    val uploadTask = avatarsRef.putFile(uri)

    uploadTask.addOnSuccessListener {
        FirebaseFirestore.getInstance().collection("USUARIOS").document(email).set(
            hashMapOf(
                "email" to email,
                "nombre" to name,
                "avatar_url" to Uri.parse("android.resource://com.j_gaby_1997.sendme/drawable/default_avatar").toString(),
                "biograf??a" to "",
                "direccion" to "",
                "fecha_alta" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString(),
                "sitio_web" to "",
                "estado_online" to isOnline
            )
        )
    }
}

fun saveUpdateDataUser(email: String, uriImage: Uri?, name: String, description: String, location: String, web: String){
    if(uriImage != null){

        val avatarsRef = Firebase.storage.reference.child("avatars/${email}/${uriImage.lastPathSegment}")
        val uploadTask = avatarsRef.putFile(uriImage)

        uploadTask.addOnSuccessListener {
            FirebaseFirestore.getInstance().collection("USUARIOS").document(email).update(
                "nombre", name, "avatar_url", uriImage.toString(), "biograf??a", description, "direccion", location, "sitio_web", web
            )
        }

    }else{
        FirebaseFirestore.getInstance().collection("USUARIOS").document(email).update(
            "nombre", name, "biograf??a", description, "direccion", location, "sitio_web", web
        )
    }
}