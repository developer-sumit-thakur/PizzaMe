package com.st.pizzame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.st.pizzame.R;

import java.util.List;

import com.st.pizzame.model.Result;

/**
 * Created by sumit.thakur on 5/12/18.
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private static final String TAG = AddressListAdapter.class.getSimpleName();
    private List<Result> storeLocation;
    private ViewHolderClickListener listener;

    public interface ViewHolderClickListener {
        void onCall(String phone);
        void onDirectionClick(String url);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView distance;
        public TextView address;
        public TextView phone;
        public RelativeLayout callLayout;
        public FrameLayout direction;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.store_name);
            distance = view.findViewById(R.id.distance);
            address = view.findViewById(R.id.store_address);
            phone = view.findViewById(R.id.store_phone);
            callLayout = view.findViewById(R.id.store_phone_layout);
            direction = view.findViewById(R.id.direction_layout);
        }
    }

    public AddressListAdapter(List<Result> storeLocation, ViewHolderClickListener clickListener) {
        this.storeLocation = storeLocation;
        this.listener = clickListener;
    }

    public void setStoreLocation(List<Result> location) {
        this.storeLocation = location;
        notifyDataSetChanged();
    }

    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_option_item, parent, false);

        ViewHolder vh = new ViewHolder(root);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position % 2 == 0) holder.itemView.setBackgroundResource(R.color.itemViewColor1);
        else holder.itemView.setBackgroundResource(R.color.itemViewColor2);

        holder.name.setText(storeLocation.get(position).getTitle());
        String address = storeLocation.get(position).getAddress()
                + " , " + storeLocation.get(position).getCity()
                + " , " + storeLocation.get(position).getState();
        holder.address.setText(address);
        holder.distance.setText(storeLocation.get(position).getDistance() + " mi");
        holder.phone.setText(storeLocation.get(position).getPhone());
        holder.callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCall(storeLocation.get(position).getPhone());
            }
        });

        holder.direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDirectionClick(storeLocation.get(position).getMapUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return storeLocation != null ? storeLocation.size() : 0;
    }
}

