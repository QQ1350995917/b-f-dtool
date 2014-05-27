package com.dingpw.tool.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Author:DingPengwei
 * @Email:dingpengwei@goodow.com
 * @DateCrate:May 26, 2014 11:11:32 AM
 * @DateUpdate:May 26, 2014 11:11:32 AM
 * @Des:description
 */
public class MyTitleFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_title, container);
  }
}
