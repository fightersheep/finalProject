package com.example.finalProjectV1;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;

public class PageAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position ==0) {
            return new HomeFragment();
        }
        else if(position ==1){
            return new FriendListFragment();
        }
        else {
            return new NotesFragment();
        }

    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
