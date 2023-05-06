package com.example.newtestdatabinding.activities.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.newtestdatabinding.R
import com.example.newtestdatabinding.activities.user.UpdateUserProfile
import com.example.newtestdatabinding.activities.user.UserLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CompanyProfileDisplay : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var disp_cname: TextView
    private lateinit var disp_cemail: TextView
    private lateinit var disp_cphone: TextView
    private lateinit var disp_caddress: TextView
    private lateinit var disp_cregis : TextView
    private lateinit var deletecProfile : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile_display)

        val CprofEdit = findViewById<Button>(R.id.edt_Cprofile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        disp_cname = findViewById(R.id.cmp_name)
        disp_cemail = findViewById(R.id.cmp_email)
        disp_cphone = findViewById(R.id.cmp_phone)
        disp_caddress = findViewById(R.id.cmp_address)
        disp_cregis = findViewById(R.id.cmp_regiseterNum)
        deletecProfile = findViewById(R.id.dlt_Cprofile)

        val usrId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("users").document(usrId)
        ref.get().addOnSuccessListener {
            if(it != null){
                val cname = it.get("companyName").toString()
                val cemail = it.get("email").toString()
                val cphoneNumber = it.get("phoneNumber").toString()
                val caddress = it.get("address").toString()
                val cregistNum = it.get("registrationNumber").toString()

                disp_cname.text = cname
                disp_cemail.text = cemail
                disp_cphone.text = cphoneNumber
                disp_caddress.text = caddress
                disp_cregis.text = cregistNum

            }
        }
            .addOnFailureListener{
                Toast.makeText(this, "Faild!!", Toast.LENGTH_SHORT).show()
            }

        CprofEdit.setOnClickListener{
            val intent = Intent(this, UpdateCompanyProfile::class.java)
            startActivity(intent)
        }
        deletecProfile.setOnClickListener{

            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)
            val usrId = FirebaseAuth.getInstance().currentUser!!.uid
            db.collection("users").document(usrId).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "successfully deleted!!",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failiure in delete",Toast.LENGTH_SHORT).show()
                }
        }

    }
}