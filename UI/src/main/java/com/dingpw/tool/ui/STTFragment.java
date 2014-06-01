package com.dingpw.tool.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dingpw.tool.stt.STTView;

/**
 * Created by dpw on 5/30/14.
 */
public class STTFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new STTView(this.getActivity());
    }

}
