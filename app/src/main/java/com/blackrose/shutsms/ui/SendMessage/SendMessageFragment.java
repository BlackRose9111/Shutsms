package com.blackrose.shutsms.ui.SendMessage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blackrose.shutsms.GroupModel;
import com.blackrose.shutsms.MessageModel;
import com.blackrose.shutsms.R;
import com.blackrose.shutsms.ui.AddToGroup.GroupAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SendMessageFragment extends Fragment {

    TextView selectedMessageView;
    TextView selectedGroupView;
    FirebaseAuth auth;
    FirebaseFirestore db;

    RecyclerView groupsRecyclerView;
    RecyclerView messagesRecyclerView;


    ArrayList<GroupModel> groups;
    ArrayList<MessageModel> messages;

    GroupModel selectedGroup;
    MessageModel selectedMessage;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_send_message, container, false);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(getContext().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.SEND_SMS"}, 1);
                }


        }

        selectedMessageView = view.findViewById(R.id.SelectedMessage);
        selectedGroupView = view.findViewById(R.id.selectedGroup);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        groupsRecyclerView = view.findViewById(R.id.groupsAddtoGroup);
        messagesRecyclerView = view.findViewById(R.id.contactsSelectContact);

        GetGroups();
        GetMessages();

        view.findViewById(R.id.SendMassMessageButton).setOnClickListener(v -> SendMessage());




        return view;
    }

    private void SendMessage() {
        if(selectedMessage == null){
            Toast.makeText(getContext(), "Please select a message", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedGroup == null){
            Toast.makeText(getContext(), "Please select a group", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> numbers = selectedGroup.getNumbers();
        SmsManager smsManager = SmsManager.getDefault();

        for (String number : numbers) {
            smsManager.sendTextMessage(number, null, selectedMessage.getDescription(), null, null);

        }
        Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();



    }

    private void GetMessages() {
        messages = new ArrayList<>();
        db.collection("/messages/").whereEqualTo("author",auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    MessageModel message = document.toObject(MessageModel.class);
                    messages.add(message);
                }
                messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                messagesRecyclerView.setAdapter(new MessageAdapter(messages,getContext(),v->{


                    selectedMessage = messages.get(v);
                    String text = "Selected Message: " + messages.get(v).getTitle();
                    selectedMessageView.setText(text);
                }));




            }
        });




    }

    public void GetGroups(){

        groups = new ArrayList<>();

        db.collection("/Groups/").whereEqualTo("userID",auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                    GroupModel group = new GroupModel(documentSnapshot.getString("groupName"), documentSnapshot.getString("groupDescription"), documentSnapshot.getString("groupImage"), documentSnapshot.getString("groupID"), (ArrayList<String>) documentSnapshot.get("numbers"), documentSnapshot.getString("userID"));
                    groups.add(group);
                }


            }
            groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
            groupsRecyclerView.setAdapter(new GroupAdapter(groups,getContext(),v->{

                selectedGroup = groups.get(v);
                String text = "Selected Group: " + selectedGroup.getName();
                selectedGroupView.setText(text);
            }));


        });






    }
}