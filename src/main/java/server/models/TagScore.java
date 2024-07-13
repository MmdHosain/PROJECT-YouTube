package server.models;

import java.util.ArrayList;

public class TagScore {
    public int video_id;
    public int totalScore;
    public ArrayList<tag> tags;


    public TagScore(int video_id,  ArrayList<tag> tags) {
        this.video_id = video_id;
        this.tags = tags;
        totalScore = 0;
    }
}
