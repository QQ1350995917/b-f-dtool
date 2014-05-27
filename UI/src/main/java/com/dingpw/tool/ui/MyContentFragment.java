package com.dingpw.tool.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @Author:DingPengwei
 * @Email:dingpengwei@goodow.com
 * @DateCrate:May 26, 2014 11:11:32 AM
 * @DateUpdate:May 26, 2014 11:11:32 AM
 * @Des:description
 */
public class MyContentFragment extends Fragment {

  /** * Create a new instance of DetailsFragment, initialized to * show the text at 'index'. */
  public static MyContentFragment newInstance(int index) {
    MyContentFragment f = new MyContentFragment();
    Bundle args = new Bundle();
    args.putInt("index", index);
    f.setArguments(args);
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (container == null) {
      return null;
    }
    ScrollView scroller = new ScrollView(getActivity());
    TextView text = new TextView(getActivity());
    int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getActivity().getResources().getDisplayMetrics());
    text.setPadding(padding, padding, padding, padding);
    StringBuffer buffer = new StringBuffer();
    for(int i=0;i<300;i++){
        buffer.append(this.getResources().getStringArray(R.array.list_item)[getArguments().getInt("index", 0)]);
    }
    text.setText(buffer.toString());
    scroller.addView(text);
    return scroller;
  }

}
