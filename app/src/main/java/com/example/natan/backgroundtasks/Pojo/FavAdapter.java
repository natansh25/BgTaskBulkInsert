package com.example.natan.backgroundtasks.Pojo;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.natan.backgroundtasks.Database.Contract;
import com.example.natan.backgroundtasks.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by natan on 2/7/2018.
 */

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private RecyclerViewClickListenerFav mListener;

    public interface RecyclerViewClickListenerFav {

        //if we want to on click the item index value
        //void onClick(View view, int position);

        //if we want the whole object to retrive the items
        void onClick(Contacts contacts);
    }


    public FavAdapter(Context context, RecyclerViewClickListenerFav listener) {

        mContext = context;
        mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;

        String name = mCursor.getString(mCursor.getColumnIndex(Contract.Fav.COLUMN_NAME));
        String phone = mCursor.getString(mCursor.getColumnIndex(Contract.Fav.COLUMN_PHONE));
        String image = mCursor.getString(mCursor.getColumnIndex(Contract.Fav.COLUMN_IMAGE));
        int id = mCursor.getInt(mCursor.getColumnIndex(Contract.Fav._ID));

        holder.itemView.setTag(id);
        holder.txt_name.setText(name);
        Picasso.with(mContext).load(image).into(holder.mCircleImageView);


    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView mCircleImageView;
        TextView txt_name;


        public MyViewHolder(View itemView) {
            super(itemView);

            mCircleImageView = itemView.findViewById(R.id.img_profile);
            txt_name = itemView.findViewById(R.id.txt_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mCursor.moveToPosition(getAdapterPosition());
            String name = mCursor.getString(mCursor.getColumnIndex(Contract.Fav.COLUMN_NAME));
            Log.i("xyz",name);
            String phone = mCursor.getString(mCursor.getColumnIndex(Contract.Fav.COLUMN_PHONE));
            String image = mCursor.getString(mCursor.getColumnIndex(Contract.Fav.COLUMN_IMAGE));
            Contacts contacts = new Contacts(name, image, phone);
            mListener.onClick(contacts);


        }
    }


}
