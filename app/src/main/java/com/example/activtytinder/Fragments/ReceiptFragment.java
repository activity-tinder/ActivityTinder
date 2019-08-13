package com.example.activtytinder.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.activtytinder.Models.Event;
import com.example.activtytinder.R;
import com.example.activtytinder.Tools;
import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptFragment extends Fragment  {


    private Button btnDirections;
    private Button btnChat;
    private Button btnCalendar;
    private Button btnDitch;
    private Button btnShare;
    private ImageView ivPicture;
    private ScrollView scDetails;
    private TextView tvEventName;
    private TextView tvEventTime;
    private TextView tvEventDate;
    private TextView tvEventLocation;
    private TextView tvEventAttendees;
    private TextView tvEventDescription;
    private String mName;
    private ParseUser mCreator;
    private String mDate;
    private String mLocation;
    private ArrayList<String> mAttendees;
    private String mDescription;
    private Event mEvent;
    private String mStartTime;
    private String mEndTime;
    private ViewGroup confettiContainer;
    static boolean Confetti;

    protected int goldDark, goldMed, gold, goldLight;
    protected int[] colors;

    private final List<ConfettiManager> activeConfettiManagers = new ArrayList<>();



    String appFb="fb://page/{fb_page_numerical_id}";
    String urlFb="https://www.facebook.com/{fb_page_name}";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt, container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDirections = view.findViewById(R.id.btnMaps);
        btnChat = view.findViewById(R.id.btnChat);
        btnCalendar = view.findViewById(R.id.btnCalendar);
        btnDitch = view.findViewById(R.id.btnDitchEvent);
        scDetails = view.findViewById(R.id.scDetails);
        tvEventName = view.findViewById(R.id.tvName);
        tvEventTime = view.findViewById(R.id.tvTime);
        tvEventDate = view.findViewById(R.id.tvDate);
        tvEventLocation = view.findViewById(R.id.tvLocation);
        tvEventAttendees = view.findViewById(R.id.tvAttendees);
        tvEventDescription = view.findViewById(R.id.tvDescription);
        ivPicture = view.findViewById(R.id.ivReceiptImage);
        btnShare = view.findViewById(R.id.btnShareEvent);
        mAttendees = new ArrayList<>();
        confettiContainer = view.findViewById(R.id.clReceipt);



        final Resources res = getResources();
        goldDark = res.getColor(R.color.bude_blue);
        gold = res.getColor(R.color.colorAccent);
        goldLight = res.getColor(R.color.colorPinkDark);
        colors = new int[] { goldDark, gold, goldLight };

//        CommonConfetti.rainingConfetti(confettiContainer, new int[] { Color.BLACK }).infinite();



        Bundle eventBundle = this.getArguments();
        if(eventBundle != null){
            mEvent = Parcels.unwrap(eventBundle.getParcelable("Event"));
        }
        else{
            tvEventName.setText("You have not selected an event to attend yet!");
            return;
        }


        //TODO -- explain query
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(Event.KEY_CREATOR);
        query.getInBackground(mEvent.getObjectId(), new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if(e == null){
                    mName = event.getKeyName();
                    mDate = Tools.convertDate(event.getKeyDate());
                    mCreator = event.getCreator();
                    mLocation = event.getKeyAddress();
                    mDescription = event.getKeyDescription();
                    mStartTime = event.getKeyStartTime();
                    mEndTime = event.getKeyEndTime();
                    //TODO-- explain query
                    ParseRelation<ParseUser> relation = event.getRelation("usersAttending");
                    ParseQuery<ParseUser> query = relation.getQuery();
                    query.include(Event.KEY_CREATOR);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if(e == null){
                                for(int x = 0; x < users.size(); x++){
                                    String entry = users.get(x).get("name") + " (@" + users.get(x).getUsername() +")";
                                    mAttendees.add(entry);
                                    if(Confetti) {
//                                        final List<Bitmap> allPossibleConfetti = Utils.generateConfettiBitmaps(new int[] {Color.BLACK},1);
//                                        final int numConfetti = allPossibleConfetti.size();
//                                        final ConfettoGenerator confettoGenerator = new ConfettoGenerator() {
//                                            @Override
//                                            public Confetto generateConfetto(Random random) {
//                                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bude_icon);
//                                                //bitmap.createScaledBitmap(bitmap,4,4,true);
//                                                return new BitmapConfetto(bitmap.createScaledBitmap(bitmap,40,40, true));
//                                            }
//                                        };
//                                        final int containerMiddleX = confettiContainer.getWidth()/2;
//                                        final int containerMiddleY = 0;
//                                        final ConfettiSource confettiSource = new ConfettiSource(containerMiddleX, containerMiddleY);
//                                        new ConfettiManager(getContext(), confettoGenerator, confettiSource, confettiContainer)
//                                                .setEmissionDuration(1000)
//                                                .setEmissionRate(150)
//                                                .setVelocityX(60, 60)
//                                                .setVelocityY(200)
//                                                .setRotationalVelocity(180, 180)
//                                                .animate();
                                        activeConfettiManagers.add(CommonConfetti.rainingConfetti(confettiContainer, colors).stream(2500).setEmissionRate(100));
                                        Confetti = false;
                                    }
                                }

                                ParseFile image = mEvent.getEventImage();
                                if (image != null) {
                                    // TODO -- make nonsecure links secure without cutting strings

                                    /**
                                     * Alters image url from Parse to begin with https instead of http to pass
                                     * Android security requirements.
                                     */
                                    String security = "https";
                                    String url = image.getUrl().substring(4);

                                    Log.d("DEBUG", "in setting image " + security + url);
                                    Glide.with(getContext())
                                            .load(security + url)
                                            .centerCrop()
                                            .dontAnimate()
                                            .into(ivPicture);
                                }

                                tvEventName.setText(mName
                                        + "\n"
                                );
                                tvEventDate.setText(mDate
                                        + "\n"
                                );
                                tvEventLocation.setText(mLocation
                                        +"\n"
                                );
                                tvEventTime.setText(mStartTime + " - " + mEndTime
                                        + "\n"
                                );

                                String attendeesList = "";
                                for (int i = 0; i < mAttendees.size(); i++) {
                                    attendeesList += "    - " + mAttendees.get(i) + "\n";
                                }

                                tvEventAttendees.setText("Created by: "
                                        + mCreator.get("name") + " (@"
                                        + mCreator.getUsername() + ")"
                                        + "\nAttendees:\n"
                                        + attendeesList
                                );
                                tvEventDescription.setText(mDescription
                                        + "\n\n"
                                );
                            }
                            else{
                                Log.e("ReceiptFragment", "There are no attendees");
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    Log.e("ReceiptFragment", "Event Query Failed!");
                    e.printStackTrace();
                }
            }
        });

        btnManager();
    }

    /**
     * Creates an overlaid checkout fragment instance
     * @param event - the event that the user is trying to leave
     */
    public void showLeaveDialog(Event event) {
        FragmentManager fragmentManager = getFragmentManager();
        LeaveFragment leaveDialogFragment = LeaveFragment.newInstance(event);
        leaveDialogFragment.show(fragmentManager, "LeaveFragment");
    }

    //TODO -- explain this
    public void addEvent(String title, String location, long begin, long end, String description) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
                .putExtra(CalendarContract.Events.DESCRIPTION, description);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //TODO -- explain this
    private long checkTime (String time) {
//        time = Tools.convertDate(time);
        String finalTime;
        String myDate;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date date = null;
        if (time.charAt(6) == 'A') {
            finalTime = time.substring(0,5);
            myDate = Tools.convertDate(mDate) + " " + finalTime + ":00";
        }else{
            String errorTime = time.substring(0,2);
            int fixedTime = Integer.parseInt(errorTime);
            fixedTime = fixedTime + 12;
            finalTime = (fixedTime)+ time.substring(2,5);
            myDate =Tools.convertDate( mDate) + " " +finalTime + ":00";
        }
        try{
            date = sdf.parse(myDate);
        }catch (java.text.ParseException e){
            e.printStackTrace();
        }
        long accurateTime = date.getTime();
        return accurateTime;
    }

    private void btnManager() {
        btnDirections.setOnClickListener(btnDirections -> {
            Uri gmmIntentUri = Uri.parse("geo:" + 0 +"," + 0 +"?q="+ mLocation);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        });

        btnChat.setOnClickListener(btnChat -> {
            Intent intent= new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Hi!");
            intent.setType("text/plain");
            intent.setPackage("com.facebook.orca");
            try
            {
                startActivity(intent);
            }
            catch (ActivityNotFoundException ex)
            {
                Toast.makeText(getContext(),
                        "Oops!Can't open Facebook messenger right now. Please try again later.",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnCalendar.setOnClickListener(btnCalendar -> addEvent(mName, mLocation, checkTime(mStartTime), checkTime(mEndTime), mDescription));

        btnDitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO -- give User a warning message/confirmation overlay. If they choose to leave, take User's name off of the users attending. Lower their score if it's 24 hours before event will occur.
                Bundle eventBundle = new Bundle();
                eventBundle.putParcelable("Event", Parcels.wrap(mEvent));
                ReceiptFragment.this.showLeaveDialog(mEvent);
            }
        });


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I'm attending " + mName + "!\nIt's on " + mDate + ", from "+ mStartTime + " to " + mEndTime +".\nYou should sign up through bud.E as well!");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }



}
