package client.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import static client.models.Main.*;


public class Request {
    //account requests--------------------------------------------------------------------------------------

    public void login(String username_input, String password_input){
        JSONObject request = new JSONObject();
        request.put("requestType","/login");
        request.put("username_input",username_input);
        request.put("password_input",password_input);
//        request.put("name_input",name_input);
//        request.put("email_input",email_input);
//        request.put("number_input",number_input);
        request.put("name_input","example_name");
        request.put("email_input","example_email");
        request.put("number_input","example_number");

        write(request);
    }
    public void signUp(String username_input,String password_input,String name_input,String email_input,String number_input){
        JSONObject request = new JSONObject();
        request.put("requestType","/signUp");
        request.put("username_input",username_input);
        request.put("password_input",password_input);
        request.put("name_input",name_input);
        request.put("email_input",email_input);
        request.put("number_input",number_input);

        write(request);
    }
    public void logout(String username_input,String password_input){
        JSONObject request = new JSONObject();
        request.put("requestType","/logout");
        request.put("username",username_input);
        request.put("password",password_input);

        write(request);
    }
    public void authorize(String username, String password){
        JSONObject request = new JSONObject();
        request.put("requestType","/authorize");
        request.put("username",username);
        request.put("password",password);

        write(request);
    }
    public void profileImg() {
        JSONObject request = new JSONObject();
        request.put("requestType","/profileImg");
        write(request);
    }
    public void channelProfileImg(String channel_username) {
        JSONObject request = new JSONObject();
        request.put("requestType","/channelProfileImg");
        request.put("channel_username",channel_username);
        write(request);
    }
    //-------------------------------------------------------------------------------------------------------

    // item list request-------------------------------------------------------------------------------------
    public void ChannelVideoList(String channel_username){
        JSONObject request = new JSONObject();
        request.put("requestType","/ChannelVideoList");
        request.put("channel_username",channel_username);


        write(request);
    }
    public void videoList(int videoCount, JSONArray tags){
        JSONObject request = new JSONObject();
        request.put("requestType","/videoList");
        request.put("count",videoCount);
        request.put("tags",tags);

        write(request);
    }
    static void channelList(String channelCount, JSONArray tags){
        JSONObject request = new JSONObject();
        request.put("requestType","/channelList");
        request.put("count",channelCount);
        request.put("tags",tags);

        write(request);
    }
    static void hybridList(String itemCount, JSONArray tags){
        //channel and video list combined
        JSONObject request = new JSONObject();
        request.put("requestType","/hybridList");
        request.put("count",itemCount);
        request.put("tags",tags);

        write(request);
    }
    public void commentList(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/commentList");
        request.put("video_id",video_id);


        write(request);
    }

    //-------------------------------------------------------------------------------------------------------
    // item request------------------------------------------------------------------------------------------
    public void video(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/video");
        request.put("video_id",video_id);

        write(request);
    }
    public void videoFile(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/videoFile");
        request.put("video_id",video_id);

        write(request);
    }
    public void imageFile(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/imageFile");
        request.put("video_id",video_id);

        write(request);
    }
    public void comment(int comment_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/comment");
        request.put("comment_id",comment_id);


        write(request);
    }
    public void channel(String channel_username){
        JSONObject request = new JSONObject();
        request.put("requestType","/channel");
        request.put("channel_username",channel_username);


        write(request);
    }
    public void getChannelUsername(String username) {

        JSONObject request = new JSONObject();
        request.put("requestType","/getChannelUsername");

        write(request);

    }

    //------------------------------------------------------------------------------------------------------
    // submit requests -------------------------------------------------------------------------------------
    public void createChannel(String channelName,String channelUsername ,JSONArray tags){

        JSONObject request = new JSONObject();
        request.put("requestType","/createChannel");
        request.put("channelName",channelName);
        request.put("channelUsername",channelUsername);
        request.put("channelDescription",channelUsername);
        request.put("tags",tags);


        write(request);
    }
    public void deleteChannel(int channel_id){

        JSONObject request = new JSONObject();
        request.put("requestType","/deleteChannel");
        request.put("channel_id",channel_id);


        write(request);
    }
    public void addVideo(String channel_username, String videoName, String videoCaption, JSONArray tags, File fileToUpload, String chosenPlaylist){

        JSONObject request = new JSONObject();
        request.put("requestType","/addVideo");
        request.put("channel_username",channel_username);
        request.put("videoName",videoName);
        request.put("videoDescription",videoCaption);
        request.put("tags",tags);
        request.put("playlist",chosenPlaylist);


        write(request);
        JSONObject response = read();

        if (response.getString("responseType").equals("/addVideo_accepted")){
            uploadFile(fileToUpload);
            System.out.println("upload successful");
        }
        else {
            System.out.println("upload unsuccessful");
        }
    }

