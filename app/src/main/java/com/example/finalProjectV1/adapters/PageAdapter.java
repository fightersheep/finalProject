package com.example.finalProjectV1.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalProjectV1.fragments.FindTournamentFragment;
import com.example.finalProjectV1.fragments.FriendListFragment;
import com.example.finalProjectV1.fragments.HomeFragment;
import com.example.finalProjectV1.fragments.NotesFragment;

import org.checkerframework.checker.nullness.qual.NonNull;

public class PageAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 4;

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
        else if(position ==2) {
            return new NotesFragment();
        }
        else{
            return  new FindTournamentFragment();
        }

    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
