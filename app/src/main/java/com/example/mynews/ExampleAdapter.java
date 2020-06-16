package com.example.mynews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;

    Map<String,?> hm;


//    private OnItemClickListener mListener;

//    public interface OnItemClickListener{
//        void onItemClick(int position);
//
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener)
//    {
//        mListener=listener;
//    }

    public ExampleAdapter(Context context,ArrayList<ExampleItem> exampleList){
        mContext=context;
        mExampleList=exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
//        final ExampleViewHolder holder=new ExampleViewHolder(v);




        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, int position) {
        final ExampleItem currentItem = mExampleList.get(position);

        String imageUrl = currentItem.getImageResource();
        final String text1 = currentItem.getText1();
        String text2 = currentItem.getText2();
        String text3 = currentItem.getText3();
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        hm=sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : hm.entrySet())
        {
            Log.d("datas",""+entry.getKey());
            if(currentItem.getArticleid().equals(entry.getKey()))
            {
                holder.mBookmark.setImageResource(R.drawable.solid_bookmark);
            }
        }
        ZonedDateTime time_in_zone_format = ZonedDateTime.parse(text2);
//            Log.d("news_time",""+time_in_zone_format);
        ZonedDateTime time_zone_LA = time_in_zone_format.withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ));
//            Log.d("news_time",""+time_zone_LA);
        LocalDateTime news_time_local =  time_zone_LA.toLocalDateTime();
//            Log.i("news_time",""+time_in_zone_format);
//            Log.d("news_time_local",""+news_time_local);

        LocalDateTime current_time_local = LocalDateTime.now();            //Local date time
//            Log.i("current_time_local",""+current_time_local);

        long time_difference;
        String time;
        time_difference = ChronoUnit.DAYS.between(news_time_local,current_time_local);
        if(time_difference < 1)
        {
            time_difference = ChronoUnit.HOURS.between(news_time_local,current_time_local);
            if(time_difference < 1)
            {
                time_difference = ChronoUnit.MINUTES.between(news_time_local, current_time_local);
                if(time_difference < 1)
                {
                    time_difference = ChronoUnit.SECONDS.between(news_time_local,current_time_local);
                    time = String.valueOf(time_difference)+"s ago";
                }
                else
                {
                    time = String.valueOf(time_difference)+"m ago";
                }
            }
            else
            {
                time = String.valueOf(time_difference)+"h ago";
            }
        }
        else
        {
            time = String.valueOf(time_difference)+"d ago";
        }



        holder.mText1.setText(text1);
        holder.mText2.setText(time);
        holder.mText3.setText(text3);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);


        final String articleid = currentItem.getArticleid();
        Log.d("articleid",""+articleid);
        holder.mBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hm=sharedPreferences.getAll();
                if(!hm.containsKey((articleid)))
                {
                holder.mBookmark.setImageResource(R.drawable.solid_bookmark);
                Gson gson = new Gson();
                Log.d("share",""+sharedPreferences.getString(articleid,""));
                editor.putString(articleid, gson.toJson(currentItem));
                editor.commit();
                    Toast.makeText(mContext,"\""+text1+"\" was added to bookmarks",Toast.LENGTH_SHORT).show();

//                editor.clear();
//                editor.commit();
                }
                else
                {
                    holder.mBookmark.setImageResource(R.drawable.hollow_bookmark);
                    editor.remove(articleid);
                    editor.commit();
                    Toast.makeText(mContext,"\""+text1+"\" was removed from bookmarks",Toast.LENGTH_SHORT).show();

                }
            }
        });
        Log.d("share",""+sharedPreferences.getString(articleid,""));


        holder.cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent( mContext.getApplicationContext(),DetailActivity.class);
                String title = mExampleList.get(holder.getAdapterPosition()).getArticleid();
                detailIntent.putExtra("title",title);
