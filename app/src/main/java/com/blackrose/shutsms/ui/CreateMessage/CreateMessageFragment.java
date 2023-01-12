package com.blackrose.shutsms.ui.CreateMessage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.blackrose.shutsms.MessageModel;
import com.blackrose.shutsms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class CreateMessageFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    EditText title, description;
    RecyclerView recyclerView;
    ArrayList<MessageModel> messages;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_message, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        title = view.findViewById(R.id.CreateMessageItemTitle);
        description = view.findViewById(R.id.CreateMessageItemDescription);
        recyclerView = view.findViewById(R.id.CreateMessageRecycler);

        view.findViewById(R.id.CreateMessageButton).setOnClickListener(v -> {
            CreateMessage();


        });
        FetchMessages();



        return view;
    }
    public void CreateMessage(){
        String uuid = UUID.randomUUID().toString();
        HashMap<String, Object> message = new HashMap<>();
        message.put("title", title.getText().toString());
        message.put("description", description.getText().toString());
        message.put("uuid", uuid);
        message.put("author", auth.getCurrentUser().getUid());
        db.collection("/messages/").document(uuid).set(message).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Message Created", Toast.LENGTH_SHORT).show();
            FetchMessages();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Message Creation Failed", Toast.LENGTH_SHORT).show();
        });




    }

    public void FetchMessages(){

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        messages = new ArrayList<>();
        db.collection("/messages/").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                MessageModel message = new MessageModel(snapshot.getString("title"), snapshot.getString("description"), snapshot.getString("uuid"), snapshot.getString("author"));
                messages.add(message);
            }
            MessageAdapter adapter = new MessageAdapter(messages, getContext());
            recyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to fetch messages", Toast.LENGTH_SHORT).show();
        });




    }



}