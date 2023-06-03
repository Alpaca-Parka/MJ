package com.cookandroid.movie.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.movie.R;
import com.cookandroid.movie.VideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileManager {
    private final AppCompatActivity activity;
    private final String mRoot;
    private String mCurrent;
    private String searchText = "";
    private final TextView mCurrentTxt;
    private final ArrayAdapter<String> mAdapter;
    private final ArrayList<String> sortedFiles;
    private final ArrayList<View> selectedViews;
    private final List<String> selectedFiles = new ArrayList<>();
    private boolean isSort = false;
    private boolean isChecked = false;
    private final Comparator<String> caseInsensitiveComparator = String.CASE_INSENSITIVE_ORDER;
    private final Comparator<String> reverseComparator = Collections.reverseOrder(caseInsensitiveComparator);


    /** 생성자
     * 클릭 리스너
     * 롱 클릭 리스너
     * */
    public FileManager(AppCompatActivity _activity, Context _context) {
        this.activity = _activity;

        mCurrentTxt = (TextView) activity.findViewById(R.id.current);
        ListView mFileList = (ListView) activity.findViewById(R.id.filelist);

        sortedFiles = new ArrayList<>();
        selectedViews = new ArrayList<>();
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCurrent = mRoot;

        mAdapter = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, sortedFiles);
        mFileList.setAdapter(mAdapter);


        AdapterView.OnItemClickListener mItemClickListener = (parent, view, position, id) -> {
            String name = (String) sortedFiles.get(position);//현재 디렉토리의 위치를 가져옴

            if (name.startsWith("[") && name.endsWith("]")) {
                name = name.substring(1, name.length() - 1);
            }

            String Path = mCurrent + "/" + name;
            File f = new File(Path);
            if (f.isDirectory()) {
                //디렉토리일 경우 동작
                mCurrent = Path;
                refreshFiles();
            } else {
                //파일일 경우 동작
                String extension = getFileExtension(name);
                if(extension != null && isSupportedExtension(extension)){
                    //실행할 수 있는 확장자일 경우 동작
                    Intent intent = new Intent(activity, VideoActivity.class);
                    intent.putExtra("text", Path);
                    activity.startActivity(intent);
                    refreshFiles();
                }else{
                    //실행할 수 없는 확장자일 경우 동작
                    Toast.makeText(activity, "실행할 수 있는 파일이 아닙니다.", Toast.LENGTH_SHORT).show();
                }


            }
        };
        mFileList.setOnItemClickListener(mItemClickListener);
        // 롱 클릭 리스너 추가
        AdapterView.OnItemLongClickListener mItemLongClickListener = (parent, view, position, id) -> {
            String name = sortedFiles.get(position);
            String filePath = mCurrent + "/" + name;
            toggleFileSelection(filePath, view); // 파일 선택 토글
            mAdapter.notifyDataSetChanged();
            return true;
        };
        mFileList.setOnItemLongClickListener(mItemLongClickListener);

    }

    /**파일매니저 기본처리 메서드*/
    public void refreshFiles() {
        mCurrentTxt.setText(mCurrent);
        sortedFiles.clear();
        selectedFiles.clear();
        clearViews();
        isSort = false;//초기화 해야 처음에 이동하면 순차정렬



        File current = new File(mCurrent);
        String[] files = current.list();

        if (files != null) {
            for (String file : files) {
                String Path = mCurrent + "/" + file;
                String name;

                File f = new File(Path);
                if (f.isDirectory()) {
                    name = "[" + file + "]";
                } else {
                    name = file;
                }
//
                if (searchText.isEmpty() || name.toLowerCase().contains(searchText)) {
                    // 검색어가 비어있거나 파일 이름이 검색어를 포함하면 추가
                    sortedFiles.add(name);
                }
            }
        }
        isSorted();//정렬확인 + mAdapter.notifyDataSetChanged()

    }
    /** 루트 디렉토리로 이동 */
    public void upRoot() {
        //루트로 이동
        if (mCurrent.compareTo(mRoot) != 0) {
            mCurrent = mRoot;
            refreshFiles();
        }
    }
    /** 상위 디렉토리로 이동 */
    public void updir() {
        //상위 디렉토리로 이동
        if (mCurrent.compareTo(mRoot) != 0) {
            int end = mCurrent.lastIndexOf("/");
            mCurrent = mCurrent.substring(0, end);
            refreshFiles();
        }
    }
    /** 순차정렬, 역순정렬 확인*/
    public void isSorted() {
        //정렬이 되었는지 안되었는지 판단해서 동작

        if (isSort) {
            //순차정렬 -> 역순정렬
            if (isChecked) {
                //체크하면 폴더 우선 정렬
                sortedFiles.sort((str1, str2) -> {
                    String lowerStr1 = str1.toLowerCase();
                    String lowerStr2 = str2.toLowerCase();

                    if (lowerStr1.startsWith("[") && !lowerStr2.startsWith("[")) {
                        return -1;
                    } else if (!lowerStr1.startsWith("[") && lowerStr2.startsWith("[")) {
                        return 1;
                    } else {
                        return lowerStr1.compareToIgnoreCase(lowerStr2);
                    }
                });
                Collections.reverse(sortedFiles);
            } else {
                //체크를 안하면 기본정렬
                sortedFiles.sort(reverseComparator);
            }
            isSort = false;
        } else {
//            역순정렬 -> 순차정렬
            if (isChecked) {
                //체크하면 폴더 우선 정렬
                sortedFiles.sort((str1, str2) -> {
                    String lowerStr1 = str1.toLowerCase();
                    String lowerStr2 = str2.toLowerCase();

                    if (lowerStr1.startsWith("[") && !lowerStr2.startsWith("[")) {
                        return -1;
                    } else if (!lowerStr1.startsWith("[") && lowerStr2.startsWith("[")) {
                        return 1;
                    } else {
                        return lowerStr1.compareToIgnoreCase(lowerStr2);
                    }
                });
            } else {
                //체크를 안하면 기본정렬
                sortedFiles.sort(caseInsensitiveComparator);
            }
            isSort = true;
        }
        mAdapter.notifyDataSetChanged();
    }
    /**파일 삭제 메서드*/
    public void deleteSelectedFiles() {
        for (String filePath : selectedFiles) {
            File file = new File(filePath);
            if(file.delete()){
                Toast.makeText(activity, "선택된 파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        refreshFiles();
    }
    /** selectedFiles가 비어있는지 반환하는 메서드 */
    public boolean areFilesSelected() {
        return !selectedFiles.isEmpty();
    }
    /** 롱클릭 선택유무 메서드 */
    private void toggleFileSelection(String filePath, View view) {
        if (selectedFiles.contains(filePath)) {
            selectedFiles.remove(filePath);
            selectedViews.remove(view);
            view.setBackgroundColor(Color.WHITE);
        } else {
            selectedFiles.add(filePath);
            selectedViews.add(view);
            view.setBackgroundColor(Color.LTGRAY);
        }

        mAdapter.notifyDataSetChanged();
    }
    /** 선택된 view항목들 배경색 초기화 메서드 */
    private void clearViews(){
        if(!selectedViews.isEmpty()){
            for(View view : selectedViews){
                view.setBackgroundColor(Color.WHITE);
            }
            selectedViews.clear();
        }
    }
    /** 확장자 추출 메서드 */
    private String getFileExtension(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex >= 0 && dotIndex < fileName.length() -1 ){
            return fileName.substring(dotIndex + 1);
        }
        return null;
    }
    /** 확장자 비교 메서드*/
    private boolean isSupportedExtension(String extension){
        String[] supportedExtensions = { "wmv", "flv", "mp4", "mkv", "3gp" };
        for (String ext : supportedExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }



    /**
     * getter
     * setter
     * 항목들
     * */
    public String getmCurrent() {
        return mCurrent;
    }
    public String getmRoot() {
        return mRoot;
    }
    public List<String> getSelectedFiles() {
        return selectedFiles;
    }
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}