//        ExampleItem clickedItem=mExampleList.get(position);
//
//        detailIntent.putExtra(EXTRA_URL,clickedItem.getImageResource());
//        detailIntent.putExtra(EXTRA_Text1,clickedItem.getText1());
//        detailIntent.putExtra(EXTRA_Text2,clickedItem.getText2());
//        detailIntent.putExtra(EXTRA_Text3,clickedItem.getText3());
//
        mContext.getApplicationContext().startActivity(detailIntent);
            }
        });

        holder.cards.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final String title = mExampleList.get(holder.getAdapterPosition()).getText1();
                Log.d("dhruv",title);
                String imgurl=mExampleList.get(holder.getAdapterPosition()).getImageResource();
                Log.d("dhruv",imgurl);
                final String articleid=mExampleList.get(holder.getAdapterPosition()).getArticleid();
                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Title...");
                TextView text = (TextView) dialog.findViewById(R.id.text_view_dialog3);
                text.setText(title);
                ImageView twitter=dialog.findViewById(R.id.twitter);
                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent hh=new Intent(Intent.ACTION_VIEW);
                        String tt="Check out this Link: https://theguardian.com/"+mExampleList.get(holder.getAdapterPosition()).getArticleid();
                        hh.setData(Uri.parse("https://twitter.com/intent/tweet?text="+tt+"&hashtags=CSCI571NewsSearch"));
                        mContext.getApplicationContext().startActivity(hh);
                    }
                });
                final ImageView bookmark=dialog.findViewById(R.id.bookmark);
                final SharedPreferences sharedPreferences = mContext.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();

                hm=sharedPreferences.getAll();
                for(Map.Entry<String,?> entry : hm.entrySet())
                {
                    Log.d("datas",""+entry.getKey());
                    if(articleid.equals(entry.getKey()))
                    {
                        bookmark.setImageResource(R.drawable.solid_bookmark);
                    }
                }

                bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hm=sharedPreferences.getAll();
                        if(!hm.containsKey((articleid)))
                        {
                            holder.mBookmark.setImageResource(R.drawable.solid_bookmark);
                            bookmark.setImageResource(R.drawable.solid_bookmark);
                            Gson gson = new Gson();
                            Log.d("share",""+sharedPreferences.getString(articleid,""));
                            editor.putString(articleid, gson.toJson(currentItem));
                            editor.commit();
                            Toast.makeText(mContext,"\""+title+"\" was added to bookmarks",Toast.LENGTH_SHORT).show();
//                editor.clear();
//                editor.commit();
                        }
                        else
                        {
                            holder.mBookmark.setImageResource(R.drawable.hollow_bookmark);
                            bookmark.setImageResource(R.drawable.hollow_bookmark);
                            editor.remove(articleid);
                            editor.commit();
                            Toast.makeText(mContext,"\""+title+"\" was removed from bookmarks",Toast.LENGTH_SHORT).show();
                        }
                    }
                });




//                TextView text1 = (TextView) dialog.findViewById(R.id.text_view_dialog2);
//                text1.setText("Twitter");
//                TextView text2 = (TextView) dialog.findViewById(R.id.text_view_dialog1);
//                text2.setText("Bookmark");

                ImageView image = (ImageView) dialog.findViewById(R.id.image_view_dialog);
                Picasso.get().load(imgurl).into(image);
                Log.d("dhruvi",title);
                dialog.show();
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        if(mExampleList.size()>10)
            return 10;
        return mExampleList.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView,mBookmark;
        public TextView mText1;
        public TextView mText2;
        public TextView mText3;
        CardView cards;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mText1 = itemView.findViewById(R.id.textView);
            mText2 = itemView.findViewById(R.id.textView2);
            mText3 = itemView.findViewById(R.id.textView3);
            cards=itemView.findViewById(R.id.cards);
            mBookmark = itemView.findViewById(R.id.bookmarking);
//
//
//            itemView.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v){
//                    if(mListener !=null){
//                        int position=getAdapterPosition();
//                        if(position!=RecyclerView.NO_POSITION){
//                            mListener.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }

//    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
//        mExampleList = exampleList;
//    }

//    @Override
//    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
//        ExampleViewHolder evh = new ExampleViewHolder(v);
//        return evh;
//    }
//
//    @Override
//    public void onBindViewHolder(ExampleViewHolder holder, int position) {
//        ExampleItem currentItem = mExampleList.get(position);
//
//        holder.mImageView.setImageResource(currentItem.getImageResource());
//        holder.mTextView1.setText(currentItem.getText1());
//        holder.mTextView2.setText(currentItem.getText2());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mExampleList.size();
//    }
}
