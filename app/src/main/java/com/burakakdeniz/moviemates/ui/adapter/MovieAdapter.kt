package com.burakakdeniz.moviemates.ui.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.burakakdeniz.moviemates.R
import com.burakakdeniz.moviemates.ui.activity.EditActivity
import com.burakakdeniz.moviemates.ui.activity.ImageActivity
import com.burakakdeniz.moviemates.ui.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_image.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class MovieAdapter (private val movies:MutableList<Movie>):RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){


    private lateinit var usersReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie=movies[position]
        holder.itemView.textViewName.text=movie.name
        holder.itemView.textViewYear.text=movie.year
        holder.itemView.textViewGenre.text=movie.genre



        holder.itemView.iEdit.setOnClickListener {
            val intent=Intent(holder.itemView.context,EditActivity::class.java)
            intent.putExtra("id",movie.id)
            intent.putExtra("name",movie.name)
            intent.putExtra("year",movie.year)
            intent.putExtra("genre",movie.genre)
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.iDelete.setOnClickListener {
            val builder= AlertDialog.Builder(holder.itemView.context)
            builder.setTitle(holder.itemView.context.getString(R.string.alert_delete_title))
            builder.setMessage(holder.itemView.context.getString(R.string.alert_delete_content))
            builder.setPositiveButton(holder.itemView.context.getString(R.string.alert_yes)){dialog, which ->
                auth=FirebaseAuth.getInstance()
                usersReference= FirebaseDatabase.getInstance().getReference("users")
                usersReference.child(auth.uid!!).child("movies").child(movie.id).removeValue()
                removeItem(position)

                Toast.makeText(holder.itemView.context,holder.itemView.context.getString(R.string.toast_delete_success),Toast.LENGTH_SHORT).show()
                
            }
            builder.setNegativeButton(holder.itemView.context.getString(R.string.alert_cancel)){ dialog, which ->  }
            val dialog: AlertDialog=builder.create()
            dialog.show()
        }
        holder.itemView.cardView.setOnClickListener {
            val intent=Intent(holder.itemView.context,ImageActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

    private fun removeItem(position: Int){
        movies.removeAt(position)
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)
}