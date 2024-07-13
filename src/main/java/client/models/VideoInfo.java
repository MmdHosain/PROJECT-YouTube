package client.models;

import org.json.JSONObject;


public class VideoInfo {
    public int id ;
    String title ;
    String title_body ;
    String duration ;
    String creation_time ;
    int total_view ;
    int total_likes ;
    int total_dislikes ;

    int partCount;

    public String getTitle_body() {
        return title_body;
    }

    public String getDuration() {
        return duration;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public int getTotal_view() {
        return total_view;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public int getTotal_dislikes() {
        return total_dislikes;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    int channel_id ;
    String channel_name ;
    public String channel_username ;
    public boolean is_liked;
    public boolean is_disliked;

    public VideoInfo(int video_id, JSONObject response){
        id = video_id;

        title = (String) response.get("title");
        title_body = (String) response.get("title_body");
        creation_time = (String) response.get("creation_time");
        total_view =  response.getInt("total_view");
        total_likes =  response.getInt("total_likes");
        total_dislikes =  response.getInt("total_dislikes");

        channel_id = (int) response.get("channel_id");
        channel_name = (String) response.get("channel_name");
        channel_username = (String) response.get("channel_username");
        is_liked = response.getBoolean("is_liked");
        is_disliked = response.getBoolean("is_disliked");

    }




}
