package it.unimib.bicap.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.utils.Constants;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> listTitles = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }

    @Override
    public int getCount() {
        return listTitles.size();
    }


    public void AddFragment(Fragment fragment, String title, IndaginiHeadList indaginiHeadList){
        listFragment.add(fragment);
        Bundle args = new Bundle();
        args.putParcelable(Constants.INDAGINI_HEAD_LIST_ARG, indaginiHeadList);
        fragment.setArguments(args);
        listTitles.add(title);
    }

    public void AddFragment(Fragment fragment, String title, String message){
        listFragment.add(fragment);
        Bundle args = new Bundle();
        args.putString(Constants.EMPTY_FRAGMENT_MESSAGE, message);
        fragment.setArguments(args);
        listTitles.add(title);
    }

    public void RemoveFragments(int position){
        destroyItem(null, position, listFragment.get(position));
        listFragment.remove(position);
        notifyDataSetChanged();
    }

    public void RemoveAllFragments(){
        for(int i = 0; i< listFragment.size();i++){
            RemoveFragments(listFragment.size()-1);
        }
    }
}
