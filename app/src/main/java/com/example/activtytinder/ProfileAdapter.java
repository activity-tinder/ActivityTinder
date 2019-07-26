package com.example.activtytinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Models.Event;

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
        holder.tvPeopleAttending.setText("There are " + event.getKeyAttendees().length() + " people attending.");

        Glide.with(context)
                .load(event.getEventImage())
                .into(holder.ivEventImage);
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
}