package com.example.ecobintarnow

import com.example.ecobintarnow.databinding.ActivityForumBinding
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf

class ForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val mDatabase = FirebaseDatabase.getInstance().getReference("Posts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val adapter = ForumAdapter(getPosts())
        binding.forumRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.forumRecyclerView.adapter = adapter


        binding.forumButton.setOnClickListener {
            val currentTimestamp = System.currentTimeMillis()
            val postContent = binding.forumEditText.text.toString()
            val postAuthor = auth.currentUser?.email.toString()
            val postData = ForumPosts(postAuthor, postContent)
            mDatabase.child(currentTimestamp.toString()).setValue(postData)
        }
    }


    private fun getPosts(): List<ForumPosts> = buildList {
//        mDatabase.get().addOnSuccessListener { it ->
//            it.children.forEach {
//               val newPost = ForumPosts(it.child("postAuthor").value.toString(),it.child("postContent").value.toString())
//                add(newPost)
//                Log.i("firebase", "${it.child("postAuthor").value}")
//
//            }
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }

        for (i in 0..5) {
            val newPost = ForumPosts("$i", "$i")
            add(newPost)
        }
    }
}

