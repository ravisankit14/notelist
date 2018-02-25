package com.note.views.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.note.Constants;
import com.note.NoteApp;
import com.note.R;
import com.note.database.db.DaoSession2;
import com.note.database.db.Note;
import com.note.views.AddActivity;
import com.note.views.AddNoteFragment;
import com.note.views.NoteList.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final List<Note> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int lastPosition = -1;

    public MyItemRecyclerViewAdapter(Context context,@NonNull List<Note> items,@NonNull OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getText());
        holder.mCreatedTime.setText(mValues.get(position).getCreated_at());

        if(mValues.get(position).getImage_path() != null){
            Glide.with(mContext).load(mValues.get(position).getImage_path())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImageView);
        }else{
            Glide.with(mContext).load(R.mipmap.note)
                    .into(holder.mImageView);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AddActivity.class);
                intent.putExtra(Constants._ID,mValues.get(position).getId());
                intent.putExtra("fromAdd",true);
                mContext.startActivity(intent);
            }
        });

        setAnimation(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        if(mValues.size() > 0){
            return mValues.size();
        }
        return 0;
    }

    public void removeItem(int position) {
        Log.e("adapter",mValues.get(position).getId().toString());
        getAppDaoSession().getNoteDao().deleteByKey(mValues.get(position).getId());
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Note note, int position) {
        mValues.add(position, note);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final View mView;
        private final TextView mIdView;
        private final TextView mContentView;
        private final TextView mCreatedTime;
        private final ImageView mImageView;
        private final ImageView mEditView;

        private Note mItem;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.note_title);
            mContentView = (TextView) view.findViewById(R.id.note_description);
            mCreatedTime = (TextView) view.findViewById(R.id.note_createdTime);
            mImageView = (ImageView) view.findViewById(R.id.note_imageView);
            mEditView = (ImageView) view.findViewById(R.id.note_editView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }


    }

    private DaoSession2 getAppDaoSession() {
        return ((NoteApp)mContext.getApplicationContext()).getDaoSession2();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            animation.setDuration(500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}