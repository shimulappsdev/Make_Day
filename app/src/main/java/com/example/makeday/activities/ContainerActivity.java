package com.example.makeday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.makeday.R;
import com.example.makeday.databinding.ActivityContainerBinding;
import com.example.makeday.fragments.AddListFragment;
import com.example.makeday.fragments.AddNoteFragment;
import com.example.makeday.fragments.AddTaskFragment;
import com.example.makeday.fragments.CategoryContainerFragment;
import com.example.makeday.fragments.DeleteFragment;
import com.example.makeday.fragments.DeleteListFragment;
import com.example.makeday.fragments.DeleteNoteFragment;
import com.example.makeday.fragments.EditListFragment;
import com.example.makeday.fragments.EditNoteFragment;
import com.example.makeday.fragments.EveryDayNoteFragment;
import com.example.makeday.fragments.HealthFragment;
import com.example.makeday.fragments.ImageViewFragment;
import com.example.makeday.fragments.ListMainFragment;
import com.example.makeday.fragments.NoteImageFragment;
import com.example.makeday.fragments.NoteImageListFragment;
import com.example.makeday.fragments.NoteMainFragment;
import com.example.makeday.fragments.NoteShowFragment;
import com.example.makeday.fragments.OtherFragment;
import com.example.makeday.fragments.ParticipantFragment;
import com.example.makeday.fragments.PersonalFragment;
import com.example.makeday.fragments.PersonalNoteFragment;
import com.example.makeday.fragments.ProfessionalNoteFragment;
import com.example.makeday.fragments.ProfileFragment;
import com.example.makeday.fragments.SettingFragment;
import com.example.makeday.fragments.ShoppingFragment;
import com.example.makeday.fragments.ShowListFragment;
import com.example.makeday.fragments.ShowTaskFragment;
import com.example.makeday.fragments.TodoMainFragment;
import com.example.makeday.fragments.TravelNoteFragment;
import com.example.makeday.fragments.UpdateTaskFragment;
import com.example.makeday.fragments.WorkFragment;

public class ContainerActivity extends AppCompatActivity {

    ActivityContainerBinding binding;
    TodoMainFragment todoMainFragment = new TodoMainFragment();
    NoteMainFragment noteMainFragment = new NoteMainFragment();
    ListMainFragment listMainFragment = new ListMainFragment();
    AddTaskFragment addTaskFragment = new AddTaskFragment();
    UpdateTaskFragment updateTaskFragment = new UpdateTaskFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    WorkFragment workFragment = new WorkFragment();
    PersonalFragment personalFragment = new PersonalFragment();
    ShoppingFragment shoppingFragment = new ShoppingFragment();
    HealthFragment healthFragment = new HealthFragment();
    OtherFragment otherFragment = new OtherFragment();
    CategoryContainerFragment categoryContainerFragment = new CategoryContainerFragment();
    SettingFragment settingFragment = new SettingFragment();
    ParticipantFragment participantFragment = new ParticipantFragment();
    DeleteFragment deleteFragment = new DeleteFragment();
    ShowTaskFragment showTaskFragment = new ShowTaskFragment();
    NoteShowFragment noteShowFragment = new NoteShowFragment();
    AddNoteFragment addNoteFragment = new AddNoteFragment();
    NoteImageFragment noteImageFragment = new NoteImageFragment();
    EditNoteFragment editNoteFragment = new EditNoteFragment();
    EveryDayNoteFragment everyDayNoteFragment = new EveryDayNoteFragment();
    PersonalNoteFragment personalNoteFragment = new PersonalNoteFragment();
    ProfessionalNoteFragment professionalNoteFragment = new ProfessionalNoteFragment();
    TravelNoteFragment travelNoteFragment = new TravelNoteFragment();
    DeleteNoteFragment deleteNoteFragment = new DeleteNoteFragment();
    NoteImageListFragment noteImageListFragment = new NoteImageListFragment();
    ImageViewFragment imageViewFragment = new ImageViewFragment();
    AddListFragment addListFragment = new AddListFragment();
    DeleteListFragment deleteListFragment = new DeleteListFragment();
    ShowListFragment showListFragment = new ShowListFragment();
    EditListFragment editListFragment = new EditListFragment();

