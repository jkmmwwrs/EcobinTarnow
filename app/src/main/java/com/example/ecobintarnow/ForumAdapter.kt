package com.example.ecobintarnow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecobintarnow.databinding.ForumPostsBinding

class ForumAdapter(private val posts: ArrayList<ForumPosts>) : RecyclerView.Adapter<ForumAdapter.ForumViewHolder>()
{
    inner class ForumViewHolder(binding: ForumPostsBinding) : ViewHolder(binding.root)
    {
        val postAuthor = binding.postAuthor
        val postContent = binding.postContent
        val postTopic = binding.postTopic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val forumPostsBinding = ForumPostsBinding.inflate(inflater,parent,false)
        return ForumViewHolder(forumPostsBinding)
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        holder.postAuthor.text =  posts[position].postAuthor
        holder.postContent.text =  posts[position].postContent
        holder.postTopic.text =  posts[position].postTopic
    }

    override fun getItemCount(): Int {
        return posts.size
    }

}