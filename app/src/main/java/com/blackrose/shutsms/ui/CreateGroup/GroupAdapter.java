package com.blackrose.shutsms.ui.CreateGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blackrose.shutsms.GroupModel;
import com.blackrose.shutsms.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    List<GroupModel> groups;


    public GroupAdapter(List<GroupModel> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder holder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_group_itemfield, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.setData(groups.get(position));

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImage;
        EditText groupName;
        EditText groupDescription;
        public GroupViewHolder(View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.ItemImage);
            groupName = itemView.findViewById(R.id.itemtitle);
            groupDescription = itemView.findViewById(R.id.itemdescription);


        }

        public View setData(GroupModel groupModel) {

            groupName.setText(groupModel.getName());
            groupDescription.setText(groupModel.getDescription());
            if(groupModel.getImage() != null){
                Picasso.get().load(groupModel.getImage()).into(groupImage);
            }



            return itemView;
        }

    }
}