    Intent intent;
    String todo, note, list, addTask,
            updateTask, work, personal,
            shopping, health, other,
            allTask, profile, setting,
            participant, delete, showTask,
            showNote, addNote, noteImage,
            editNote, everyDayNote, personalNote,
            professionalNote, travelNote, deleteNote,
            noteImageList, imageView, addList,
            deleteList, showList, editList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        profile = intent.getStringExtra("profile");
        todo = intent.getStringExtra("todo");
        note = intent.getStringExtra("startNote");
        list = intent.getStringExtra("list");
        addTask = intent.getStringExtra("addTask");
        updateTask = intent.getStringExtra("updateTask");
        work = intent.getStringExtra("work");
        personal = intent.getStringExtra("personal");
        shopping = intent.getStringExtra("shopping");
        health = intent.getStringExtra("health");
        other = intent.getStringExtra("other");
        allTask = intent.getStringExtra("allTask");
        setting = intent.getStringExtra("setting");
        participant = intent.getStringExtra("participant");
        delete = intent.getStringExtra("delete");
        showTask = intent.getStringExtra("showTask");
        showNote = intent.getStringExtra("showNote");
        addNote = intent.getStringExtra("addNote");
        noteImage = intent.getStringExtra("noteImage");
        editNote = intent.getStringExtra("editNote");
        everyDayNote = intent.getStringExtra("everyDayNote");
        personalNote = intent.getStringExtra("personalNote");
        professionalNote = intent.getStringExtra("professionalNote");
        travelNote = intent.getStringExtra("travelNote");
        deleteNote = intent.getStringExtra("deleteNote");
        noteImageList = intent.getStringExtra("noteImageList");
        imageView = intent.getStringExtra("imageView");
        addList = intent.getStringExtra("addList");
        deleteList = intent.getStringExtra("deleteList");
        showList = intent.getStringExtra("showList");
        editList = intent.getStringExtra("editList");

        if (profile != null){
            if (profile.equals("profile")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, profileFragment).commit();
            }
        }

        if (todo != null){
            if (todo.equals("startTodo")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, todoMainFragment).commit();
            }
        }

        if (note != null){
            if (note.equals("startNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, noteMainFragment).commit();
            }
        }

        if (list != null){
            if (list.equals("startList")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, listMainFragment).commit();
            }
        }

        if (addTask != null){
            if (addTask.equals("addTask")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, addTaskFragment).commit();
            }
        }

        if (work != null){
            if (work.equals("work")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, workFragment).commit();
            }
        }

        if (personal != null){
            if (personal.equals("personal")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, personalFragment).commit();
            }
        }

        if (shopping != null){
            if (shopping.equals("shopping")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, shoppingFragment).commit();
            }
        }

        if (health != null){
            if (health.equals("health")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, healthFragment).commit();
            }
        }

        if (other != null){
            if (other.equals("other")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, otherFragment).commit();
            }
        }

        if (allTask != null){
            if (allTask.equals("allTask")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, categoryContainerFragment).commit();
            }
        }

        if (setting != null){
            if (setting.equals("setting")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, settingFragment).commit();
            }
        }

        if (participant != null){
            if (participant.equals("participant")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, participantFragment).commit();
            }
        }

        if (updateTask != null){
            if (updateTask.equals("updateTask")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, updateTaskFragment).commit();
            }
        }

        if (delete != null){
            if (delete.equals("delete")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, deleteFragment).commit();
            }
        }

        if (showTask != null){
            if (showTask.equals("showTask")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, showTaskFragment).commit();
            }
        }

        if (addNote != null){
            if (addNote.equals("addNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, addNoteFragment).commit();
            }
        }

        if (noteImage != null){
            if (noteImage.equals("noteImage")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, noteImageFragment).commit();
            }
        }

        if (showNote != null){
            if (showNote.equals("showNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, noteShowFragment).commit();
            }
        }

        if (editNote != null){
            if (editNote.equals("editNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, editNoteFragment).commit();
            }
        }

        if (everyDayNote != null){
            if (everyDayNote.equals("everyDayNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, everyDayNoteFragment).commit();
            }
        }

        if (personalNote != null){
            if (personalNote.equals("personalNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, personalNoteFragment).commit();
            }
        }

        if (professionalNote != null){
            if (professionalNote.equals("professionalNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, professionalNoteFragment).commit();
            }
        }

        if (travelNote != null){
            if (travelNote.equals("travelNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, travelNoteFragment).commit();
            }
        }

        if (deleteNote != null){
            if (deleteNote.equals("deleteNote")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, deleteNoteFragment).commit();
            }
        }

        if (noteImageList != null){
            if (noteImageList.equals("noteImageList")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, noteImageListFragment).commit();
            }
        }

        if (imageView != null){
            if (imageView.equals("imageView")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, imageViewFragment).commit();
            }
        }

        if (addList != null){
            if (addList.equals("addList")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, addListFragment).commit();
            }
        }

        if (deleteList != null){
            if (deleteList.equals("deleteList")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, deleteListFragment).commit();
            }
        }

        if (showList != null){
            if (showList.equals("showList")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, showListFragment).commit();
            }
        }

        if (editList != null){
            if (editList.equals("editList")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, editListFragment).commit();
            }
        }

    }
}