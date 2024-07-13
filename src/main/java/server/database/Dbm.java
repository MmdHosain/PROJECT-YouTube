package server.database;

import org.json.JSONArray;
import org.json.JSONObject;
import server.models.TagScore;
import server.models.tag;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dbm {
    private static final String url = "jdbc:mysql://localhost:3306/youtube_db";
    private static final String username = "root";
    private static final String password = "qazmlp2990";
    private static Connection con;
    private static Statement stat;
    private static final String videoPath="src/main/resources/DATA/video_examples/" ;
    private final String profileImagePath="" ;
    private final String channelImagePath="" ;




    private static void open(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(url, username, password);
            stat =  con.createStatement();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    private static void close(){
        try {

            stat.close();
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static JSONObject searchBarList(String toSearch) {
        List<String> searchBarList = new ArrayList<>();
        ArrayList<JSONObject> videos = new ArrayList<>();
        ArrayList<JSONObject> channels = new ArrayList<>();
        ArrayList<JSONObject> playlists = new ArrayList<>();
        open();

        String videoQuery = "SELECT * FROM videos";
        String channelQuery = "SELECT * FROM channels";
//        String playlistQuery = "SELECT * FROM playlists";


        try {
            ResultSet videoResultSet = stat.executeQuery(videoQuery);
            while (videoResultSet.next()) {
                String title = videoResultSet.getString("title");
                int id = videoResultSet.getInt("id");
                JSONObject video = new JSONObject();
                video.put("title",title);
                video.put("id",id);
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet channelResultSet = stat.executeQuery(channelQuery);
            while (channelResultSet.next()) {
                String title = channelResultSet.getString("title");
                int id = channelResultSet.getInt("id");
                JSONObject channel = new JSONObject();
                channel.put("title",title);
                channel.put("id",id);
                channels.add(channel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//            ResultSet playlistResultSet = stat.executeQuery(playlistQuery);
//            while (playlistResultSet.next()) {
//                String title = playlistResultSet.getString("title");
//                searchBarList.add(title);
//            }
//            searchBarList.add("playLists :");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        finally {
            close();
        }

        JSONArray videoResult = findSimilarTitles(toSearch ,videos);
        JSONArray channelResult = findSimilarTitles(toSearch ,channels);
        JSONObject result = new JSONObject();
        result.put("videoResult",videoResult);
        result.put("channelResult",channelResult);
        return result;
    }
    public static void showSearchedContent (){
        // todo bayad age moshabehate esmi kamel mikardam
    }

    public static JSONArray findSimilarTitles(String toSearch, ArrayList<JSONObject> titles) {

        // title
        // id
        // distance

        // Step 1: Use regex to filter out titles that contain the search term
        Pattern pattern = Pattern.compile(".*" + Pattern.quote(toSearch) + ".*", Pattern.CASE_INSENSITIVE);


        // Step 2: Calculate Levenshtein distance for filtered titles
        Map<JSONObject, Integer> titleDistances = new HashMap<>();
        for (JSONObject result : titles) {
            String title = result.getString("title");

            int distance = levenshteinDistance(toSearch, title);

            Matcher matcher = pattern.matcher(title);
            if (!matcher.matches()) {
                distance += 4;
            }

            result.put("distance",distance);

            titleDistances.put(result, distance);
        }



        // Step 3: Sort titles by their distance
        List<Map.Entry<JSONObject, Integer>> sortedEntries = new ArrayList<>(titleDistances.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());

        ArrayList<JSONObject> sortedTitles = new ArrayList<>();

        for (Map.Entry<JSONObject, Integer> entry : sortedEntries) {
            sortedTitles.add(entry.getKey());
        }
        JSONArray finalResult= new JSONArray();
        for (JSONObject a : sortedTitles ){
            finalResult.put(a);
        }
        return finalResult;
    }

    public static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1]
                                    + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1));
                }
            }
        }

        return dp[a.length()][b.length()];
    }




    public static boolean checkUsername(String usernameInput){
        open();
        String query = "SELECT * FROM users " +
                "WHERE username =  '" + usernameInput+ "'";
        try{
            ResultSet rs = stat.executeQuery(query);
            boolean toReturn = rs.next();
            close();
            return toReturn;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return false;
    }


    public static boolean authorize(String usernameInput,String passwordInput) {
        open();
        String query = "SELECT * FROM users " +
                "WHERE username =  '" + usernameInput+ "'" +"AND pass = '" + passwordInput+ "'";
        try{
            ResultSet rs = stat.executeQuery(query);
            boolean bool = rs.next();
            close();
            return bool;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return false;
    }

    public static String getVideo_Title(int id) {
        open();
        String query = "SELECT title  FROM videos " +
                "WHERE id =  " +id ;
        try{
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            String str = rs.getString("title");
            close();
            return str;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "";
    }


    public static String getVideo_creationTime(int id) {
        open();
        String query = "SELECT *  FROM videos " +
                "WHERE id =  " +id ;
        try{
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            String str = rs.getString("creation_date");
            if (str == null){
                str = "";
            }
            return str;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "";
    }

    public static String getVideo_duration(int id) {
        open();
        String query = "SELECT *  FROM videos " +
                "WHERE id =  " +id ;
        try{
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            String str= rs.getString("duration");
            close();
            return str;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "";
    }

    public static int getVideo_totalView(int id) {
        open();
        String query = "SELECT *  FROM videos " +
                "WHERE id =  " +id ;
        try{
            int total = 0;
            ResultSet rs = stat.executeQuery(query);
            if (rs.next()){
                total = rs.getInt("total_view");
            }
            return total;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return 0;
    }

    public static int getVideo_totalLikes(int video_id) {
        open();
        String query = "SELECT COUNT(video_id) AS total FROM saved_videos " +
                "WHERE type = 'liked' AND video_id = " + video_id;
        int totalLikes = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            if(rs.next()){

                totalLikes = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return totalLikes;
    }
    public static List<Integer> getRandomVideoId(int videoCount) {
        String maxId = "SELECT MAX(id) AS maxId FROM videos";
        int max=0;
        List <Integer> randList= new ArrayList<>();
        try {
            ResultSet rs = stat.executeQuery(maxId);
            // bug probablity
            max =rs.getInt("id") ;
        }catch(SQLException e){
            e.printStackTrace();
        }
        for(int i=0 ; i< videoCount ; i++){
            int randomNumber =getRandomNumber(1 ,max );
            randList.add(randomNumber);
        }
        return randList;
    }


    public static int get_user_id(String username) {
        open();
        String query = "SELECT *  FROM users " +
                "WHERE username =  '" + username +"'";
        try{
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            return rs.getInt("id");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return 0;
    }
    public  static  void main (String args[]){
//        addUser("kourosh" , "111" ,"kourosh", "k.rasht@gmail.com" ,"1234" ,false ,"29.3.403" );
//        System.out.println( checkUsername("koursh"));
//        System.out.println(authorize("koursh" ,"111"));
//        todo addVideo(1 , "vid" ,"this is a video" , "320" ,"29.3.403" ,["[\"fun\", \"horror\", \"adventure\"]"]);
//        System.out.println((getVideo_Title(1)));
//        System.out.println(getVideo_creationTime(1));
//        for(String i: searchBarList("heyn")){
//            System.out.println(i);
//        }
//        System.out.println(getChannelTotalVideoes(1));
        System.out.println(getChannelId("heyy"));


    }

    public static int getVideo_totalDislikes(int video_id) {
        open();
        String query = "SELECT COUNT(video_id) AS total FROM saved_videos " +
                "WHERE type = 'disliked' AND video_id = " + video_id;
        int totalDisLikes = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            if(rs.next()){

                totalDisLikes = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return totalDisLikes;
    }


    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static List<Integer> getRandomChannelId(int channelCount) {
        open();
        String maxId = "SELECT MAX(id) AS maxId FROM channels";
        int max=0;
        List <Integer> randList= new ArrayList<>();
        try {
            ResultSet rs = stat.executeQuery(maxId);
            if(rs.next()) {     // rs.next() movesthe curser to the first row of resultset
                max = rs.getInt("maxId");
            }
            for(int i=0 ; i< channelCount ; i++){
                int randomNumber = getRandomNumber(1 ,max );
                randList.add(randomNumber);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return randList;
    }


    public static int getCommentCount(int videoId) {
        open();
        String query = " SELECT * FROM comments " +
                "WHERE video_id = " + videoId ;
        int count =0;
        try {
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()){
                count ++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return count;
    }

    public static List<Integer> getCommentIdList(int videoId) {
        open();
        List<Integer> commentIdList = new ArrayList<>();
        String query = "SELECT id FROM comments" +
                " WHERE video_id =" + videoId;
        try {
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()){
                commentIdList.add( rs.getInt("id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return commentIdList;
    }

    public static String getCommentText(int commentId) {
        open();
        String query = "SELECT body FROM comments" +
                " WHERE id =" + commentId;
        String body="";
        try {
            ResultSet rs = stat.executeQuery(query);
            if (rs.next()){
                body = rs.getString("body");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return body;
    }

    public static String getcommentCreationTime(int commentId) {
        open();
        String query = "SELECT * FROM comments" +
                " WHERE id =" + commentId;
        String creation_time="";
        try {
            ResultSet rs = stat.executeQuery(query);
            if(rs.next()){
                creation_time =rs.getString("creation_date");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return creation_time;
    }


    public static int getChannelId(String channelUserName) {
        open();
        String query = "SELECT * FROM channels " +
                "WHERE channel_username = '" + channelUserName + "'";
        int id = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public static int getChannelTotalVideoes(int channelId) {
        open();
        String query = "SELECT COUNT(video_id) AS total FROM uploaded_videos " +
                "WHERE channel_id = " + channelId;
        int totalVideos = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            totalVideos = rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return totalVideos;
    }
    public static ArrayList<Integer> getSubscribedChannels(int user_id) {

        open();
        ArrayList <Integer> channelVideoList = new ArrayList<>();
        String query = "SELECT channel_id FROM subscribed_channels WHERE user_id = " + user_id;
        try {
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()){
                channelVideoList .add (rs.getInt("channel_id"));
            }
            return channelVideoList;
        }catch (SQLException e){
            e.printStackTrace();

        }
        finally {
            close();
        }
        return null;
    }


    public static int getChannelTotalViews(int channelId) {
        open();
        String query = "SELECT total_views FROM channels WHERE id = " + channelId;
        int totalViews = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            if (rs.next()) { // Move the cursor to the first row
                totalViews = rs.getInt("total_views");
            }else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return totalViews;
    }


    public static int getChannelTotalSubscribers(int channelId) {
        open();
        String query = "SELECT COUNT(*) AS total_subscribers FROM subscribed_channels WHERE channel_id = '" + channelId + "'";
        int totalSubscribers = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            if (rs.next()) {
                totalSubscribers = rs.getInt("total_subscribers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return totalSubscribers;
    }

    public static boolean checkChannelUsername(String channelUsername) {
        open();
        String query = "SELECT * FROM channels " +
                "WHERE channel_name =  '" + channelUsername+ "'";
        try{
            ResultSet rs = stat.executeQuery(query);
            close();
            return rs.next();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return false;
    }



    public static int addVideo(int channelId, String title , String videoDescription, String duration , JSONArray tags) {

        open();
        String maxId = "SELECT MAX(id) AS maxId FROM videos";
        int lastId=0;
        try {
            ResultSet rs = stat.executeQuery(maxId);
            // bug probablity
            rs.next();
            lastId =rs.getInt("maxId") ;
            lastId++;

            String query =
                    "INSERT INTO videos (id , title, video_description ,creation_date,duration) VALUES ("
                            + lastId +",'"+ title + "','" + videoDescription +"','" + LocalDateTime.now() + "',"+0+ ")";
            int res = stat.executeUpdate(query);
            close();
            return lastId;

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return -1;
    }





    public static void removeVideo(int videoId) {
        // todo mmdhosain

    }

    public static int addComment(int video_id,int user_id, String text, int repliedToId,String creationDate) {
        open();
        String maxId = "SELECT MAX(id) AS maxId FROM comments";
        int lastId=0;
        try {
            ResultSet rs = stat.executeQuery(maxId);
            // bug probablity
            if (rs.next()){
                lastId = rs.getInt("maxId");
                lastId++;

                String query =
                        "INSERT INTO comments (id ,user_id, video_id,creation_date, body, reply_to) VALUES ("
                                + lastId + "," + user_id + "," + video_id + ",'" + creationDate + "','" + text + "'," + repliedToId + ")";
                int res = stat.executeUpdate(query);
            }
            close();
            return lastId;

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return -1;
    }

    public static void removeComment(int commentId) {
        // todo all void methods has int rs = stat.executeUpdate(query);
        // todo nemikhay age amaliat dorost anjam nashod -1 return kone?
        open();
        try {
            String query ="DELETE FROM comments WHERE id = " + commentId;
            int rs = stat.executeUpdate(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }


    public static void editCommentLike(int commentId, int userId, String likeType) {
        open();
        try {
            String query = " UPDATE comments SET type = '"+likeType+"' WHERE commentId =  "+commentId ;

            int rs = stat.executeUpdate(query);

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public static void removeCommentLike(int commentLike_id) {
        open();
        try {
            String query = "DELETE FROM liked_comments WHERE id = " + commentLike_id ;

            int rs = stat.executeUpdate(query);

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }
    public static void addUser(String username, String password, String name,String email,
                               String number,boolean is_premium,String creationDate ) {
        open();
        String query = "INSERT INTO users ( username ,name , number ,pass , email , is_premium ,creation_date) VALUES ('"+
                username + "','" + name+"','"+ number + "','" +password + "','" +email+" '," + is_premium +", '"+ creationDate +"')";
        try{
            int rs = stat.executeUpdate(query);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }

    }


    public static String getVideo_description(int id) {
        open();
        String query = "SELECT *  FROM videos " +
                "WHERE id =  " +id ;
        try{
            ResultSet rs = stat.executeQuery(query);
            if(rs.next()) {
                return rs.getString("video_description");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "";
    }


    public static String getVideo_link(int videoId) {
        return videoPath +"/"+ videoId+".mp4";
    }


    public static String getUsername(int userId) {
        open();
        String query = "SELECT *  FROM channels " +
                "WHERE id =  " +userId ;
        try{
            ResultSet rs = stat.executeQuery(query);
            if(rs.next()){
                return rs.getString("channel_username");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return "";
    }

    public static String getChannelDescription(int channelId) {
        open();
        String query = "SELECT * FROM channels WHERE id = " + channelId;
        try {
            ResultSet rs = stat.executeQuery(query);
            if (rs.next()) {
                return rs.getString("channel_description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return "";
    }


    public static int addChannel(String channelName,String channelUsername, int owner_id, String creationDate ,String channelDescription ) {
        // it returns the new channel id
        open();
        String maxId = "SELECT MAX(id) AS maxId FROM channels";
        int lastId=0;
        try {
            ResultSet rs = stat.executeQuery(maxId);
            // bug probablity
            lastId =rs.getInt("maxId") ;
            lastId++;
            String query =
                    "INSERT INTO TABLE channels (id , owner_id, channel_username, channel_description ,creation_date) VALUES ("
                            + lastId +","+ owner_id + ",'"+ channelUsername + "','" + channelDescription +"','" + creationDate + "')";
            int res = stat.executeUpdate(query);
            close();
            return lastId;

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return -1;
    }

    public static List<Integer> getChannelVideoList(int channelId) {
        open();
        List <Integer> channelVideoList = new ArrayList<>();
        String query = "SELECT video_id FROM uploaded_videos WHERE channel_id = " + channelId;
        try {
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()){
                channelVideoList .add (rs.getInt("video_id"));
            }
            return channelVideoList;
        }catch (SQLException e){
            e.printStackTrace();

        }
        finally {
            close();
        }
        return null;
    }

    public static ArrayList<tag> tagList(int item_id,String type){
        open();
        try{

            String videoTagQuery = "SELECT * FROM tag where type = '"+type+"' AND item_id = " + item_id;
            ResultSet videoTagsRs = stat.executeQuery(videoTagQuery);
            ArrayList<tag> tags = new ArrayList<>();
            while (videoTagsRs.next()){
                String tagName = videoTagsRs.getString("tag_name");
                int score = videoTagsRs.getInt("score");
                tags.add(new tag(tagName,score));
            }
            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return null;
    }
    public static ArrayList<Integer> getRecommendedVideos(int userId) {
        open();
        ArrayList <Integer> videoIdList = new ArrayList<>();

        ArrayList <tag> userTags = new ArrayList<>();
        ArrayList <TagScore> videoTagScores = new ArrayList<>();
        ArrayList <TagScore> sortedVideoTagScores = new ArrayList<>();

        String userQuery = "SELECT * FROM tag WHERE item_id =  " + userId + " AND type = 'user' ";
        String videosQuery = "SELECT id FROM videos ";
        try {
            ResultSet tagRs = stat.executeQuery(userQuery);

            while (tagRs.next()){

                String tagName = tagRs.getString("tag_name");
                int score = tagRs.getInt("score");
                userTags.add(new tag(tagName,score));

            }
            tagRs.close();
            ResultSet videosRs = stat.executeQuery(videosQuery);
            while (videosRs.next()){
                videoIdList.add(videosRs.getInt("id"));
            }
            videosRs.close();

            for (int i = 0; i < videoIdList.size(); i++) {
                int video_id = videoIdList.get(i);
                String videoTagQuery = "SELECT * FROM tag where type = 'video' AND item_id = " + video_id;
                ResultSet videoTagsRs = stat.executeQuery(videoTagQuery);
                ArrayList<tag> tags = new ArrayList<>();
                while (videoTagsRs.next()){
                    String tagName = videoTagsRs.getString("tag_name");
                    int score = videoTagsRs.getInt("score");
                    tags.add(new tag(tagName,score));
                }
                videoTagScores.add(new TagScore(video_id,tags));
            }

            close();

            for (int i = 0; i < userTags.size(); i++) {
                tag tag = userTags.get(i);
                for (int j = 0; j < videoTagScores.size(); j++) {
                    TagScore tagScore = videoTagScores.get(j);
                    for (int k = 0; k < tagScore.tags.size(); k++) {
                        if(tagScore.tags.get(k).tagName.equals(tag.tagName)){
                            tagScore.totalScore += tagScore.tags.get(k).score * tag.score;
                        }
                    }
                }
            }
            sortedVideoTagScores.add(videoTagScores.get(0));
            for (int i = 1; i < videoTagScores.size(); i++) {
                boolean x = true;
                for (int j = 0; j < sortedVideoTagScores.size(); j++) {
                    if(videoTagScores.get(i).totalScore>sortedVideoTagScores.get(j).totalScore && x){
                        sortedVideoTagScores.add(j,videoTagScores.get(i));
                        x= false;
                    }
                }
                if (x){
                    sortedVideoTagScores.add(videoTagScores.get(i));
                }
            }

            videoIdList.clear();
            for (int i = 0; i < sortedVideoTagScores.size(); i++) {
                videoIdList.add(sortedVideoTagScores.get(i).video_id);
            }

            return videoIdList;
        }catch (SQLException e){
            e.printStackTrace();

        }
        finally {
            close();
        }
        return null;
    }

    public static List<Integer> getVideoCommentList(Integer videoId) {
        open();
        List <Integer> videoCommentList = new ArrayList<>();
        String query = "SELECT id FROM comments WHERE video_id = " + videoId;
        try {
            ResultSet rs = stat.executeQuery(query);
            close();
            while (rs.next()){
                videoCommentList .add (rs.getInt("id"));
            }
            return videoCommentList;
        }catch (SQLException e){
            e.printStackTrace();
            close();
        }
        return null;
    }

    public static void removeChannel(int channelId) {
        open();
        try {
            String query = "DELETE FROM channels WHERE id = " + channelId ;
            int rs = stat.executeUpdate(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }



    public static String getVideoPath() {
        return videoPath;
    }

    public static void addUserSubscribedChannels(int channelId, int userId , String add_date) {
        open();
        String query = "INSERT INTO subscribed_channels (channel_id , user_id , is_notif_on , add_date "
                +") VALUES ( " + channelId +","+ userId +","+ false +",'"+ add_date+"')";
        try{
            int rs = stat.executeUpdate(query);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }
    public static void addUserLike(int videoId, int userId, String addDate) {
        open();
        String query = "INSERT INTO saved_videos (video_id , user_id , type , add_date "
                +") VALUES ( " + videoId +","+ userId +",'"+ "liked" +"','"+ addDate+"')";
        try{
            int rs = stat.executeUpdate(query);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }
    public static void removeUserLike(int videoId, int userId, String addDate) {
        open();
        try {
            String query = "DELETE FROM saved_videos WHERE videoId = " + videoId
                    +" AND user_id = "+userId
                    +" AND type = liked" ;
            int rs = stat.executeUpdate(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }


    public static void removeUserSubscribedChannels(int channelId, int userId) {
        open();
        try {
            String query = "DELETE FROM subscribed_channels WHERE channel_id = " + channelId
                    +" AND user_id = "+userId ;
            int rs = stat.executeUpdate(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public static void addSavedVideo(int userId, String playlistType, int videoId) {
        open();
//        String maxId = "SELECT MAX(id) AS maxId FROM videos";
//        int lastId=0;
//        try {
//            ResultSet rs = stat.executeQuery(maxId);
//            // bug probablity
//            lastId =rs.getInt("maxId") ;
//            lastId++;

//            String query =
//                    "INSERT INTO TABLE saved_videos ( user_id ,video_Id , type ) VALUES ("
//                            + userId +","+ videoId + ",'" + playlistType+ "')";
//            int res = stat.executeUpdate(query);
//            close();
//
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        finally {
//            close();
//        }
//        return -1;

    }

    public static void removeSavedVideo(int userId, String playlistType, int videoId) {
    }
    public static int getcommentRepliedTo(int commentId) {
        // todo is it to understand that this comment is replied to which comment?
//        String query = "SELECT * FROM comments" +
//                " WHERE id =" + commentId;
//        int commentRepliedToId=0;
//        try {
//            ResultSet rs = stat.executeQuery(query);
//            commentRepliedTo =rs.getString("body");
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
        return -1;
    }

    public static int getCommentTotalLikes(int commentId){
        open();
        String query = "SELECT *  FROM videos" +
                "WHERE id =  " +commentId ;
        try{
            ResultSet rs = stat.executeQuery(query);
            return rs.getInt("total_likes");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
        return 0;
    }


    public static int getchannel_id(int video_id) {
        open();
        String query = "SELECT * FROM uploaded_videos " +
                "WHERE video_id = '" + video_id + "'";
        int id = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            id = rs.getInt("channel_id");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public static String getchannel_name(int channelId) {
        open();
        String query = "SELECT * FROM channels " +
                "WHERE id = '" + channelId + "'";
        String title = "demo";
        try {
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            title = rs.getString("title");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return title;
    }

    public static String getChannelUsername(int channel_id) {
        open();
        String query = "SELECT * FROM channels " +
                "WHERE id = '" + channel_id + "'";
        String title = "demo";
        try {
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            title = rs.getString("channel_username");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return title;
    }

    public static int getChannel_id(String channelUsername) {
        open();
        String query = "SELECT * FROM channels " +
                "WHERE channel_username = '" + channelUsername + "'";
        int id = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public static boolean is_subscribed(int channel_id,int user_id) {
        open();

        String query = "SELECT * FROM subscribed_channels " +
                "WHERE channel_id = " + channel_id
                + " AND user_id = " + user_id;

        try {
            ResultSet rs = stat.executeQuery(query);
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
    public static boolean is_liked(int videoId, int userId) {
        open();

        String query = "SELECT * FROM saved_videos " +
                "WHERE video_id = " + videoId
                + " AND user_id = " + userId
                + " AND type = 'liked'" ;

        try {
            ResultSet rs = stat.executeQuery(query);
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public static boolean is_disliked(int videoId, int userId) {
        open();

        String query = "SELECT * FROM saved_videos " +
                "WHERE video_id = " + videoId
                + " AND user_id = " + userId
                + " AND type = 'disliked'" ;

        try {
            ResultSet rs = stat.executeQuery(query);
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public static void addTag(int itemId, String type, String tagName, int score) {
        open();
        try {
            String query =
                    "INSERT INTO tag (item_id , type, score , tag_name) VALUES ("
                            + itemId +",'"+ type + "'," + score +",'" + tagName + "')";

            int res = stat.executeUpdate(query);

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }

    }

    public static int addPlaylist(String title, String description, String channelUsername) {
        open();
        try {


            int channel_id = Dbm.getChannel_id(channelUsername);
            open();
            String query =
                    "INSERT INTO playlists (channel_id , title, playlist_description , creation_date) VALUES ("
                             + channel_id +",'"+ title + "','" + description +"','" + LocalDateTime.now() + "')";

            int res = stat.executeUpdate(query);
            return Dbm.getPlaylistId(title);
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {

            close();

        }
        return 2;
    }

    public static JSONArray getPlaylistList(int channelId) {
        open();
        JSONArray result = new JSONArray();
        String query = "SELECT title FROM playlists WHERE channel_id = " + channelId;
        try {
            ResultSet rs = stat.executeQuery(query);

            while (rs.next()){
                result .put(rs.getString("title"));
            }
            return result;
        }catch (SQLException e){
            e.printStackTrace();
            close();
        }
        return null;

    }

    public static void addVideotoPlaylist(int videoId, String playlist) {
        open();
        try {
            String query =
                    "INSERT INTO tag (video_id , playlist_id) VALUES ("
                            + videoId +","+ Dbm.getPlaylistId(playlist) + " )";

            int res = stat.executeUpdate(query);

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    private static int getPlaylistId(String playlist) {
        open();
        String query = "SELECT * FROM playlists " +
                "WHERE title = '" + playlist + "'";
        int id = 0;
        try {
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public static void addVideotoChannel(int videoId, int channelId) {
        open();
        try {
            String query =
                    "INSERT INTO uploaded_videos (video_id , channel_id) VALUES ("
                            + videoId +","+ channelId + " )";

            int res = stat.executeUpdate(query);

        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public static void addView(int videoId, int user_id) {
        open();
        try {
            String query0 = "SELECT video_id FROM saved_videos WHERE type = 'watched'";
            String query =
                    "INSERT INTO saved_videos (video_id , user_id,type,add_date) VALUES ("
                            + videoId +","+ user_id + ",'watched','"+  LocalDateTime.now()+ "')";
            ResultSet rs0 = stat.executeQuery(query0);
            if(!rs0.next()){

                int res = stat.executeUpdate(query);
            }


        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public static List<Integer> getHistoryVideoList(int userId) {
        open();
        List <Integer> VideoList = new ArrayList<>();
        String query = "SELECT video_id FROM saved_videos WHERE type = 'watched'";

        try {
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()){
                VideoList .add (rs.getInt("video_id"));
            }
            return VideoList;
        }catch (SQLException e){
            e.printStackTrace();

        }
        finally {
            close();
        }
        return null;
    }

    public static List<Integer> watchLaterVideoList(int userId) {
        open();
        List <Integer> VideoList = new ArrayList<>();
        String query = "SELECT video_id FROM saved_videos WHERE type = 'watchLater'";

        try {
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()){
                VideoList .add (rs.getInt("video_id"));
            }
            return VideoList;
        }catch (SQLException e){
            e.printStackTrace();

        }
        finally {
            close();
        }
        return null;
    }

    public static void WatchLaterVideo(int videoId,int user_id) {
        open();
        try {
            String query0 = "SELECT video_id FROM saved_videos WHERE type = 'watchLater'";
            String query =
                    "INSERT INTO saved_videos (video_id , user_id,type,add_date) VALUES ("
                            + videoId +","+ user_id + ",'watchLater','"+  LocalDateTime.now()+ "')";
            ResultSet rs0 = stat.executeQuery(query0);
            if(!rs0.next()){

                int res = stat.executeUpdate(query);
            }


        }catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public static List<Integer> likeVideoList(int userId) {
        open();
        List <Integer> VideoList = new ArrayList<>();
        String query = "SELECT video_id FROM saved_videos WHERE type = 'liked'";

        try {
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()){
                VideoList .add (rs.getInt("video_id"));
            }
            return VideoList;
        }catch (SQLException e){
            e.printStackTrace();

        }
        finally {
            close();
        }
        return null;
    }
}
