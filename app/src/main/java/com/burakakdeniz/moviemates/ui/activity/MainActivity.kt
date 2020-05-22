package com.burakakdeniz.moviemates.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burakakdeniz.moviemates.R
import com.burakakdeniz.moviemates.ui.adapter.MovieAdapter
import com.burakakdeniz.moviemates.ui.model.Movie
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val movies= mutableListOf<Movie>()
    lateinit var recyclerView:RecyclerView
    private lateinit var usersReference: DatabaseReference
    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        getMovies()

        fabAdd.setOnClickListener {
            val intent=Intent(applicationContext,AddActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId){
        R.id.logout ->{
            val builder= android.app.AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.alert_logout_title))
            builder.setMessage(getString(R.string.alert_logout_content))
            builder.setPositiveButton(getString(R.string.alert_yes)){dialog, which ->
                auth.signOut()
                googleSignInClient.signOut().addOnCompleteListener{
                    val intent=Intent(applicationContext,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            builder.setNegativeButton(getString(R.string.alert_cancel)){ dialog, which ->  }
            val dialog: android.app.AlertDialog =builder.create()
            dialog.show()
            true
        }
        else->super.onOptionsItemSelected(item)

    }


    private fun init(){
        recyclerView=findViewById(R.id.rvMovies)
        usersReference= FirebaseDatabase.getInstance().getReference("users")
        auth=FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient= GoogleSignIn.getClient(this,gso)

    }


    private fun getMovies(){
        progressBar.visibility=View.VISIBLE
        usersReference.child(auth.uid!!).child("movies").addListenerForSingleValueEvent(object :
            ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    val children=dataSnapshot.children
                    children.forEach{
                        movies.add(Movie(
                            it.child("id").value.toString(),
                            it.child("name").value.toString(),
                            it.child("year").value.toString(),
                            it.child("genre").value.toString()
                        ))
                    }
                    recyclerView.layoutManager=LinearLayoutManager(applicationContext)
                    recyclerView.adapter=MovieAdapter(movies)
                }
                progressBar.visibility=View.INVISIBLE

            }

        })
    }
}
