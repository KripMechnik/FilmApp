package com.example.filmapp.ui.firebase

import com.example.filmapp.ui.common.UserState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleFirebase {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun checkAuth(): Boolean{
        return auth.currentUser?.let {
            true
        } ?: run {
            false
        }
    }

    suspend fun register(user: UserData): Boolean {
            return try {
                auth.createUserWithEmailAndPassword(user.email, user.password).await()
                val uid = auth.currentUser?.uid
                val userHashMap = hashMapOf(
                    "uid" to uid,
                    "username" to user.username,
                    "photoUrl" to user.photoUrl
                )
                uid?.let {
                    db.collection("users").document(it).set(userHashMap).await()
                }

                true
            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
                false
            }
    }

    suspend fun signIn(email: String, password: String): Result<Boolean>{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(true)
        } catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
        }
    }

    suspend fun sendFriendInvite(uidSecond: String){
        try{
            val checkIfExistsFirst = db.collection("friends")
                .whereEqualTo("uidFirst", auth.currentUser?.uid)
                .whereEqualTo("uidSecond", uidSecond)
                .get()
                .await()
            val checkIfExistsSecond = db.collection("friends")
                .whereEqualTo("uidSecond", auth.currentUser?.uid)
                .whereEqualTo("uidFirst", uidSecond)
                .get()
                .await()
            if (checkIfExistsFirst.isEmpty && checkIfExistsSecond.isEmpty){
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

    suspend fun getAllUsers() : List<UserState>? {
        return try {
            val res = mutableListOf<Map<String, Any>>()
            val users = db.collection("users")
                .whereNotEqualTo("uid", auth.currentUser!!.uid)
                .get()
                .await()
            for (user in users){
                res.add(user.data)
            }
            val usersResult = res.map { user ->
                UserState(
                    username = user["username"] as String,
                    photoUrl = user["photoUrl"] as String,
                    uid = user["uid"] as String
                )
            }
            usersResult
        } catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
            null
        }
    }

    suspend fun getUser(uid: String) : Map<String, Any>? {
        return try {
            val user = db.collection("users")
                .document(uid)
                .get()
                .await()
            user.data ?: emptyMap()
        } catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
            null
        }

    }

    suspend fun getSelfUser() : Map<String, Any>? {
        return try {
            auth.currentUser?.let {
                val user = db.collection("users")
                    .document(it.uid)
                    .get()
                    .await()
                user.data ?: emptyMap()
            } ?: emptyMap()
        } catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
            null
        }

    }

    suspend fun getFriends(): List<UserState>? {
        return try {
            val res = mutableListOf<Map<String, Any>>()
            val queryFirst = db.collection("friends")
                .whereEqualTo("uidFirst", auth.currentUser?.uid)
                .whereEqualTo("accepted", 1)
                .get()
                .await()

            queryFirst.forEach { document ->
                res.add(getUser(document.data["uidSecond"] as String)!!)
            }

            val querySecond = db.collection("friends")
                .whereEqualTo("uidSecond", auth.currentUser?.uid)
                .whereEqualTo("accepted", 1)
                .get()
                .await()

            querySecond.forEach { document ->
                res.add(getUser(document.data["uidFirst"] as String)!!)
            }
            val users = res.map { user ->
                UserState(
                    username = user["username"] as String,
                    photoUrl = user["photoUrl"] as String,
                    uid = user["uid"] as String
                )
            }
            users
        } catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
            null
        }
    }

    suspend fun getFriendInvites(): List<UserState>? {
        return try {
            val res = mutableListOf<Map<String, Any>>()
            val friendInvites =  db.collection("friends")
                .whereEqualTo("uidSecond", auth.currentUser?.uid)
                .whereEqualTo("accepted", 0)
                .get()
                .await()
            for (friend in friendInvites){
                res.add(getUser(friend.data["uidFirst"] as String)!!)
            }
            val users = res.map { user ->
                UserState(
                    username = user["username"] as String,
                    photoUrl = user["photoUrl"] as String,
                    uid = user["uid"] as String
                )
            }
            users
        } catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
            null
        }
    }

    suspend fun acceptInvite(uid: String){

        val friendInvite = db.collection("friends")
            .whereEqualTo("uidFirst", uid)
            .whereEqualTo("uidSecond", auth.currentUser?.uid)
            .get()
            .await()
        for (document in friendInvite){
            db.collection("friends").document(document.id)
                .update("accepted", 1)
        }

    }

    suspend fun declineInvite(uid: String){
        val friendInvite = db.collection("friends")
            .whereEqualTo("uidFirst", uid)
            .whereEqualTo("uidSecond", auth.currentUser?.uid)
            .get()
            .await()
        for (document in friendInvite){
            db.collection("friends").document(document.id)
                .delete().await()
        }
    }

    suspend fun sendRecommendation( uidTo: String, id: String){
        val newRecommendation = mapOf(
            "uidFrom" to auth.currentUser?.uid,
            "uidTo" to uidTo,
            "id" to id
        )
        db.collection("recommendations")
            .add(newRecommendation)
            .await()
    }

    suspend fun getFilmsIdsAndUsers(): List<Pair<String, String>> {
        val documents = db.collection("recommendations").whereEqualTo("uidTo", auth.currentUser?.uid).get().await()
        val idsAndUsers = mutableListOf<Pair<String, String>>()
        documents.forEach {
            idsAndUsers.add(Pair(it.data["id"] as String, it.data["uidFrom"] as String))
        }
        return idsAndUsers
    }

}

data class UserData(
    val email: String,
    val password: String,
    val username: String,
    val photoUrl: String?
)


