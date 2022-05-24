package com.j_gaby_1997.sendme.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.data.repository.Repository

class SearchViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var _searchResult: MutableLiveData<MutableList<User>> = MutableLiveData<MutableList<User>>()
    val searchResult: LiveData<MutableList<User>> get() = _searchResult

    fun search(input: String, currentUserEmail: String){
        if(input == ""){
            _searchResult.value = mutableListOf()
        }else{
            db.collection("USUARIOS")
                .orderBy("nombre").startAt(input).endAt(input+"\uf8ff")
                .get()
                .addOnSuccessListener { results ->
                    val searchList: MutableList<User> = mutableListOf()

                    for (result in results){
                        if(result.data["email"] != currentUserEmail){
                            searchList.add(
                                User(
                                    result.data["email"].toString(),
                                    result.data["nombre"].toString(),
                                    result.data["avatar_url"].toString(),
                                    result.data["biografía"].toString(),
                                    result.data["direccion"].toString(),
                                    result.data["fecha_alta"].toString(),
                                    result.data["sitio_web"].toString()
                                )
                            )
                        }
                    }
                    _searchResult.value = searchList
                }
        }

    }

}