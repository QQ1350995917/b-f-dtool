package com.dingpw.tool.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dingpw.tool.filebrowser.FileBrowserView;
/**
 * Created by dingpw on 5/29/14.
 */
public class FileBorowserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new FileBrowserView(this.getActivity()).initData();
    }
}
