package com.example.filmapp.ui.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleFirebase(
    private val coroutineScope: CoroutineScope
) {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun register(user: UserData){
        coroutineScope.launch(Dispatchers.IO) {
            try {
                auth.createUserWithEmailAndPassword(user.email, user.password).await()
                val uid = auth.currentUser?.uid
                val userHashMap = hashMapOf(
                    "uid" to uid,
                    "username" to user.username,
                    "photoUrl" to user.photoUrl
                )
                db.collection("users").document(uid!!).set(userHashMap).await()
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }

        }



    }

    fun signIn(user: UserData){
        coroutineScope.launch(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(user.email, user.password).await()
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }

    }

    fun signOut() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                auth.signOut()
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
    }

    fun sendFriendInvite(uidSecond: String){

        coroutineScope.launch(Dispatchers.IO){
            try{
                val checkIfExists = db.collection("friends")
                    .whereEqualTo("uidFirst", auth.currentUser?.uid)
                    .whereEqualTo("uidSecond", uidSecond)
                    .get()
                    .await()
                if (checkIfExists.isEmpty){
                    val newFriendRecord = hashMapOf(
                        "uidFirst" to auth.currentUser?.uid,
                        "uidSecond" to uidSecond,
                        "accepted" to 0
                    )
                    db.collection("friends").add(newFriendRecord).await()
                } else {
                    println("Запись уже существует")
                }
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }


        }

    }

    fun getAllUsers() : List<Map<String, Any>>{

        val res = mutableListOf<Map<String, Any>>()
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val users = db.collection("users")
                    .get()
                    .await()
                for (user in users){
                    res.add(user.data)
                }
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
        return res
    }

    private fun getUser(uid: String) : Map<String, Any>{
        var data: Map<String, Any> = hashMapOf()

        coroutineScope.launch(Dispatchers.IO){
            try {
                val user = db.collection("users")
                    .document(uid)
                    .get()
                    .await()
                data = user.data!!
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
        return data
    }

    fun getFriends(): List<Map<String, Any>> {
        val res = mutableListOf<Map<String, Any>>()

        coroutineScope.launch(Dispatchers.IO){
            try {
                val queryFirst = db.collection("friends")
                    .whereEqualTo("uidFirst", auth.currentUser?.uid)
                    .get()
                    .await()

                val querySecond = db.collection("friends")
                    .whereEqualTo("uidSecond", auth.currentUser?.uid)
                    .get()
                    .await()
                val combinedResults = mutableListOf<DocumentSnapshot>()

                for (document in queryFirst) {
                    combinedResults.add(document)
                }

                for (document in querySecond) {
                    if (!combinedResults.contains(document)) {
                        combinedResults.add(document)
                    }
                }

                for (doc in combinedResults) {
                    res.add(getUser(doc.data!!["uid"].toString()))
                }
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }

        }
        return res
    }

    fun getFriendInvites(): MutableList<Map<String, Any>> {
        val res = mutableListOf<Map<String, Any>>()
        coroutineScope.launch(Dispatchers.IO){
            try {
                val friendInvites =  db.collection("friends")
                    .whereEqualTo("secondUid", auth.currentUser?.uid)
                    .whereEqualTo("accepted", 0)
                    .get()
                    .await()
                for (friend in friendInvites){
                    res.add(friend.data)
                }
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }
        }
        return res
    }

    fun acceptInvite(uid: String){

        coroutineScope.launch(Dispatchers.IO){
            val friendInvite = db.collection("friends")
                .whereEqualTo("firstUid", uid)
                .whereEqualTo("secondUid", auth.currentUser?.uid)
                .get()
                .await()
            for (document in friendInvite){
                db.collection("friends").document(document.id)
                    .update("accepted", 1)
            }
        }
    }

}

data class UserData(
    val email: String,
    val password: String,
    val username: String,
    val photoUrl: String?
)

