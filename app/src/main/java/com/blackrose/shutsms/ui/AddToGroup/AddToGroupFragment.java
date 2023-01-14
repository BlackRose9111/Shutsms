package com.blackrose.shutsms.ui.AddToGroup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blackrose.shutsms.ContactModel;
import com.blackrose.shutsms.GroupModel;
import com.blackrose.shutsms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Objects;


public class AddToGroupFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseStorage storage;

    ArrayList<GroupModel> groups;
    ArrayList<ContactModel> contacts;
    RecyclerView contactsRecyclerView;
    RecyclerView groupsRecyclerView;
    GroupModel selectedGroup;
    TextView selectedGroupText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_group, container, false);

        contactsRecyclerView = view.findViewById(R.id.contactsSelectContact);
        groupsRecyclerView = view.findViewById(R.id.groupsAddtoGroup);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        selectedGroupText = view.findViewById(R.id.selectedGroup);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
            } else {
                getContacts();
            }
        }
        getContacts();
        getGroups();

        return view;
    }

    private void getGroups() {

        groups = new ArrayList<GroupModel>();
        String uid = auth.getCurrentUser().getUid();

        db.collection("/Groups/").whereEqualTo("userID", uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                    GroupModel group = new GroupModel(documentSnapshot.getString("groupName"), documentSnapshot.getString("groupDescription"), documentSnapshot.getString("groupImage"), documentSnapshot.getString("groupID"), (ArrayList<String>) documentSnapshot.get("numbers"), documentSnapshot.getString("userID"));

                    groups.add(group);
                }
                groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                groupsRecyclerView.setAdapter(new GroupAdapter(groups, getContext(), v -> {

                    selectedGroup = groups.get(v);
                    Toast.makeText(getContext(), selectedGroup.getName(), Toast.LENGTH_SHORT).show();
                    String result = "Selected Group: "+ selectedGroup.getName();
                    selectedGroupText.setText(result);



                }));
            }
        });


    }

    private void getContacts() {
        contacts = new ArrayList<>();
        Cursor cursor = requireContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI) != -1) {
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                contacts.add(new ContactModel(name, number, image));
            } else {
                contacts.add(new ContactModel(name, number, null));
            }


        }
        cursor.close();
        System.out.println(contacts);

        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        contactsRecyclerView.setAdapter(new ContactsAdapter(contacts, v -> {

            ContactModel contact = contacts.get(v);
            if (selectedGroup != null) {
                db.document(
                        "Groups/" + selectedGroup.getId()).get().addOnSuccessListener(documentSnapshot -> {
                    ArrayList<String> numbers = (ArrayList<String>) documentSnapshot.get("numbers");
                    if(numbers.contains(contact.getNumber())){
                        Toast.makeText(getContext(), "This number is already in that group", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    numbers.add(contact.getNumber());
                    db.document("Groups/" + selectedGroup.getId()).update("numbers", numbers);
                    Toast.makeText(getContext(), "Added to group", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getContext(), "Please select a group", Toast.LENGTH_SHORT).show();
            }

        }));


    }
}