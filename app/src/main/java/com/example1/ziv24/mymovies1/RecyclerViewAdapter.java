package com.example1.ziv24.mymovies1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example1.ziv24.mymovies1.db.DBHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ziv24 on 21/03/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Movies> mData;
    private Activity mActivity;
    private int mPosition;
    AlertDialog dialog;
    DBHandler handler;

    public RecyclerViewAdapter(Context mContext, List<Movies> mData, Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_movie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        this.mPosition = position;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mActivity);
                View va = mActivity.getLayoutInflater().inflate(R.layout.dialog, null);
                TextView dialog_tv = (TextView) va.findViewById(R.id.tv_dialog);
                dialog_tv.setText(R.string.dialog_add);

                FloatingActionButton fab_remove = va.findViewById(R.id.fab_remove);
                fab_remove.setImageResource(R.drawable.ic_save_black_24dp);
                fab_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        handler = new DBHandler(mContext);
                        handler.addMovie(mData.get(position).getOriginal_title(), mData.get(position).getOverview(), mData.get(position).getVote_average(), mData.get(position).getPoster());
                        mActivity.recreate();
                        dialog.cancel();

                    }
                });
                FloatingActionButton fab_cancel = va.findViewById(R.id.fab_cancel);
                fab_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                mBuilder.setView(va);

                dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                dialog.getWindow().getAttributes().windowAnimations = R.style.UpDownAnim;
                dialog.show();

                return true;

            }
        });
        holder.cardText.setText(mData.get(position).getOriginal_title());
        float rating = mData.get(position).getVote_average() / 2;
        holder.cartRating.setRating(rating);
        String imageURL = mData.get(position).getPoster();
        Picasso.with(mContext).load(imageURL).resize(320, 380).into(holder.cardImage);
        if (mData.get(position).getPoster() == null) {
            holder.cardImage.setImageResource(R.drawable.ic_add_black_24dp);
        }
        final Intent intent = new Intent(mActivity, Add_Activity.class);

        intent.putExtra("id", mData.get(mPosition).getId());
        intent.putExtra("title", mData.get(mPosition).getOriginal_title());
        intent.putExtra("overview", mData.get(mPosition).getOverview());
        intent.putExtra("vote_average", mData.get(mPosition).getVote_average());
        intent.putExtra("poster_path", mData.get(mPosition).getPoster());
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


        public ViewHolder(View itemView) {
            super(itemView);
            cardText = itemView.findViewById(R.id.title_card);
            cardImage = itemView.findViewById(R.id.image_card);
            cartRating = itemView.findViewById(R.id.rating_card);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });

        }
    }

}
