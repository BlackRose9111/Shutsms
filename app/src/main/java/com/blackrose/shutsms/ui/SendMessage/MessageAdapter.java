package com.blackrose.shutsms.ui.SendMessage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blackrose.shutsms.IItemClickAble;
import com.blackrose.shutsms.MessageModel;
import com.blackrose.shutsms.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    Context context;
    List<MessageModel> messages;
    IItemClickAble listener;


    public MessageAdapter(ArrayList<MessageModel> messages, Context context, IItemClickAble listener) {
        this.messages = messages;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MessageViewHolder holder = new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false), listener);
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

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        IItemClickAble listener;
        public MessageViewHolder(@NonNull View itemView, IItemClickAble listener) {
            super(itemView);
            title = itemView.findViewById(R.id.ItemTitle);
            description = itemView.findViewById(R.id.itemMessage);
            itemView.setOnClickListener(this);
            this.listener = listener;

        }

        public void setData(MessageModel messageModel) {

            title.setText(messageModel.getTitle());
            description.setText(messageModel.getDescription());

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}
