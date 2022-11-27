package com.example.ecobintarnow

import com.example.ecobintarnow.databinding.ActivityForumBinding
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth: FirebaseAuth
    val mDatabase = FirebaseDatabase.getInstance().getReference("Posts")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val post = dataSnapshot.getValue()
                val adapter = ForumAdapter(post as List<ForumPosts>)
                binding.forumRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                binding.forumRecyclerView.adapter = adapter

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        mDatabase.addValueEventListener(postListener)


        binding.forumButton.setOnClickListener {
            val postContent = binding.forumEditText.text.toString()
            val postAuthor = auth.currentUser?.email.toString()
            val postData = ForumPosts(postAuthor,postContent)
            database.child("Posts").setValue(postData)
        }
    }
    companion object {
        private const val TAG = "ForumActivity"
    }
}