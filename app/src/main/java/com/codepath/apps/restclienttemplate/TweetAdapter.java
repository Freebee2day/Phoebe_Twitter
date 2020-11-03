package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    ArrayList<Tweet> collection;
    Context calling_from;


    public TweetAdapter(ArrayList<Tweet> collection, Context calling_from) {
        this.collection = collection;
        this.calling_from = calling_from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tweetView= LayoutInflater.from(calling_from).inflate(R.layout.tweet_item,parent,false);
        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet single_tweet = collection.get(position);
        holder.bind(single_tweet);
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    public void clear(){
        collection.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Tweet> list){
        collection.addAll(list);
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvBody;
        TextView tvName;
        ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBody=itemView.findViewById(R.id.tvBody);
            tvName=itemView.findViewById(R.id.tvName);
            ivProfile=itemView.findViewById(R.id.ivProfile);
        }

        public void bind(Tweet single_tweet) {
            tvBody.setText(single_tweet.body);
            tvName.setText(single_tweet.user.screen_name);
            Glide.with(calling_from).load(single_tweet.user.profile_url).into(ivProfile);



        }
    }
}
