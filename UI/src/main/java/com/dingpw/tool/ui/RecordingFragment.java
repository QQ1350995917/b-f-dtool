package com.dingpw.tool.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dingpw.tool.recording.RecordingView;
import com.dingpw.tool.tts.TTSView;

/**
 * Created by dpw on 5/30/14.
 */
public class RecordingFragment extends Fragment {

    private RecordingView recordingView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.recordingView = new RecordingView(this.getActivity());
        return this.recordingView;
    }

}
