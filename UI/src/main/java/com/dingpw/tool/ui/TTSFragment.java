package com.dingpw.tool.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dingpw.tool.tts.TTSView;

/**
 * Created by dpw on 5/30/14.
 */
public class TTSFragment extends Fragment {

    private TTSView ttSview = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.ttSview = new TTSView(this.getActivity());
        return this.ttSview;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.ttSview.onResume();
    }
}
