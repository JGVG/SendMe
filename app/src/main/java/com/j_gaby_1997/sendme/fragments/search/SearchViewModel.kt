package com.j_gaby_1997.sendme.fragments.search

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.data.repository.Repository

private const val STATE_RESULTS_INDEX = "STATE_RESULTS_INDEX"

class SearchViewModel (private val repository: Repository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private var _searchResult: MutableLiveData<List<User>> = MutableLiveData<List<User>>()
    var currentUser = repository.queryCurrentUser().value

    val searchResult: LiveData<List<User>> get() = _searchResult
    val db = FirebaseFirestore.getInstance()

    fun search(input: String) {
        db.collection("USUARIOS")
            .orderBy("nombre").startAt(input).endAt(input+'\uf8ff')
            .get()
            .addOnSuccessListener { results ->
                for (result in results) {
                    Log.i(TAG, "RESULTADO => ${result.data["email"]}")
                }
            }.addOnFailureListener { exception ->
                Log.i(TAG, "No found documents...: ", exception)
            }
    }
}