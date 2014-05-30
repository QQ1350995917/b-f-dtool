package com.dingpw.tool.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyListFragment extends ListFragment {

  int mCurCheckPosition = 0;
  int mShownCheckPosition = -1;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    this.setListAdapter(new ArrayAdapter<String>(getActivity(),
        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.list_item))); // 使用静态数组填充列表
    if (savedInstanceState != null) {
      mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
      mShownCheckPosition = savedInstanceState.getInt("shownChoice", -1);
    }
    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    showDetails(mCurCheckPosition);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_list, container, false);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    showDetails(position);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("curChoice", mCurCheckPosition);
    outState.putInt("shownChoice", mShownCheckPosition);
  }

  /**
   * 显示listview item 详情
   */
  void showDetails(int index) {
    mCurCheckPosition = index;
    this.getListView().setItemChecked(index, true);
    if (mShownCheckPosition != mCurCheckPosition) {
        Fragment fragment = MyContentFragment.newInstance(index);
        if(index == 3){
            fragment = new FileBorowserFragment();
        }
        if(index == 5){
            fragment = new TTSFragment();
        }
      FragmentTransaction ft = this.getFragmentManager().beginTransaction();
      ft.replace(R.id.fragment_detail, fragment);
      ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
      ft.addToBackStack(null);
      ft.commit();
      mShownCheckPosition = index;
    }
  }

}
