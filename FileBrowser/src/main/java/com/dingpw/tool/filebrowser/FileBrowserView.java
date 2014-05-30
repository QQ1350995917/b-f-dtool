package com.dingpw.tool.filebrowser;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Author:DingPengwei
 * @Email:dingpengwei@goodow.com
 * @DateCrate:May 28, 2014 9:07:31 AM
 * @DateUpdate:May 28, 2014 9:07:31 AM
 * @Des:file browser
 */
public class FileBrowserView extends RelativeLayout {

    private class ListViewAdapter extends BaseAdapter{
        private LayoutInflater inflater = null;
        private File[] files = null;

        public ListViewAdapter(Context context,File[] files) {
            this.inflater = LayoutInflater.from(context);
            this.files = files;
        }

        public void reset(File[] files){
            this.files = files;
        }

        @Override
        public int getCount() {
            if(this.files != null){
                return this.files.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(this.files != null){
                return this.files[position];
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if(convertView == null){
                view = this.inflater.inflate(R.layout.view_file_browser_item,null);
            }else{
                view = convertView;
            }
            ((TextView)view).setText(this.files[position].getName());
            view.setTag(this.files[position]);
            return view;
        }
    }

    private class OnFileItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            File item = (File)view.getTag();
            if(item.isDirectory()){
                currentPath = item.getAbsolutePath();
                listViewAdapter.reset(new File(currentPath).listFiles(defalultFilter));
                listViewAdapter.notifyDataSetChanged();
                //generateLevel(currentPath);
                showLevelPath(currentPath);
            }else{
                defaultFileClickLinsener.onFileClick(item);
            }
        }
    }

    private class OnBackClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            if(!new File(currentPath).getAbsolutePath().equals(new File(root).getAbsolutePath())){
                currentPath = currentPath.substring(currentPath.indexOf(root), currentPath.lastIndexOf("/"));
                if(currentPath.trim().equals("")){
                    currentPath = root;
                }
                listViewAdapter.reset(new File(currentPath).listFiles(defalultFilter));
                listViewAdapter.notifyDataSetChanged();
                //generateLevel(currentPath);
                showLevelPath(currentPath);
            }
        }
    }

    public interface OnFileClickListener{
        public void onFileClick(File file);
    }

    private ListView listView = null;
    private ListViewAdapter listViewAdapter = null;
    private String root = "/";
    private FilenameFilter defalultFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            return true;
        }
    };
    private OnFileClickListener defaultFileClickLinsener = new OnFileClickListener(){
        @Override
        public void onFileClick(File file) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            System.out.println(file.getAbsolutePath());
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri,"*/*");
            FileBrowserView.this.getContext().startActivity(intent);
        }
    };

    private String currentPath = null;
    private LinearLayout ll_level = null;

    public FileBrowserView(Context context) {
        super(context);
        this.initView();
    }

    public FileBrowserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public FileBrowserView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initView();
    }

    public FileBrowserView setRoot(String root){
        if(!new File(root).exists()){
            throw new RuntimeException("the path you provided does not exist on this system");
        }
        this.root = root;
        return this;
    }

    public FileBrowserView setFilenameFilter(FilenameFilter filenameFilter){
        if(filenameFilter != null){
            this.defalultFilter = filenameFilter;
        }
        return this;
    }

    public FileBrowserView setOnFileClickListener(OnFileClickListener onFileClickListener){
        if(onFileClickListener != null){
            this.defaultFileClickLinsener = onFileClickListener;
        }
        return this;
    }

    private void initView() {
        View.inflate(this.getContext(), R.layout.view_file_browser_title, this);
        View.inflate(this.getContext(), R.layout.view_file_browser_list, this);
        this.findViewById(R.id.tv_back).setOnClickListener(new OnBackClickListener());
        this.ll_level = (LinearLayout)this.findViewById(R.id.ll_level);
        LayoutParams titleViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.findViewById(R.id.ll_view_file_browser_title).setLayoutParams(titleViewLayoutParams);
        this.listView = (ListView) this.findViewById(R.id.lv_view_file_browser_list);
        LayoutParams listViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listViewLayoutParams.addRule(RelativeLayout.BELOW, R.id.ll_view_file_browser_title);
        listViewLayoutParams.setMargins(0, 20, 0, 0);
        this.listView.setLayoutParams(listViewLayoutParams);
        this.listView.setOnItemClickListener(new OnFileItemClickListener());
    }

    public FileBrowserView initData(){
        this.currentPath = root;
        this.listViewAdapter = new ListViewAdapter(this.getContext(),new File(root).listFiles(this.defalultFilter));
        this.listView.setAdapter(this.listViewAdapter);
        this.showLevelPath(currentPath);
        return this;
    }

    private void showLevelPath(String currentPath){
        View path = this.ll_level.getChildAt(0);
        if(path == null){
            TextView textView = new TextView(this.getContext());
            textView.setText(currentPath);
            this.ll_level.addView(textView);
        }else{
            ((TextView)this.ll_level.getChildAt(0)).setText(currentPath);
        }
    }

    /**
     * 生成任意路径的点击事件
     * @param currentPath
     */
    private void generateLevel(String currentPath){
        /*
        @TODO: 给没个按钮增加点击事件
         */
        this.ll_level.removeAllViews();
        TextView rootText = new TextView(this.getContext());
        if(new File(root).getAbsolutePath().equals(new File(currentPath).getAbsolutePath())){
            rootText.setText(new File(this.root).getAbsolutePath() + "/");
            this.ll_level.addView(rootText);
            return;
        }
        String substring = currentPath.substring(root.length(), currentPath.length() - 1);
        rootText.setText(new File(this.root).getAbsolutePath() + "/");
        this.ll_level.addView(rootText);

        String[] splits = substring.split("/");
        for(String split : splits){
            TextView button = new TextView(this.getContext());
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setText(split + "/");
            this.ll_level.addView(button);
        }
    }

}
