package server.models;

import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONObject;
import server.database.Dbm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;


public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private static BufferedWriter writer;

    private int user_id;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        } catch (IOException e) {
            closeClientHandler(socket, reader, writer);
        }
    }

    public static void write(JSONObject response) {


        try {
            writer.write(response.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override

    public void run() {
        try {
            requestHandler();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private void requestHandler() throws URISyntaxException {
        while (socket.isConnected()) {
            JSONObject request = new JSONObject();
            try {
                String clientRequest = reader.readLine();
                request = new JSONObject(clientRequest);

            } catch (IOException e) {
                e.printStackTrace();
            }


            String requestType = (String) request.get("requestType");
            System.out.println(requestType);
            switch (requestType) {
                case "/login" -> login(request);
                case "/logout" -> logout(request);
                case "/signUp" -> signUp(request);
                case "/getChannelUsername" -> getChannelUsername(request);
                case "/profileImg" -> profileImg(request);
                case "/channelProfileImg" -> channelProfileImg(request);
                case "/videoList" -> videoList(request);
                case "/ChannelVideoList" -> ChannelVideoList(request);
                case "/channelList" -> channelList(request);
                case "/hybridList" -> hybridList(request);
                case "/commentList" -> commentList(request);
                case "/video" -> video(request);
                case "/videoFile" -> videoFile(request);
                case "/imageFile" -> imageFile(request);
                case "/comment" -> comment(request);
                case "/channel" -> channel(request);
                case "/createChannel" -> createChannel(request);
                case "/deleteChannel" -> deleteChannel(request);
                case "/addVideo" -> addVideo(request);
                case "/removeVideo" -> removeVideo(request);
                case "/addComment" -> addComment(request);
                case "/removeComment" -> removeComment(request);
                case "/subscribeChannel" -> subscribeChannel(request);
                case "/unsubscribeChannel" -> unsubscribeChannel(request);
                case "/add_WatchedVideo"       -> add_WatchedVideo(request);
                case "/add_WatchLaterVideo"       -> add_WatchLaterVideo(request);
                case "/watchLaterVideoList"       -> watchLaterVideoList(request);
//                case "/remove_WatchedVideo"    -> remove_WatchedVideo(request);
//                case "/add_WatchLaterVideo"    -> add_WatchLaterVideo(request);
//                case "/remove_WatchLaterVideo" -> remove_WatchLaterVideo(request);
                case "/likeVideo"         -> likeVideo(request);
                case "/remove_likedVideo"      -> remove_likedVideo(request);
                case "/dislikeVideo"      -> dislikeVideo(request);
                case "/remove_dislikedVideo"   -> remove_dislikedVideo(request);
                case "/edit_commentLike" -> edit_commentLike(request);
                case "/search" -> search(request);
                case "/createPlaylist" -> createPlaylist(request);
                case "/getPlaylistList" -> getPlaylistList(request);
                case "/historyVideoList" -> historyVideoList(request);
                case "/likedVideoList" -> likedVideoList(request);


            }
        }
    }

    private void add_WatchLaterVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id = request.getInt("video_id");
        Dbm.WatchLaterVideo(video_id,user_id);

        write(response);
    }

    private void add_WatchedVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id = request.getInt("video_id");
        Dbm.addView(video_id,user_id);

        write(response);
    }

    private void likedVideoList(JSONObject request) {
        JSONObject response = new JSONObject();

        JSONArray videoIdList = new JSONArray();

        response.put("responseType", "/likedVideoList_accepted");


        List<Integer> channelVideoList = Dbm.likeVideoList(user_id);
        assert channelVideoList != null;
        for (Integer video_id : channelVideoList) {

            videoIdList.put(video_id);

        }
        response.put("videoIdList", videoIdList);


        write(response);
    }

    private void watchLaterVideoList(JSONObject request) {
        JSONObject response = new JSONObject();

        JSONArray videoIdList = new JSONArray();

        response.put("responseType", "/watchLaterVideoList_accepted");


        List<Integer> channelVideoList = Dbm.watchLaterVideoList(user_id);
        assert channelVideoList != null;
        for (Integer video_id : channelVideoList) {

            videoIdList.put(video_id);

        }
        response.put("videoIdList", videoIdList);


        write(response);
    }

    private void historyVideoList(JSONObject request) {
        JSONObject response = new JSONObject();

        JSONArray videoIdList = new JSONArray();

        response.put("responseType", "/historyVideoList_accepted");


        List<Integer> channelVideoList = Dbm.getHistoryVideoList(user_id);
        assert channelVideoList != null;
        for (Integer video_id : channelVideoList) {

            videoIdList.put(video_id);

        }
        response.put("videoIdList", videoIdList);


        write(response);
    }

    private void getPlaylistList(JSONObject request) {
        JSONObject response = new JSONObject();
        int channel_id = Dbm.getChannel_id(request.getString("channel_username"));
        response.put("playlistList",Dbm.getPlaylistList(channel_id));
        write(response);
    }

    private void createPlaylist(JSONObject request) {
        JSONObject response = new JSONObject();
        String title = request.getString("title");
        String description = request.getString("description");
        String channel_username = request.getString("channel_username");
        int playlist_id = Dbm.addPlaylist(title,description,channel_username);
        response.put("responseType","/createPlaylist_accepted");
        response.put("playlist_id",playlist_id);
        response.put("title",title);

        write(response);
    }

    private void search(JSONObject request) {
        JSONObject response ;

        response =  Dbm.searchBarList(request.getString("text"));

        write(response);
    }


    private void channelProfileImg(JSONObject request) {

        JSONObject response = new JSONObject();

        String channel_username = request.getString("channel_username");
        int channel_id = Dbm.getChannel_id(channel_username);

        response.put("responseType", "/profileImg_accepted");

        File fileToSend = new File("src/main/resources/DATA/channelProfileImage/"+ channel_id+".jpg");

        write(response);
        sendFile(fileToSend);
    }

    private void getChannelUsername(JSONObject request) {

        JSONObject response = new JSONObject();


//        user_id = 1;
        String channelUsername = Dbm.getChannelUsername(user_id);
//        String channelUsername = "demo";
        response.put("channel_username",channelUsername);

        write(response);

    }

    private void ChannelVideoList(JSONObject request) {

        JSONObject response = new JSONObject();
        String channel_username = request.getString("channel_username");

        JSONArray videoIdList = new JSONArray();

        response.put("responseType", "/ChannelVideoList_accepted");

        int channel_id = Dbm.getChannel_id(channel_username);
//        int channel_id = 1;

        List<Integer> channelVideoList = Dbm.getChannelVideoList(channel_id);
        assert channelVideoList != null;
        for (Integer video_id : channelVideoList) {

            videoIdList.put(video_id);

        }
        response.put("videoIdList", videoIdList);


        write(response);

    }

    private void login(JSONObject request) {
        JSONObject response = new JSONObject();
        String username_input = (String) request.get("username_input");
        String password_input = (String) request.get("password_input");
        try {

            if (Dbm.checkUsername(username_input) && Dbm.authorize(username_input,password_input))
            {

                user_id = Dbm.get_user_id(username_input);
//            user_id = 1;

                response.put("responseType", "/login_accepted");
                response.put("username", username_input);
                response.put("password", password_input);


            }
            else{
                response.put("responseType","/login_rejected");
            }

            write(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout(JSONObject request) {
        JSONObject response = new JSONObject();
        response.put("responseType", "/logout_accepted");
        user_id = -1;
        write(response);
    }

    private void signUp(JSONObject request) {

        JSONObject response = new JSONObject();

        String username_input = (String) request.get("username_input");
        String password_input = (String) request.get("password_input");
        String name_input = (String) request.get("name_input");
        String email_input = (String) request.get("email_input");
        String number_input = (String) request.get("number_input");

        if (Dbm.checkUsername(username_input)){
            response.put("responseType","/signUp_rejected");
        }
        else
        {
            response.put("responseType", "/signUp_accepted");
            response.put("username", username_input);
            response.put("password", password_input);
            Dbm.addUser(username_input,password_input,name_input,email_input,number_input,false,"");
        }
        write(response);
    }

    private void profileImg(JSONObject request){

        JSONObject response = new JSONObject();
        response.put("responseType", "/profileImg_accepted");

        File fileToSend = new File("src/main/resources/DATA/profileImage/"+ user_id+".jpg");

        write(response);
        sendFile(fileToSend);
    }


    private void videoList(JSONObject request) {

        JSONObject response = new JSONObject();

        int videoCount = request.getInt("count");

        JSONArray tags = (JSONArray) request.get("tags");

        // todo recommendation

        JSONArray videoIdList = new JSONArray();
//        List<Integer> idList = Dbm.getRandomVideoId(videoCount);  //todo temporary
//
//        for (Integer id : idList) {
//            videoIdList.put(id);
//        }
        ArrayList<Integer> subscribedChannels = Dbm.getSubscribedChannels(user_id);

        for (int i = 1; i <= videoCount/3; i++) {
            int num = Dbm.getRandomNumber(0,subscribedChannels.size());
            int channel_id = subscribedChannels.get(num);
            List<Integer> channelVideos = Dbm.getChannelVideoList(channel_id);

            int video_id = channelVideos.get(Dbm.getRandomNumber(0,channelVideos.size()));

            videoIdList.put(video_id);
        }
        ArrayList<Integer> recommendedVideos = Dbm.getRecommendedVideos(user_id);


        for (int i = videoCount/3 +1; i <= videoCount; i++) {
            int num = Dbm.getRandomNumber(videoCount/3 + 1,videoCount);
            int num2 = Dbm.getRandomNumber(1,num+1);
            if(num2 == num){
                videoIdList.put(recommendedVideos.get(num));
            }
            else i--;
        }



        response.put("responseType", "/videoList_accepted");

        response.put("videoIdList", videoIdList);


        write(response);
    }

    private void channelList(JSONObject request) {

        JSONObject response = new JSONObject();

        int channelCount = (int) request.get("count");
        JSONArray tags = (JSONArray) request.get("tags");



        JSONArray channelIdList = new JSONArray();
        List<Integer> idList = Dbm.getRandomChannelId(channelCount);  //todo temporary

        for (Integer id : idList) {
            channelIdList.put(id);
        }
        response.put("responseType", "/videoList_accepted");
        response.put("channelIdList", channelIdList);


        write(response);
    }

    private void hybridList(JSONObject request) {

        JSONObject response = new JSONObject();

        int itemCount = (int) request.get("count");
        JSONArray tags = (JSONArray) request.get("tags");

        // todo recommendation

        JSONArray itemIdList = new JSONArray();
        List<Integer> idList = Dbm.getRandomChannelId(itemCount);  //todo temporary

        for (Integer id : idList) {
            itemIdList.put(id);
        }
        response.put("responseType", "/videoList_accepted");
        response.put("itemIdList", itemIdList);


        write(response);
    }

    private void commentList(JSONObject request) {
        JSONObject response = new JSONObject();
        int video_id = (int) request.get("video_id");
        JSONArray commentIdList = new JSONArray();
        int commentCount = Dbm.getCommentCount(video_id);
        List<Integer> idList = Dbm.getCommentIdList(video_id);

        for (Integer id : idList) {
            commentIdList.put(id);
        }
        response.put("responseType", "/commentList_accepted");
        response.put("commentIdList", commentIdList);
        response.put("commentCount", commentCount);


        write(response);
    }

    private void video(JSONObject request) throws URISyntaxException {

        JSONObject response = new JSONObject();

        int id = (int) request.get("video_id");




//        String title = "habibPoor";
//        String title_body = "cs is good";
//        String duration = "60";
//        String creation_time = "tomorrow";
//        String total_view = "1k";
//        String total_likes = "7";
//        String total_dislikes = "993";
//        int channel_id = 1;
//        String channel_name = "channel o  the basterds";


        String title = Dbm.getVideo_Title(id);
        String title_body = Dbm.getVideo_description(id);
        String duration = Dbm.getVideo_duration(id);
        String creation_time = Dbm.getVideo_creationTime(id);
        int total_view = Dbm.getVideo_totalView(id);
        int total_likes = Dbm.getVideo_totalLikes(id);
        int total_dislikes = Dbm.getVideo_totalDislikes(id);
        boolean is_liked = Dbm.is_liked(id,user_id);
        boolean is_disliked = Dbm.is_disliked(id,user_id);

        int channel_id = Dbm.getchannel_id(id);
        String channel_name = Dbm.getchannel_name(channel_id);
        String channel_username = Dbm.getChannelUsername(channel_id);
        response.put("responseType", "/video_accepted");
        response.put("title", title);
        response.put("title_body", title_body);
        response.put("duration", duration);
        response.put("creation_time", creation_time);
        response.put("total_view", total_view);
        response.put("total_likes", total_likes);
        response.put("total_dislikes", total_dislikes);

        response.put("channel_id", channel_id);
        response.put("channel_name", channel_name);
        response.put("channel_username", channel_username);
        response.put("is_liked", is_liked);
        response.put("is_disliked", is_disliked);


        write(response);


    }

    private void videoFile(JSONObject request) {
        int video_id = (int) request.get("video_id");

        String videoLink = Dbm.getVideo_link(video_id);



//        String videoLink = "src/main/resources/DATA/video_examples/1.mp4";
//        String videoLink = "src/main/resources/server/video_examples/1.mkv";


        File fileToSend = new File(videoLink);
        sendFile(fileToSend);
    }

    private void imageFile(JSONObject request) throws URISyntaxException {
        int video_id = (int) request.get("video_id");
//        String imageLink = Dbm.getImage_link(video_id);
//        String imageLink = "D:\\Fainal_Project\\ROJECT-YouTube\\src\\main\\resources\\server\\image_examples\\" + video_id + ".jpg";
//        File fileToSend = new File(imageLink);

//        File fileToSend = new File(Objects.requireNonNull(getClass().getResource("../../server/image_examples/"        + video_id + ".jpg")).toURI());
        File fileToSend = new File(Objects.requireNonNull(getClass().getResource("../../DATA/image_examples/lowSize/lowSize" + video_id + ".jpg")).toURI());

//        fileToSend = resize(fileToSend,video_id);
        sendFile(fileToSend);
    }

    private void comment(JSONObject request) {
        JSONObject response = new JSONObject();
        int comment_id = (int) request.get("comment_id");
        String senderName = Dbm.getUsername(comment_id);
        String Text = Dbm.getCommentText(comment_id);
        int repliedTo = Dbm.getcommentRepliedTo(comment_id);
        String creationTime = Dbm.getcommentCreationTime(comment_id);

        response.put("responseType", "/comment_accepted");
        response.put("senderName", senderName);
        response.put("text", Text);
        response.put("repliedTo", repliedTo);

        if(creationTime == null){creationTime = "";}
        response.put("creationTime", creationTime);

        write(response);
    }

    private void channel(JSONObject request) {
        JSONObject response = new JSONObject();
        String channel_username =  request.getString("channel_username");
        int channel_id = Dbm.getChannel_id(channel_username);
//        int channel_id = 1;
        int totalVideos = Dbm.getChannelTotalVideoes(channel_id);
        int totalViews = Dbm.getChannelTotalViews(channel_id);
        int totalSubscribers = Dbm.getChannelTotalSubscribers(channel_id);
        String description = Dbm.getChannelDescription(channel_id);
        String title = Dbm.getchannel_name(channel_id);
        boolean is_subscribed = Dbm.is_subscribed(channel_id,user_id);
        response.put("responseType", "/channel_accepted");
        response.put("totalVideos", totalVideos);
        response.put("channel_username", channel_username);
        response.put("totalViews", totalViews);
        response.put("totalSubscribers", totalSubscribers);
        response.put("channelTitle", title);
        response.put("channelDescription", description);
        response.put("is_subscribed", is_subscribed);



        write(response);
    }

    private void createChannel(JSONObject request) {
        JSONObject response = new JSONObject();

        String channelName = (String) request.get("channelName");
        String channelUsername = (String) request.get("channelUsername");
        String channelDescription = (String) request.get("channelDescription");
        int owner_id = (int) request.get("channelDescription");
        JSONArray tags = (JSONArray) request.get("tags");
        String creationDate = "";

        if (!Dbm.checkChannelUsername(channelUsername)) {
            response.put("responseType", "/createChannel_rejected");
        } else {
            response.put("responseType", "/createChannel_accepted");
            Dbm.addChannel(channelName, channelUsername, owner_id, creationDate, channelDescription);
        }
        write(response);


    }

    private void deleteChannel(JSONObject request) {
        JSONObject response = new JSONObject();

        String username_input = (String) request.get("username_input");
        String password_input = (String) request.get("password_input");

        if (Dbm.authorize(username_input, password_input)) {

            int channel_id = (int) request.get("channel_id");


            List<Integer> channelVideoList = Dbm.getChannelVideoList(channel_id);
            for (Integer video_id : channelVideoList) {
                List<Integer> videoCommentList = Dbm.getVideoCommentList(video_id);
                for (Integer comment_id : videoCommentList) {
                    Dbm.removeComment(comment_id);
                }
                Dbm.removeVideo(video_id);
            }
            Dbm.removeChannel(channel_id);

            response.put("responseType", "/deleteChannel_accepted");
        } else {
            response.put("responseType", "/deleteChannel_accepted");
        }
        write(response);
    }

    private void addVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        String channel_username =  request.getString("channel_username");
        String videoName = (String) request.get("videoName");
        String videoDescription = (String) request.get("videoDescription");
        JSONArray tags = (JSONArray) request.get("tags");
        int channel_id = Dbm.getChannel_id(channel_username);
//        int channel_id = 1;
        int video_id = Dbm.addVideo(channel_id, videoName, videoDescription, "", tags);
//        int video_id =  5001;
        response.put("responseType", "/addVideo_accepted");
        write(response);

        if (response.getString("responseType").equals("/addVideo_accepted")){
            byte[] fileBytes = getUploadedFile();
            saveFile(fileBytes,"src/main/resources/DATA/video_examples/"+ 39 + ".mp4");
        }
        if (response.getString("responseType").equals("/addVideo_accepted")){
            Dbm.addVideotoChannel(video_id,channel_id);
        }

        if (!response.getString("playlist").isEmpty()){
            Dbm.addVideotoPlaylist(video_id,response.getString("playlist"));
        }
        for (int i = 0; i < tags.length(); i++) {
            Dbm.addTag(video_id,"video",tags.getString(i),10);
        }
        Dbm.addTag(video_id,"video","channel_"+ channel_username,10);


    }

    public static void saveFile(byte[] bytes,String path){

        try {

            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private void uploadVideoFile(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id = (int) request.get("video_id");
        try {

            File file = new File(Dbm.getVideoPath());

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            byte[] bytes = new byte[1024];
            dataIn.read(bytes);
            fileOutputStream.write(bytes);

            dataIn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        response.put("responseType", "/uploadVideoFile_accepted");

        write(response);
    }

    private void removeVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id = (int) request.get("video_id");

        List<Integer> videoCommentList = Dbm.getVideoCommentList(video_id);
        for (Integer comment_id : videoCommentList) {
            Dbm.removeComment(comment_id);
        }
        Dbm.removeVideo(video_id);
        response.put("responseType", "/removeVideo_accepted");

    }

    private void addComment(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id = (int) request.get("video_id");
        String text = (String) request.get("text");
        int repliedTo_id = (int) request.get("repliedTo_id");


        Dbm.addComment(video_id, user_id, text, repliedTo_id, String.valueOf(LocalDateTime.now()));
        response.put("responseType", "/addComment_accepted");


    }

    private void removeComment(JSONObject request) {
        JSONObject response = new JSONObject();

        int comment_id = (int) request.get("comment_id");

        Dbm.removeComment(comment_id);
        response.put("responseType", "/removeComment_accepted");

    }

    private void subscribeChannel(JSONObject request) {
        JSONObject response = new JSONObject();

        String channel_username =  request.getString("channel_username");
        int channel_id = Dbm.getChannel_id(channel_username);

        if (!Dbm.is_subscribed(channel_id,user_id)){
            String add_date = String.valueOf(LocalDateTime.now());
            Dbm.addUserSubscribedChannels(channel_id, user_id,add_date);
            response.put("responseType", "/subscribeChannel_accepted");
            System.out.println("/subscribeChannel_accepted");
            Dbm.addTag(user_id,"user","channel_" + channel_username,100);

        }
        else {
            response.put("responseType", "/subscribeChannel_rejected");
            System.out.println("/subscribeChannel_rejected");

        }


        write(response);
    }

    private void unsubscribeChannel(JSONObject request) {
        JSONObject response = new JSONObject();

        String channel_username =  request.getString("channel_username");
        int channel_id = Dbm.getChannel_id(channel_username);

        if(Dbm.is_subscribed(channel_id,user_id)){
            Dbm.removeUserSubscribedChannels(channel_id, user_id);
            response.put("responseType", "/unsubscribeChannel_accepted");
            System.out.println("/unsubscribeChannel_accepted");

        }
        else {
            response.put("responseType", "/unsubscribeChannel_rejected");
            System.out.println("/unsubscribeChannel_rejected");

        }
        write(response);
    }

    private void likeVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id =  request.getInt("video_id");

        if (!Dbm.is_liked(video_id,user_id)){
            String add_date = String.valueOf(LocalDateTime.now());
            Dbm.addUserLike(video_id, user_id,add_date);
            response.put("responseType", "/likeVideo_accepted");
            System.out.println("/likeVideo_accepted");
            ArrayList<tag> userTagList= Dbm.tagList(user_id,"user");
            for(tag tag:userTagList ){
               Dbm.addTag(video_id,"video",tag.tagName,100);

            }
            ArrayList<tag> videoTagList= Dbm.tagList(user_id,"user");
            for(tag tag:videoTagList ){
                Dbm.addTag(user_id,"user",tag.tagName,100);

            }
        }
        else {
            response.put("responseType", "/likeVideo_rejected");
            System.out.println("/likeVideo_rejected");

        }


        write(response);
    }
    private void remove_likedVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id =  request.getInt("video_id");

        if (Dbm.is_liked(video_id,user_id)){
            String add_date = String.valueOf(LocalDateTime.now());
            Dbm.removeUserLike(video_id, user_id,add_date);
            response.put("responseType", "/remove_likedVideo_accepted");
            System.out.println("/remove_likedVideo_accepted");
//            Dbm.addTag(user_id,"user","channel_" + channel_username,100);
            // todo edit recommendation
        }
        else {
            response.put("responseType", "/remove_likedVideo_rejected");
            System.out.println("/remove_likedVideo_rejected");

        }


        write(response);
    }

    private void dislikeVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id =  request.getInt("video_id");

        if (!Dbm.is_disliked(video_id,user_id)){
            String add_date = String.valueOf(LocalDateTime.now());
            Dbm.addUserLike(video_id, user_id,add_date);
            response.put("responseType", "/dislikeVideo_accepted");
            System.out.println("/dislikeVideo_accepted");
//            Dbm.addTag(user_id,"user","channel_" + channel_username,100);
            // todo edit recommendation
        }
        else {
            response.put("responseType", "/dislikeVideo_rejected");
            System.out.println("/dislikeVideo_rejected");

        }


        write(response);
    }


    private void remove_dislikedVideo(JSONObject request) {
        JSONObject response = new JSONObject();
        int video_id =  request.getInt("video_id");

        if (Dbm.is_disliked(video_id,user_id)){
            String add_date = String.valueOf(LocalDateTime.now());
            Dbm.removeUserLike(video_id, user_id,add_date);
            response.put("responseType", "/remove_dislikedVideo_accepted");
            System.out.println("/remove_dislikedVideo_accepted");
//            Dbm.addTag(user_id,"user","channel_" + channel_username,100);
            // todo edit recommendation
        }
        else {
            response.put("responseType", "/remove_dislikedVideo_rejected");
            System.out.println("/remove_dislikedVideo_rejected");

        }


        write(response);
    }
    private void add_savedVideo(JSONObject request) {

        JSONObject response = new JSONObject();

        int video_id = (int) request.get("video_id");
        String playlist_type = (String) request.get("playlist_type");

        Dbm.addSavedVideo(user_id, playlist_type, video_id);

        response.put("responseType", "/add_savedVideo_accepted");

    }

    private void remove_savedVideo(JSONObject request) {
        JSONObject response = new JSONObject();

        int video_id = (int) request.get("video_id");
        String playlist_type = (String) request.get("playlist_type");


        Dbm.removeSavedVideo(user_id, playlist_type, video_id);


        response.put("responseType", "/remove_WatchedVideo_accepted");
    }
// may be used fo recommendation system
//    private void add_WatchLaterVideo(JSONObject request) {
//        JSONObject response = new JSONObject();
//
//        int video_id = (int) request.get("video_id");
//
//        int WatchLaterVideos_id = Dbm.getUserWatchedVideosId(user_id);
//
//        Dbm.addToPlaylist(video_id,WatchLaterVideos_id);
//
//        response.put("responseType","/add_WatchLaterVideo_accepted");
//    }
//    private void remove_WatchLaterVideo(JSONObject request) {
//        JSONObject response = new JSONObject();
//
//        int video_id = (int) request.get("video_id");
//
//        int WatchLaterVideos_id = Dbm.getUserWatchedVideosId(user_id);
//
//        Dbm.removeFromPlaylist(video_id,WatchLaterVideos_id);
//
//        response.put("responseType","/remove_WatchLaterVideo_accepted");
//    }
//    private void add_likedVideo(JSONObject request) {
//        JSONObject response = new JSONObject();
//
//        int video_id = (int) request.get("video_id");
//
//        int likedVideos_id = Dbm.getUserWatchedVideosId(user_id);
//
//        Dbm.addToPlaylist(video_id,likedVideos_id);
//
//        response.put("responseType","/add_likedVideo_accepted");
//    }
//    private void remove_likedVideo(JSONObject request) {
//        JSONObject response = new JSONObject();
//
//        int video_id = (int) request.get("video_id");
//
//        int likedVideos_id = Dbm.getUserWatchedVideosId(user_id);
//
//        Dbm.removeFromPlaylist(video_id,likedVideos_id);
//
//        response.put("responseType","/remove_likedVideo_accepted");
//    }
//    private void add_dislikedVideo(JSONObject request) {
//        JSONObject response = new JSONObject();
//
//        int video_id = (int) request.get("video_id");
//
//        int dislikedVideo_id = Dbm.getUserWatchedVideosId(user_id);
//
//        Dbm.addToPlaylist(video_id,dislikedVideo_id);
//
//        response.put("responseType","/add_dislikedVideo_accepted");
//    }
//    private void remove_dislikedVideo(JSONObject request) {
//        JSONObject response = new JSONObject();
//
//        int video_id = (int) request.get("video_id");
//
//        int dislikedVideo_id = Dbm.getUserWatchedVideosId(user_id);
//
//        Dbm.removeFromPlaylist(video_id,dislikedVideo_id);
//
//        response.put("responseType","/remove_dislikedVideo_accepted");

//    }

    private void edit_commentLike(JSONObject request) {

        JSONObject response = new JSONObject();

        int comment_id = (int) request.get("comment_id");
        String editType = (String) request.get("editType");

        switch (editType) {
            case "addLike" -> Dbm.editCommentLike(comment_id, user_id, "liked");
            case "removeLike" -> Dbm.removeCommentLike(comment_id);
            case "addDislike" -> Dbm.editCommentLike(comment_id, user_id, "Disliked");

        }

        response.put("responseType", "/edit_commentLike_accepted");
    }

    private void sendFile(File file) {
//        try {
//
//            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//
//            long length = file.length();
//            byte[] bytes = new byte[Math.toIntExact(length)];
//            FileInputStream fileInputStream= new FileInputStream(file);
//            fileInputStream.read(bytes);
//            dataOut.write(bytes);                                          //*
//            dataOut.flush();
//            System.out.println("data has been sent");
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {

            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            FileInputStream fileInputStream= new FileInputStream(file);

            long length = file.length();
            dataOut.writeLong(length);
            dataOut.flush();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }
            dataOut.flush();

            System.out.println("file has been sent");


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private  byte[] getUploadedFile() {
        try {
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            long fileLength = dataIn.readLong();

            byte[] bytes = new byte[(int) fileLength];
            int bytesRead;
            int totalBytesRead = 0;
            while (totalBytesRead < fileLength) {
                bytesRead = dataIn.read(bytes, totalBytesRead, bytes.length - totalBytesRead);
                if (bytesRead == -1) { // End of stream
                    break;
                }
                totalBytesRead += bytesRead;
            }

            return bytes;
        }catch (IOException e){
            e.printStackTrace();
        }

        return new byte[0];
    }

    public void closeClientHandler(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File resize(File icon,int video_id) {
        File resizedFile = new File("src/main/resources/DATA/image_examples/lowSize/lowSize" + video_id +".jpg");
        try {
            BufferedImage originalImage = ImageIO.read(icon);
//
//            //To save with original ratio uncomment next line and comment the above.
            originalImage= Scalr.resize(originalImage, 960, 540);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(originalImage, "jpg", baos);
//            baos.flush();
//            byte[] imageInByte = baos.toByteArray();
//            baos.close();
//            return imageInByte;

            // Resize the image

            // Write the resized image to a file
            ImageIO.write(originalImage, "jpg", resizedFile);

            return resizedFile;


        } catch (Exception e) {
            return null;
        }


    }


}
