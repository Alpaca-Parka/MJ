package com.videoplayer;

public class SampleData {
    public int Thumnail;
    public String VideoTitle;
    public String VideoTime;

    public SampleData(int Thumnail, String VideoTitle, String VideoTime){
        this.Thumnail = Thumnail;
        this.VideoTime = VideoTime;
        this.VideoTitle = VideoTitle;
    }

    public int getThumnail(){
        return this.Thumnail;
    }

    public String getVideoTitle(){
        return this.VideoTitle;
    }

    public String getVideoTime(){
        return this.VideoTime;
    }

}
