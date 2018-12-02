package com.example1.ziv24.mymovies1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by ziv24 on 10/04/2018.
 */

public class MyMoviesAdapter extends RecyclerView.Adapter<MyMoviesAdapter.ViewHolder> {

    private OnItemLongClickListener mListener;
    private Context mContext;
    private List<Movies> mData;
    private Activity mActivity;
    private int mPosition;

    public interface OnItemLongClickListener {
        void onItemClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mListener = listener;

    }

    public MyMoviesAdapter(Context mContext, List<Movies> mData, Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_movie, parent, false);

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.mPosition = position;
        holder.cardText.setText(mData.get(position).getOriginal_title());
        float rating = mData.get(position).getVote_average() / 2;
        holder.cartRating.setRating(rating);
        String imageURL = mData.get(position).getPoster();
        if (URLUtil.isValidUrl(imageURL)) {
            Picasso.with(mContext).load(imageURL).resize(320, 380).into(holder.cardImage);
        } else {
            try {


                File f = new File(imageURL);
                Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                holder.cardImage.setImageBitmap(bm);
            } catch (Exception e) {

            }
        }
        if (mData.get(position).getPoster() == null) {
            holder.cardImage.setImageResource(R.drawable.ic_add_black_24dp);
        }
        final Intent intent = new Intent(mActivity, Add_Activity.class);

        intent.putExtra("id", mData.get(mPosition).getId());
        intent.putExtra("title", mData.get(mPosition).getOriginal_title());
        intent.putExtra("overview", mData.get(mPosition).getOverview());
        intent.putExtra("vote_average", mData.get(mPosition).getVote_average());
        intent.putExtra("poster_path", mData.get(mPosition).getPoster());
        if (mData.get(mPosition).getId() == -3) {
            intent.putExtra("flag", -1);
        } else {
            intent.putExtra("flag", 1);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivity.startActivityForResult(intent, 17);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardText;
        ImageView cardImage;
        RatingBar cartRating;


        public ViewHolder(View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            cardText = itemView.findViewById(R.id.title_card);
            cardImage = itemView.findViewById(R.id.image_card);
            cartRating = itemView.findViewById(R.id.rating_card);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}

