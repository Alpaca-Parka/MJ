package com.cookandroid.movie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private Permission permission;
    String mCurrent;
    String mRoot;
    TextView mCurrentTxt;
    ListView mFileList;
    ArrayAdapter mAdapter;
    ArrayList arFiles;

    

    List<String> filesNameList = new ArrayList<>();




    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        permissionCheck();

        mCurrentTxt = (TextView)findViewById(R.id.current);
        mFileList = (ListView)findViewById(R.id.filelist);

        arFiles = new ArrayList();
        //SD카드 루트 가져옴
        //mRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCurrent = mRoot;

        //어댑터를 생성하고 연결해줌
        mAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, arFiles);
        mFileList.setAdapter(mAdapter);//리스트뷰에 어댑터 연결
        mFileList.setOnItemClickListener(mItemClickListener);//리스너 연결

        refreshFiles();
    }


    //리스트뷰 클릭 리스너
    AdapterView.OnItemClickListener mItemClickListener =
            new AdapterView.OnItemClickListener() {

                @SuppressLint("WrongConstant")
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Name = (String)arFiles.get(position);//클릭된 위치의 값을 가져옴

                    //디렉토리이면
                    if(Name.startsWith("[") && Name.endsWith("]")){
                        Name = Name.substring(1, Name.length() - 1);//[]부분을 제거해줌
                    }
                    //들어가기 위해 /와 터치한 파일 명을 붙여줌
                    String Path = mCurrent + "/" + Name;
                    File f = new File(Path);//File 클래스 생성
                    if(f.isDirectory()){//디렉토리면?
                        mCurrent = Path;//현재를 Path로 바꿔줌
                        refreshFiles();//리프레쉬
                    }else{//디렉토리가 아니면 동영상 주소를 세컨드 액티비티에 넘김
                        Intent intent = new Intent(MainActivity.this,VideoActivity.class );
                        intent.putExtra("text",Path);
                        startActivity(intent);
                        refreshFiles();
                    }
                }
            };

    //버튼 2개 클릭시
    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.btnroot://루트로 가기
                if(mCurrent.compareTo(mRoot) != 0){//루트가 아니면 루트로 가기
                    mCurrent = mRoot;
                    refreshFiles();//리프레쉬
                }
                break;
            case R.id.btnup:
                if(mCurrent.compareTo(mRoot) != 0){//루트가 아니면
                    int end = mCurrent.lastIndexOf("/");///가 나오는 마지막 인덱스를 찾고
                    String uppath = mCurrent.substring(0, end);//그부분을 짤라버림 즉 위로가게됨
                    mCurrent = uppath;
                    refreshFiles();//리프레쉬
                }
                break;
        }
    }

    void refreshFiles(){
        mCurrentTxt.setText(mCurrent);//현재 PATH를 가져옴
        arFiles.clear();//배열리스트를 지움
        File current = new File(mCurrent);//현재 경로로 File클래스를 만듬
        String[] files = current.list();//현재 경로의 파일과 폴더 이름을 문자열 배열로 리턴

        //파일이 있다면?
        if(files != null){
            //여기서 출력을 해줌
            for(int i = 0; i < files.length;i++){
                String Path = mCurrent + "/" + files[i];
                String Name = "";

                File f = new File(Path);
                if(f.isDirectory()){
                    Name = "[" + files[i] + "]";//디렉토리면 []를 붙여주고
                }else{
                    Name = files[i];//파일이면 그냥 출력
                }

                arFiles.add(Name);//배열리스트에 추가해줌
            }
        }
        //다끝나면 리스트뷰를 갱신시킴
        mAdapter.notifyDataSetChanged();
    }

    private void permissionCheck(){
        permission = new Permission(this, this);
        if(!permission.checkPermission()){
           permission.requestPermission();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            permission.requestPermission();
        }
    }

}
