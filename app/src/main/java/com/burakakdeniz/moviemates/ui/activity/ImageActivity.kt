package com.burakakdeniz.moviemates.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley
import com.burakakdeniz.moviemates.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.list_item.*
import org.json.JSONException


class ImageActivity : AppCompatActivity() {
    private var requestQueue: RequestQueue? = null
    private var imageAdr:url
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        requestQueue = Volley.newRequestQueue(this)

        jsonParse()

        Picasso.with(applicationContext).load(imageAdr).into(posterId)
        movieDetail.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
            closeKeyboard()

        }

    }
    private fun jsonParse(){
        val url="https://www.omdbapi.com/?t=it&apikey=6efbf078"
        val request = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                response ->try {
            val yearJ = response.getString("Year").toString()
            val runtimeJ = response.get("Runtime").toString()
            val genreJ = response.get("Genre").toString()
            val directorJ = response.get("Director").toString()
            val actorsJ = response.get("Actors").toString()
            val countryJ = response.get("Country").toString()
            val ratingJ = response.get("imdbRating").toString()
            val votesJ = response.get("imdbVotes").toString()
            val boXofficeJ = response.get("BoxOffice").toString()
            val poster=response.get("Poster").toString()

            imageAdr=poster
            rating.text=ratingJ
            yearRelease.text=yearJ
            country.text=countryJ
            runtime.text=runtimeJ
            director.text=directorJ
            boxoffice.text=boXofficeJ
            actors.text=actorsJ
            genreM.text=genreJ
            votes.text=votesJ


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, Response.ErrorListener { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }


    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}