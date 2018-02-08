package com.example.natan.backgroundtasks.Pojo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.natan.backgroundtasks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by natan on 2/3/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<Contacts> mContacts;

    private RecyclerViewClickListener mListener;



    public MyAdapter(List<Contacts> contacts, RecyclerViewClickListener listener) {
        mContacts = contacts;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {

        //if we want to on click the item index value
        //void onClick(View view, int position);

        //if we want the whole object to retrive the items
        void onClick(Contacts contacts);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Contacts contacts = mContacts.get(position);
        Context context = holder.mCircleImageView.getContext();

        holder.txt_name.setText(contacts.getName());
        Picasso.with(context).load(contacts.getImage()).into(holder.mCircleImageView);


    }

    @Override
    public int getItemCount() {
        return mContacts.size();
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


            // for whole object to be passed
            int adapterPosition = getAdapterPosition();
            Contacts clicked = mContacts.get(adapterPosition);
            mListener.onClick(clicked);
        }
    }


}
