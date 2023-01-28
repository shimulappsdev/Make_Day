package com.example.makeday.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.makeday.R;
import com.example.makeday.activities.ContainerActivity;
import com.example.makeday.adapters.ListItemAdapter;
import com.example.makeday.adapters.ShowListItemAdapter;
import com.example.makeday.databinding.FragmentEditListBinding;
import com.example.makeday.models.Item;
import com.example.makeday.models.ListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EditListFragment extends Fragment {

   FragmentEditListBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseUser;
    String currentUser, listId, listItemId, rate;
    ProgressDialog dialog, dialog1;
    private int addYear, addMonth, addDay;
    Calendar calendar;
    ListItemAdapter adapter;
    List<ListModel> listModelList;
    List<Item> itemList;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditListBinding.inflate(getLayoutInflater(), container, false);

        listModelList = new ArrayList<>();
        itemList = new ArrayList<>();

        intent = getActivity().getIntent();
        listId = intent.getStringExtra("listId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        database = FirebaseFirestore.getInstance();

        database.collection("lists")
                .document(currentUser)
                .collection("myLists")
                .whereEqualTo("list_id", listId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<ListModel> data = new ArrayList<>();
                        data = value.toObjects(ListModel.class);
                        if (data != null){
                            binding.titleInput.setText(data.get(0).getList_title());
                            binding.listDateInput.setText(data.get(0).getList_date());
                            binding.listTypeInput.setText(data.get(0).getList_type());
                        }

                    }
                });

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating List");
        dialog.setCancelable(false);

        dialog1 = new ProgressDialog(getActivity());
        dialog1.setTitle("Attaching List Item");
        dialog1.setCancelable(false);


        database = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();
        binding.dateCard.setOnClickListener(view -> {
            addYear = calendar.get(Calendar.YEAR);
            addMonth = calendar.get(Calendar.MONTH);
            addDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    binding.listDateInput.setText(dayOfMonth+ " / "+(monthOfYear+1)+" / "+year);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
            },addYear, addMonth, addDay);
            datePickerDialog.show();
        });

        binding.addItemBtn.setOnClickListener(view -> {
            dialog1.show();
            listItemId = UUID.randomUUID().toString();
            String listTitle = binding.titleInput.getText().toString().trim();
            String listDate = binding.listDateInput.getText().toString().trim();

            String itemName = binding.itemNameInput.getText().toString().trim();
            String itemUnitInput = binding.itemUnitInput.getText().toString().trim();
            String itemAmountInput = binding.itemAmountInput.getText().toString().trim();
            String itemQuantityInput = binding.itemQuantityInput.getText().toString().trim();

            if (itemAmountInput.isEmpty()){
                rate = "0";
            }else {
                rate = itemAmountInput;
            }

            if (listTitle.equals("")){
                binding.titleInput.setError("");
                dialog1.dismiss();
            }else if (listDate.equals("")){
                binding.listDateInput.setError("");
                dialog1.dismiss();
            }else if (itemName.equals("")){
                binding.itemNameInput.setError("");
                dialog1.dismiss();
            }else if (itemUnitInput.equals("")){
                binding.itemUnitInput.setError("");
                dialog1.dismiss();
            }else if (itemQuantityInput.equals("")){
                binding.itemQuantityInput.setError("");
                dialog1.dismiss();
            }else {
                Map<String, Object> listItemMap = new HashMap<>();
                listItemMap.put("item_id", listItemId);
                listItemMap.put("list_id", listId);
                listItemMap.put("item_name", itemName);
                listItemMap.put("item_unit", itemUnitInput);
                listItemMap.put("item_quantity", itemQuantityInput);
                listItemMap.put("item_amount", rate);
                listItemMap.put("item_status", "Pending");

                database.collection("lists")
                        .document(currentUser)
                        .collection("myLists")
                        .document(listId)
                        .collection("list_Items")
                        .document(listItemId)
                        .set(listItemMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog1.dismiss();
                                    binding.itemNameInput.setText("");
                                    binding.itemUnitInput.setText("");
                                    binding.itemQuantityInput.setText("");
                                    binding.itemAmountInput.setText("");
                                    Toast.makeText(getActivity(), "Item Added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog1.dismiss();
                                Toast.makeText(getActivity(), "Oops Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });

        binding.completeListBtn.setOnClickListener(view -> {
            dialog.show();
            String listTitle = binding.titleInput.getText().toString().trim();
            String listType = binding.listTypeInput.getText().toString().trim();
            String listDate = binding.listDateInput.getText().toString().trim();

            if (listTitle.equals("")){
                binding.titleInput.setError("");
                dialog.dismiss();
            }else if (listDate.equals("")){
                binding.listDateInput.setError("");
                dialog.dismiss();
            }else {
                Map<String, Object> listMap = new HashMap<>();
                listMap.put("list_id", listId);
                listMap.put("list_title", listTitle);
                listMap.put("list_type", listType);
                listMap.put("list_date", listDate);

                database.collection("lists")
                        .document(currentUser)
                        .collection("myLists")
                        .document(listId)
                        .update(listMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    Intent intent = new Intent(getActivity(), ContainerActivity.class);
                                    intent.putExtra("list", "startList");
                                    startActivity(intent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Oops Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        });

        database.collection("lists")
                .document(currentUser)
                .collection("myLists")
                .document(listId)
                .collection("list_Items").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        itemList.clear();
                        List<Item> data = value.toObjects(Item.class);
                        itemList.addAll(data);
                        adapter = new ListItemAdapter(getActivity(), itemList);
                        binding.itemRecyclerView.setAdapter(adapter);
                    }
                });

        binding.backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("listId", listId);
            intent.putExtra("showList", "showList");
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }
}