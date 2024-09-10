package se.mikaiklintman.pt123

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PercViewmodel : ViewModel() {


    val database = Firebase.database

    private val _statsList = MutableStateFlow<List<FirebaseClass>?>(null)

    val statsList: StateFlow<List<FirebaseClass>?> get() = _statsList

    fun addStuff(addShotsTook: String, addShotsMade: String, position: String, addPercentage: Float) {

        if (addShotsTook.toIntOrNull() == null) {
            return
        }
        if (addShotsMade.toIntOrNull() == null) {
            return
        }


        val newPerc =
            FirebaseClass(addShotsMade.toInt(), addShotsTook.toInt(), addPercentage, position, "FBid123")

        database.getReference("Percentage").child(Firebase.auth.currentUser!!.uid).push()
            .setValue(newPerc)
            .addOnSuccessListener {
                loadPerc()
            }
    }

    fun deleteitem(delitem : FirebaseClass) {
        database.getReference("Percentage")
            .child(Firebase.auth.currentUser!!.uid)
            .child(delitem.fbid!!)
            .removeValue().addOnSuccessListener {
                loadPerc()
            }
    }

    fun loadPerc() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Log.e("PercViewmodel", "User is null!")
            return
        }

        database.getReference("Percentage").child(currentUser.uid).get()
            .addOnSuccessListener {
                val tempPercList = mutableListOf<FirebaseClass>()
                for (childsnap in it.children) {
                    val tempPerc = childsnap.getValue<FirebaseClass>()
                    if (tempPerc != null) {
                        tempPerc.fbid = childsnap.key
                        Log.i("Logss", tempPerc.fbid ?: "No fbid")
                        tempPercList.add(tempPerc)
                    } else {
                        Log.e("PercViewmodel", "tempPerc is null for key: ${childsnap.key}")
                    }
                }
                _statsList.value = tempPercList
            }
            .addOnFailureListener { exception ->
                Log.e("PercViewmodel", "Failed to load data: ${exception.message}")
            }
    }

}