    public void removeVideo(int video_id){

        JSONObject request = new JSONObject();
        request.put("requestType","/removeVideo");
        request.put("video_id",video_id);



        write(request);
    }
    public void addComment(int video_id,String text ,int repliedTo_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/addComment");

        request.put("video_id",video_id);
        request.put("text",text);
        request.put("repliedTo_id",repliedTo_id);

        write(request);
    }
    public void removeComment(int comment_id,int repliedTo_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/removeComment");
        request.put("comment_id",comment_id);
        request.put("repliedTo_id",repliedTo_id);

        write(request);
    }
    public void createPlaylist(String title,String description,String channel_username){
        JSONObject request = new JSONObject();
        request.put("requestType","/createPlaylist");
        request.put("title",title);
        request.put("description",description);
        request.put("channel_username",channel_username);

        write(request);
    }
    public void getPlaylistList(String channel_username){
        JSONObject request = new JSONObject();
        request.put("requestType","/getPlaylistList");
        request.put("channel_username",channel_username);

        write(request);
    }
    //------------------------------------------------------------------------------------------------------
    // tiny action request----------------------------------------------------------------------------------
    public void subscribeChannel(String channel_username){

        JSONObject request = new JSONObject();
        request.put("requestType","/subscribeChannel");
        request.put("channel_username",channel_username);


        write(request);
    }
    public void unsubscribeChannel(String channel_username){

        JSONObject request = new JSONObject();
        request.put("requestType","/unsubscribeChannel");
        request.put("channel_username",channel_username);

        write(request);
    }
    public void add_WatchedVideo(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/add_WatchedVideo");
        request.put("video_id",video_id);

        write(request);
    }
    public void add_WatchLaterVideo(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/add_WatchLaterVideo");
        request.put("video_id",video_id);

        write(request);
    }
    static void remove_WatchedVideo(String video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/remove_WatchedVideo");
        request.put("video_id",video_id);


        write(request);
    }
    static void add_WatchLaterVideo(String video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/add_WatchLaterVideo");
        request.put("video_id",video_id);

        write(request);
    }
    static void remove_WatchLaterVideo(String video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/remove_WatchLaterVideo");
        request.put("video_id",video_id);


        write(request);
    }
    public void likeVideo(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/likeVideo");
        request.put("video_id",video_id);


        write(request);
    }
    public void remove_likedVideo(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/remove_likedVideo");
        request.put("video_id",video_id);


        write(request);
    }
    public void dislikeVideo(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/dislikeVideo");
        request.put("video_id",video_id);

        write(request);
    }
    public void remove_dislikedVideo(int video_id){
        JSONObject request = new JSONObject();
        request.put("requestType","/remove_dislikedVideo");
        request.put("video_id",video_id);

        write(request);
    }

    static void edit_commentLike(String comment_id,String editType){
        JSONObject request = new JSONObject();

        request.put("requestType","/edit_commentLike");
        request.put("editType","/edit_commentLike"); //edit type: /addLike /removeLike /addDislike /removeDislike
        request.put("comment_id",comment_id);


        write(request);
    }

    public void search(String text) {
        JSONObject request = new JSONObject();

        request.put("requestType","/search");
        request.put("text",text);

        write(request);
    }

    public void historyVideoList() {
        JSONObject request = new JSONObject();
        request.put("requestType","/historyVideoList");


        write(request);
    }

    public void watchLaterVideoList() {
        JSONObject request = new JSONObject();
        request.put("requestType","/watchLaterVideoList");


        write(request);
    }
    public void likedVideoList() {
        JSONObject request = new JSONObject();
        request.put("requestType","/likedVideoList");


        write(request);
    }

    //-----------------------------------------------------------------------------------------------------

}