package com.blackrose.shutsms.ui.CreateMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blackrose.shutsms.MessageModel;
import com.blackrose.shutsms.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    Context context;
    List<MessageModel> messages;


    public MessageAdapter(ArrayList<MessageModel> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MessageViewHolder holder = new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false));
        return  holder;



    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        holder.setData(messages.get(position));


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ItemTitle);
            description = itemView.findViewById(R.id.itemMessage);

        }

        public void setData(MessageModel messageModel) {

            title.setText(messageModel.getTitle());
            description.setText(messageModel.getDescription());

        }
    }
}
