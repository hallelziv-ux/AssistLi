package com.example.assistli;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new DisFragment();
//            case 1: return new VirusFragment();
//            case 2: return new InstructionsFragment();
//            case 3: return new WinnersFragment();

            default: return new DisFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}


