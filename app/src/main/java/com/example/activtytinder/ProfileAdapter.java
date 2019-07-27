package com.example.activtytinder;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Models.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private Context context;
    private List<Event> mEvents;


    public ProfileAdapter(Context context, List<Event> events) {
        this.context = context;
        this.mEvents = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View eventView = inflater.inflate(R.layout.item_event, parent, false);
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.tvName.setText(event.getKeyName());
        holder.tvDate.setText(event.getKeyDate());
        holder.tvLocation.setText(event.getKeyAddress());
        ParseRelation<ParseUser> relation = event.getRelation("usersAttending");
        ParseQuery<ParseUser> query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                holder.tvPeopleAttending.setText("There are "  + users.size() +" people attending.");
            }
        });


        ParseFile image = event.getEventImage();
        if (image != null) {
            Uri imageUri = Uri.fromFile(new File(image.getUrl()));

            // TODO -- make nonsecure links secure without cutting strings

            /**
             * Alters image url from Parse to begin with https instead of http to pass
             * Android security requirements.
             */
            String security = "https";
            String url = image.getUrl().substring(4);

            Log.d("DEBUG", "in setting image " + security + url);
            Glide.with(context)
                    .load(security + url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .dontAnimate()
                    .into(holder.ivEventImage);


        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivEventImage;
        public TextView tvName;
        public TextView tvDate;
        public TextView tvLocation;
        public TextView tvPeopleAttending;

        public ViewHolder(View itemview) {
            super(itemview);

            ivEventImage = itemview.findViewById(R.id.ivEventImage);
            tvName = itemview.findViewById(R.id.tvName);
            tvDate = itemview.findViewById(R.id.tvDate);
            tvLocation = itemview.findViewById(R.id.tvLocation);
            tvPeopleAttending= itemview.findViewById(R.id.tvPeopleAttending);
        }

    }
    public void clear(){
        mEvents.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Event> list){
        mEvents.addAll(list);
        notifyDataSetChanged();
    }
}