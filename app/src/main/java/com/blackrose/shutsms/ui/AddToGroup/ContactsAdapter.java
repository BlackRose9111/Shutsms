package com.blackrose.shutsms.ui.AddToGroup;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blackrose.shutsms.ContactModel;
import com.blackrose.shutsms.IItemClickAble;
import com.blackrose.shutsms.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter {

    private List<ContactModel> contacts;
    IItemClickAble listener;

    public ContactsAdapter(ArrayList<ContactModel> contacts, IItemClickAble listener) {

        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_group_item, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ContactsViewHolder) holder).setData(contacts.get(position));
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private class ContactsViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener{

        ImageView contactImage;
        TextView contactName;
        TextView contactNumber;
        IItemClickAble listener;

        public ContactsViewHolder(@NonNull View parent, IItemClickAble onClickListener) {
            super(parent);
            contactImage = parent.findViewById(R.id.ContactImage);
            contactName = parent.findViewById(R.id.ContactName);
            contactNumber = parent.findViewById(R.id.phoneNumber);
            listener = onClickListener;
            parent.setOnClickListener(this);
        }



        public void setData(ContactModel contactModel) {
            contactName.setText(contactModel.getName());
            contactNumber.setText(contactModel.getNumber());
            if(contactModel.getImage() != null) {
                contactImage.setImageURI(Uri.parse(contactModel.getImage()));
            }



        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());

        }
    }
}
