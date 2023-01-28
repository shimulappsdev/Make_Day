package com.example.makeday.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.makeday.fragments.AllTaskFragment;
import com.example.makeday.fragments.DoneTaskFragment;
import com.example.makeday.fragments.HealthFragment;
import com.example.makeday.fragments.OtherFragment;
import com.example.makeday.fragments.PendingTaskFragment;
import com.example.makeday.fragments.PersonalFragment;
import com.example.makeday.fragments.PinTaskFragment;
import com.example.makeday.fragments.ShoppingFragment;
import com.example.makeday.fragments.WorkFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    String[] name = {"All Task", "Done", "Pending", "Pined"};
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllTaskFragment();
            case 1:
                return new DoneTaskFragment();
            case 2:
                return new PendingTaskFragment();
            case 3:
                return new PinTaskFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return name[position];
    }
}
