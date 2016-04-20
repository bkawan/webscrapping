/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bk.webscrappercybersansar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bikeshkawan
 */
public class Program {

    private static String model;
    private static String album;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // String imageUrl = "http://cybersansar.com/graphics/model/liril_fresh_face_season_3/liril fresh face_season 3_544611200.jpg";
        // String imageUrl = "http://www.portersprogressuk.org/wp-content/uploads/2015/04/nepal-update.jpg";

        // String destinationFile = "image.jpg";
        //writeImage(imageUrl, destinationFile);
        // TODO code application logic here
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Cyber Sansar model page url: ");
        String link = input.nextLine();
        String[] albumId = link.split("=");
        album = albumId[1];
        System.out.println(link);
        String data = getURLContent(link);
        //to check if data is printing
        // System.out.println(" I am here" + data);
        // String imgPattern = "<img src=\"graphics/model/(.*?)\\.jpg\" width=\"100\" height=\"150\" border=\"0\" />";
        String imgPattern = "graphics/model/(.*?)\\.jpg"; // whole line is group 0 then first ? is group 1 and so on

        Pattern pattern = Pattern.compile(imgPattern);
        // System.out.println("I am here" + pattern);
        Matcher matcher = pattern.matcher(data);
        // System.out.println("I am here" +matcher.group(0));
        //   System.out.println("I am matcher group" +matcher.group());

        int i = 1;
        while (matcher.find()) {

            String imagePath = "http://www.cybersansar.com/graphics/model/" + matcher.group(1) + ".jpg";
            // System.out.println("I am matcher group" +matcher.group(1));
            String[] modelName = matcher.group(1).split("/");
            System.out.println("Model:" + modelName[0]);
            model = modelName[0];

            imagePath = imagePath.replace("/thumb", "");
            System.out.println("Image Path: " + imagePath);

            writeImage(imagePath, i + "_" + model + ".jpg");
            // writeFile(i, imagePath);

            i++;
        }

        // create directory for model
        File model_dir = new File("images_models/" + model);
        model_dir.mkdir();

    }

    public static String getURLContent(String uri) {

        try {
            // TODO code application logic here

            //establish url
            URL url = new URL(uri);
            //connect url 
            URLConnection conn = url.openConnection();
            // to take data from inputstream
            //conn.getInputStream();

            Reader reader = new InputStreamReader(conn.getInputStream());
            // to read line by line 
            BufferedReader bufferedReader = new BufferedReader(reader);

            //this will read only one line
            // System.out.println(bufferedReader.readLine());
            String line = "";
            //concate string 
            //to consume low memory
            StringBuilder content = new StringBuilder();
            // this will read all the line by looping
            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                //first read all the content and append to string builder
                //content.append(line + "\r\n");
                content.append(line);//this is content and we need to match this content
                //System.out.println(line);

            }
            //after appending all the string 
            //System.out.println(content);

            // try return string 
            return content.toString();

        } catch (MalformedURLException ex) {
            System.out.println(ex.getMessage());

            return null;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }

    /**
     *
     * @param uri url source for image
     * @param fileName to be saved as
     */
    public static void writeImage(String uri, String fileName) {

        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            //InputStream is = url.openStream();
            // URL  url = new URL(uri);
            //  URLConnection conn = url.openStream();

            InputStream inputstream = conn.getInputStream();

            // to save image 
            // Image are binary file so
            // path for album
            String album_path = "images_models/" + model + "/album-id-" + album;
            File album_dir = new File(album_path);
            album_dir.mkdir();//create directory 
            
            OutputStream outputstream = new FileOutputStream(album_path + "/" + fileName);

            byte[] b = new byte[1048];
            int length;

            while ((length = inputstream.read(b)) != -1) {
                outputstream.write(b, 0, length);
                //System.out.println("success");

            }
            // System.out.println("success");
            inputstream.close();
            outputstream.close();
        } catch (MalformedURLException ex) {
            System.out.println("Error 2 ");
            System.out.println(ex.getMessage());

        } catch (IOException ex) {
            System.out.println("Error 1");
            System.out.println(ex.getMessage());

        }

    }

//    public static void writeFile(int num, String content) throws IOException{
//        FileWriter writer = new FileWriter(new File("url_" + num + ".html"));
//        writer.write(content);
//    }
}
