package com.example.ecobintarnow

import com.example.ecobintarnow.databinding.ActivityForumBinding
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class ForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var postsRecyclerView : RecyclerView
    private lateinit var postListArray : ArrayList<ForumPosts>

    private val mDatabase = FirebaseDatabase.getInstance().getReference("Posts")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postsRecyclerView = findViewById(R.id.forumRecyclerView)
        postsRecyclerView.layoutManager = LinearLayoutManager(this)
        postsRecyclerView.setHasFixedSize(true)

        postListArray = arrayListOf<ForumPosts>()
        getPostData()

        //auth = Firebase.auth

//        val adapter = ForumAdapter(getPosts())
//        binding.forumRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
//        binding.forumRecyclerView.adapter = adapter

        auth = Firebase.auth

        binding.showbutton.setOnClickListener {
            binding.forumEditText.visibility = View.VISIBLE
            binding.forumButton.visibility = View.VISIBLE
            binding.showbutton.visibility = View.GONE
        }

        binding.forumButton.setOnClickListener {
            if(binding.forumEditText.text != null) {
                val postData = ForumPosts(
                    auth.currentUser?.email.toString(),
                    binding.forumEditText.text.toString()
                )
                mDatabase.child(System.currentTimeMillis().toString()).setValue(postData)
                binding.forumEditText.text = null
                binding.forumEditText.visibility = View.GONE
                binding.forumButton.visibility = View.GONE
                binding.showbutton.visibility = View.VISIBLE
            }
            else
            {
                Toast.makeText(
                    this, "Wprowadź wiadomość",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

      private fun getPostData() {
          database = FirebaseDatabase.getInstance().getReference("Posts")
          database.addValueEventListener(object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    for(postSnapshot in snapshot.children)
                    {
                        val post = postSnapshot.getValue(ForumPosts::class.java)
                        postListArray.add(post!!)
                    }
                    postsRecyclerView.adapter = ForumAdapter(postListArray)
                }
              }

              override fun onCancelled(error: DatabaseError) {

              }

          })

      }
//    private fun getPosts(): List<ForumPosts> = buildList {
//
//        for (i in 0..5)
//        {
//            val pAuthor = mDatabase.get().result.children.elementAt(i).child("postAuthor").value
//            val pContent = mDatabase.get().result.children.elementAt(i).child("postContent").value
//            var newPost = ForumPosts(pAuthor.toString(),pContent.toString())
//            add(newPost)
//        }

//        mDatabase.get().addOnSuccessListener { it ->
//            it.children.forEach {
//
//               val newPost = ForumPosts(it.child("postAuthor").value.toString(),it.child("postContent").value.toString())
//                add(newPost)
//                Log.i("firebase", "${it.key}")
//
//            }
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }

//        for (i in 0..5) {
//            val newPost = ForumPosts("$i", "$i")
//            add(newPost)
//        }
//    }
}

