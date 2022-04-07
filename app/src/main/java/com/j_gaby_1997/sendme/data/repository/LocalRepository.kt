package com.j_gaby_1997.sendme.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.entity.CurrentUserDao
import java.time.LocalDateTime

class LocalRepository(private val currentUserDao: CurrentUserDao) : Repository {

    @RequiresApi(Build.VERSION_CODES.O)
    private val contacts: MutableLiveData<List<Contact>> = MutableLiveData(listOf(
        Contact("https://fotografias.lasexta.com/clipping/cmsimages02/2020/06/05/FD2DCDA1-1F2F-4C4F-9D4C-98F41AF737AC/98.jpg?crop=1300,731,x0,y19&width=1900&height=1069&optimize=high&format=webply",
            "Lorena crush",
            "Te quiero solo como amigo :(",
            LocalDateTime.now(),
            false,
            true,
        ),
        Contact("https://f4.bcbits.com/img/a3053931077_10.jpg",
            "Uvuvwevwevwe Osas",
            "Nunca te acerques a un algodón tio",
            LocalDateTime.now(),
            true,
            true,
        ),
        Contact("https://st2.depositphotos.com/1011382/7489/i/950/depositphotos_74896235-stock-photo-backpacker-man-taking-selfie-on.jpg",
            "El metralleta",
            "Donde pillaste la última vez???",
            LocalDateTime.now(),
            true,
            true,
        ),
        Contact("https://www.pronto.es/uploads/s1/84/55/36/madalyn-selfie.jpeg",
            "Sonia Ex",
            "no me vuelvas a hablar..",
            LocalDateTime.now(),
            false,
            false,
        ),
        Contact("https://i1.sndcdn.com/artworks-000039847852-af58zq-t500x500.jpg",
            "SuhMorenito19",
            "illo xq no contestasss xdxd",
            LocalDateTime.now(),
            false,
            false,
        ),
        Contact("https://www.tiktok.com/api/img/?itemId=7069514110974610694&location=0&aid=1988",
            "Manolo",
            "Me la suda soy Manolo",
            LocalDateTime.now(),
            false,
            false,
        ),
        Contact("https://www.lavanguardia.com/files/image_449_220/uploads/2022/03/20/62370c9317199.png",
            "ELXokas",
            "PERMABAN TONTO",
            LocalDateTime.now(),
            true,
            false,
        ),
    ))

    @RequiresApi(Build.VERSION_CODES.O)
    override fun queryAllContacts(): LiveData<List<Contact>> {
        return contacts
    }

    override fun queryCurrentUser(): LiveData<CurrentUser> = currentUserDao.queryCurrentUser()

    override fun addCurrentUser(currentUser: CurrentUser) = currentUserDao.insertCurrentUser(currentUser)

    override fun deleteCurrentUser() = currentUserDao.deleteCurrentUser()

}