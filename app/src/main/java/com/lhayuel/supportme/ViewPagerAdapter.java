package com.lhayuel.supportme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    //Constructor
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    //Returns the fragment to display for that page
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    // Returns total number of pages
    public int getCount() {
        return fragmentArrayList.size();
    }

    //Adds Fragments on ViewPager
    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    // Returns the page title for the top indicator
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
