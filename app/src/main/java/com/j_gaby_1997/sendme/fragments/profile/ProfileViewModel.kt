package com.j_gaby_1997.sendme.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.data.repository.Repository
import com.j_gaby_1997.sendme.services.*
import kotlin.concurrent.thread

class ProfileViewModel(private val repository: Repository, savedStateHandle: SavedStateHandle) : ViewModel() {

    var currentUser: LiveData<CurrentUser> = repository.queryCurrentUser()
    private var _user: MutableLiveData<User> = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun setUser(emailInput: String){
        FirebaseFirestore.getInstance().collection("USUARIOS").document(emailInput).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                _user.value = User(
                    emailInput,
                    snapshot.data!!["nombre"].toString(),
                    snapshot.data!!["avatar_url"].toString(),
                    snapshot.data!!["biografía"].toString(),
                    snapshot.data!!["direccion"].toString(),
                    snapshot.data!!["fecha_alta"].toString(),
                    snapshot.data!!["sitio_web"].toString()
                )
            }
        }
    }

    fun signOutUser() {
        signOut()
        delete()
    }

    private fun delete() {
        thread {
            repository.deleteCurrentUser()
        }
    } // Delete current user data.

}
