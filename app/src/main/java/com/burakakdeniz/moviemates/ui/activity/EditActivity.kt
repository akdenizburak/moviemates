package com.burakakdeniz.moviemates.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.burakakdeniz.moviemates.R
import com.burakakdeniz.moviemates.ui.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit.buttonSave
import kotlinx.android.synthetic.main.activity_edit.editTextGenre
import kotlinx.android.synthetic.main.activity_edit.editTextName
import kotlinx.android.synthetic.main.activity_edit.editTextYear

class EditActivity : AppCompatActivity() {

    private lateinit var id:String
    private lateinit var name:String
    private lateinit var year:String
    private lateinit var genre:String
    private lateinit var usersReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        init()

        buttonSave.setOnClickListener {
            if (checkValidation()){
                usersReference.child(auth.uid!!).child("movies").child(id).setValue(Movie(id, name, year, genre)).addOnSuccessListener {
                    Toast.makeText(applicationContext,getString(R.string.toast_edit_success),Toast.LENGTH_SHORT).show()
                    val intent=Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    closeKeyboard()
                }

                }
            else{
                Toast.makeText(applicationContext,getString(R.string.toast_validation), Toast.LENGTH_SHORT).show()
                closeKeyboard()
            }
        }
    }

    private fun init(){
        id=intent.extras.getString("id")
        editTextName.setText(intent.extras.getString("name"))
        editTextYear.setText(intent.extras.getString("year"))
        editTextGenre.setText(intent.extras.getString("genre"))


        usersReference= FirebaseDatabase.getInstance().getReference("users")
        auth=FirebaseAuth.getInstance()
    }

    private fun checkValidation():Boolean{
        name=editTextName.text.toString()
        year=editTextYear.text.toString()
        genre=editTextGenre.text.toString()

        return !(name=="" || year=="" || genre =="")

    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
