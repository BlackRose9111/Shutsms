package com.blackrose.shutsms.ui.CreateGroup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blackrose.shutsms.GroupModel;
import com.blackrose.shutsms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class CreateGroupFragment extends Fragment {

    EditText groupName;
    EditText groupDescription;
    ImageView groupImage;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    RecyclerView recyclerView;
    Uri filePath;
    ArrayList<GroupModel> groups;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        groupName = view.findViewById(R.id.editGroupNameCreate);
        groupDescription = view.findViewById(R.id.editDescription);
        recyclerView = view.findViewById(R.id.groupRecyclerView);
        groupImage = view.findViewById(R.id.groupImageCreate);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK) {
                Intent data = result.getData();
                filePath = data.getData();
                groupImage.setImageURI(filePath);
            }
        });

        groupImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            launcher.launch(Intent.createChooser(intent, "Select Picture"));


        });

        Button createGroup = view.findViewById(R.id.CreateGroupButton);
        createGroup.setOnClickListener(v -> {

            String groupName = this.groupName.getText().toString();
            String groupDescription = this.groupDescription.getText().toString();
            if (groupName.isEmpty() || groupDescription.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {

                if (filePath != null) {
                    CreateGroup(groupName, groupDescription, filePath);
                } else {
                    CreateGroup(groupName, groupDescription, null);
                }


            }

        });


        fetchGroupsFromDb();
        return view;

    }

    public void CreateGroup(String groupName, String groupDescription, Uri filePath) {

        String uuid = UUID.randomUUID().toString();
        HashMap<String, Object> group = new HashMap<>();
        group.put("groupID", uuid);
        group.put("userID", auth.getUid());
        group.put("groupName", groupName);
        group.put("numbers", new ArrayList<String>());
        group.put("groupDescription", groupDescription);
        db.collection("/Groups/").document(uuid).set(group).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Group Created", Toast.LENGTH_SHORT).show();
            if (filePath != null) {
                storage.getReference("/images/" + uuid).putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storage.getReference("/images/" + uuid).getDownloadUrl().addOnSuccessListener(uri -> {
                        group.put("groupImage", uri.toString());
                        db.collection("Groups").document(uuid).set(group).addOnSuccessListener(v -> {
                            Toast.makeText(getContext(), "Picture Uploaded", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                        });
                    });
                });
            }

            fetchGroupsFromDb();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Group Creation Failed", Toast.LENGTH_SHORT).show();
        });




    }

    public void fetchGroupsFromDb() {
        String uid = auth.getCurrentUser().getUid();

        db.collection("/Groups/").whereEqualTo("userID", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            groups = new ArrayList<>();
            System.out.println("GROUP DATA SIZE "+queryDocumentSnapshots.getDocuments().size());
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                GroupModel group = new GroupModel(documentSnapshot.getString("groupName"), documentSnapshot.getString("groupDescription"), documentSnapshot.getString("groupImage"), documentSnapshot.getString("groupID"), (ArrayList<String>) documentSnapshot.get("numbers"), documentSnapshot.getString("userID"));
                groups.add(group);
            }

            GroupAdapter adapter = new GroupAdapter(groups);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to fetch groups", Toast.LENGTH_SHORT).show();

        });


    }


}