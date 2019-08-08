package com.example.activtytinder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Fragments.ReceiptFragment;
import com.example.activtytinder.Models.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import static com.example.activtytinder.MainActivity.fragmentManager;

//TODO -- explain this adapter
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
        holder.tvDate.setText(Tools.convertDate(event.getKeyDate()));
        //holder.tvLocation.setText(event.getKeyAddress());
        //TODO - explain this query
        ParseRelation<ParseUser> relation = event.getRelation("usersAttending");
        ParseQuery<ParseUser> query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(users.size() == 1){
                    holder.tvPeopleAttending.setText(+ users.size() +" person attending");
                }
                else{
                    holder.tvPeopleAttending.setText(users.size() +" people attending");
                }

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
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivEventImage;
        public TextView tvName;
        public TextView tvDate;
        public TextView tvPeopleAttending;

        public ViewHolder(View itemview) {
            super(itemview);

            ivEventImage = itemview.findViewById(R.id.ivEventImage);
            tvName = itemview.findViewById(R.id.tvName);
            tvDate = itemview.findViewById(R.id.tvDate);
            tvPeopleAttending= itemview.findViewById(R.id.tvPeopleAttending);
        }
        public void bind(final Event event){
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //TODO -- explain all the fragment stuff
                   Fragment fragment = new ReceiptFragment();
                   Bundle bundle = new Bundle();
                   bundle.putParcelable("Event", Parcels.wrap(event));
                   fragment.setArguments(bundle);

                   fragmentManager.popBackStack();
                   fragmentManager.beginTransaction().addToBackStack("Receipt").replace(R.id.flContainer, fragment).commit();
               }
           });
        }

    }
    //TODO -- explain this
    public void clear(){
        mEvents.clear();
        notifyDataSetChanged();
    }

}
