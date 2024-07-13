//package server.models;
//
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.ServerSocket;
//
//import org.bytedeco .ffmpeg.global.avcodec;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.Java2DFrameConverter;
//
//import javax.imageio.ImageIO;
//
//public class DurationTest {
//
//
//
//
//    public static void main(String[] args) {
////        splitVideo();
//        try {
//
//            for (int i = 1; i < 29; i++) {
//                randomImg(i);
//                System.out.println(i);
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//    public static void randomImg(int i) throws FFmpegFrameGrabber.Exception {
//        try {
//
//            String videoFilePath = "src/main/resources/DATA/video_examples/"+ i +".mp4";
//
//            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoFilePath);
//            frameGrabber.start();
//
//            // Grab the frame
//            Java2DFrameConverter converter = new Java2DFrameConverter();
//            BufferedImage imag1.jpge = converter.convert(frameGrabber.grab());
//
//            // Save the image to a file
//            ImageIO.write(image, "png", new File("src/main/resources/DATA/image_examples/image_examples2/"+i+".png"));
//
//            frameGrabber.stop();
//        }catch (FFmpegFrameGrabber.Exception e){
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private static void splitVideo() {
//        String inputVideo = "src/main/resources/DATA/video_examples/500.mp4";
//        String outputPart1 = "src/main/resources/DATA/video_examples/part1.mp4";
//        String outputPart2 = "src/main/resources/DATA/video_examples/part2.mp4";
//
//
//        try {
//            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideo);
//            grabber.setTimestamp(0);
//            grabber.start();
//
////            int totalFrames = grabber.getLengthInFrames()  ;
////
////            int halfFrames = totalFrames  ;
//
//
//            long totalTime = grabber.getLengthInTime();
//            long halftime = totalTime / 2;
//
//
//            int audioBitrate = grabber.getAudioBitrate();
//            int videoCodec = grabber.getVideoCodec();
//            int videoBitrate = grabber.getVideoBitrate();
//            double frameRate = grabber.getFrameRate();
//            System.out.println(grabber.getFrameRate());
//
////            int counter = 0;
////            for (int i = 0; i < 2000; i++) {
////                System.out.println(grabber.grab().timestamp);
////                counter ++ ;
////            }
////            System.out.println(counter);
//
//            FFmpegFrameRecorder recorder1 = new FFmpegFrameRecorder(outputPart1, grabber.getImageWidth(), grabber.getImageHeight());
//            recorder1.setVideoQuality(1);
//            recorder1.setAudioChannels(grabber.getAudioChannels());
//            recorder1.setAudioBitrate(audioBitrate);
//            recorder1.setVideoCodec(videoCodec);
//            recorder1.setFrameRate(frameRate);
//            recorder1.setVideoBitrate(videoBitrate);
//            recorder1.setSampleRate(grabber.getSampleRate());
//            recorder1.start();
//
//
//            Frame frame;
//            while ((frame = grabber.grabFrame()) != null && grabber.getTimestamp() < halftime) {
//                recorder1.record(frame); // Record frame to output file
//            }
//
////            for (int i = 0; i < totalFrames * 2; i++) {
////                Frame frame = grabber.grab();
////                recorder1.setTimestamp(grabber.getTimestamp());
////                recorder1.record(frame);
////            }
//
//            FFmpegFrameRecorder recorder2 = new FFmpegFrameRecorder(outputPart2, grabber.getImageWidth(), grabber.getImageHeight());
//            recorder2.setVideoQuality(1);
//            recorder2.setAudioChannels(grabber.getAudioChannels());
//            recorder2.setAudioBitrate(audioBitrate);
//            recorder2.setVideoCodec(grabber.getVideoCodec());
//            recorder2.setVideoBitrate(videoBitrate);
//            recorder2.setFrameRate(frameRate);
//            recorder2.start();
//
//            grabber.setTimestamp(halftime);
//            while ((frame = grabber.grabFrame()) != null && grabber.getTimestamp() < totalTime) {
//                recorder2.record(frame);
//            }
//
////            for (int i = 0; i < halfFrames; i++) {
////                Frame frame1 =  grabber.grab();
////                recorder2.setTimestamp(grabber.getTimestamp());
////                recorder2.record(frame1);
////            }
//
//            grabber.stop();
//            recorder1.stop();
//            recorder2.stop();
//
//
//
//            System.out.println("Video split successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//}
//
