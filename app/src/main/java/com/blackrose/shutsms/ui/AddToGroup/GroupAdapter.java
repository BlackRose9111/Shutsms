package com.blackrose.shutsms.ui.AddToGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.blackrose.shutsms.GroupModel;
import com.blackrose.shutsms.IItemClickAble;
import com.blackrose.shutsms.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> implements IItemClickAble {
    Context context;
    ArrayList<GroupModel> groups;
    IItemClickAble listener;


    public GroupAdapter(ArrayList<GroupModel> groups, Context context, IItemClickAble listener) {
        this.groups = groups;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_group_group_item, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.SetData(groups.get(position));
    }

    @Override
    public int getItemCount() {

        return groups.size();
    }

    @Override
    public void onItemClick(int position) {

    }


    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        IItemClickAble listener;
        TextView groupName;
        ImageView groupImage;
        TextView groupDescription;


        public GroupViewHolder(View parent, IItemClickAble listener) {
            super(parent);
            this.listener = listener;
            groupImage = itemView.findViewById(R.id.GroupImageAddToGroup);
            groupName = itemView.findViewById(R.id.GroupName);
            groupDescription = itemView.findViewById(R.id.GroupDescriptionAddToGroup);
            parent.setOnClickListener(this);

        }
        public void SetData(GroupModel model){
            groupName.setText(model.getName());
            groupDescription.setText(model.getDescription());
            if (model.getImage() != null) {
                Picasso.get().load(model.getImage()).into(groupImage);
            }


        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());

        }
    }
}
