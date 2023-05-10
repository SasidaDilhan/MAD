package com.example.newtestdatabinding.activities.user

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import android.widget.Toast
import com.example.newtestdatabinding.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResumeDashboardActivity : AppCompatActivity() {
    private lateinit var viewMyResumeTile: CardView
    private lateinit var createResumeTile: CardView
    private lateinit var editResumeTile: CardView
    private lateinit var deleteResumeTile: CardView

    val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_resume_dashboard)
        firebaseAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("cv-generator")

        viewMyResumeTile = findViewById(R.id.card1)
        createResumeTile = findViewById(R.id.card2)
        editResumeTile = findViewById(R.id.card3)
        deleteResumeTile = findViewById(R.id.card4)

        viewMyResumeTile.setOnClickListener {

            val intent = Intent(this, MyCVActivity::class.java)
            startActivity(intent)
        }

        createResumeTile.setOnClickListener {

            val intent = Intent(this, ResumeFormActivity::class.java)
            startActivity(intent)
        }

        deleteResumeTile.setOnClickListener {
            val query = collectionRef.whereEqualTo("email", firebaseAuth.currentUser?.email)
            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // The document ID can be retrieved using document.id
                        documentId = document.id
                        // do something with the document ID
                    }
                    if(documentId != null) {
                        db.collection("cv-generator")
                            .document(documentId!!)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "CV deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    applicationContext,
                                    "Error while deleting successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "No to CV delete",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
//
        }

        editResumeTile.setOnClickListener {

//            val intent = Intent(this, AppliedJobsActivity::class.java)
//            startActivity(intent)
        }
    }

    fun CreateResume(view: View) {}
    fun ViewMyResume(view: View) {}
    fun EditResume(view: View) {}
    fun DeleteResume(view: View) {}
}