/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reciepttrackerparser;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author William Bui
 */
public class RecieptTrackerParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        String title = findTitle();
        String price = findTotal();
        String date = findDate();
        
        System.out.println("Title: " + title);
        System.out.println("Date: " + date);
        System.out.println("Total " + price);
        
        FileWriter output = new FileWriter("output.txt");
        output.write(title + ", " + price + ", " + date);
        output.close();

    }

    //Assuming first line is the company name
    public static String findTitle() throws FileNotFoundException {
        Scanner text = new Scanner(new File ("input.txt"));
        String title = "";
        getNextDetectedText(text);
        title = text.next();
        title=title.substring(1, title.length()-2);
        return title;
    }
    //Finds maximum price, only works if there is $
    public static String findTotal() throws FileNotFoundException{
        Scanner text = new Scanner(new File ("input.txt"));
        skipLines(text);
        String x = "";
        double total = 0;
        while(text.hasNext()){
            getNextDetectedText(text);
            if(!(text.hasNext()))
                break;
            x = text.next();
            if (x.contains("$")){
                x=x.substring(3, x.length()-2);
                total = Math.max(total,Double.parseDouble(x));
            }
        }
        String s = String.format("$%.2f", total);
        
        return s;
    }
    //Checks if the input is actually a date, Need to add more formats I think?
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dateFormat4 = new SimpleDateFormat("MM-dd-yyyy");
        dateFormat1.setLenient(false);
        dateFormat2.setLenient(false);
        dateFormat3.setLenient(false);
        dateFormat4.setLenient(false);
        boolean isDate = false;
        try {
            dateFormat1.parse(inDate);
            isDate = true;
        } catch (ParseException pe) {
            isDate = false;
        }
        if(isDate==false){
        try {
            dateFormat2.parse(inDate);
            isDate = true;
        } catch (ParseException pe) {
            isDate = false;
        }
        }
        if(isDate==false){
        try {
            dateFormat3.parse(inDate);
            isDate = true;
        } catch (ParseException pe) {
            isDate = false;
        }
        
        }
        if(isDate==false){
        try {
            dateFormat4.parse(inDate);
            isDate = true;
        } catch (ParseException pe) {
            isDate = false;
        }
        
        }
        return isDate;
    }
    //Find a date that follows DetectedText
    public static String findDate() throws FileNotFoundException{
        Scanner text = new Scanner(new File ("input.txt"));
        skipLines(text);
        String input="";
        while(text.hasNext()){
            getNextDetectedText(text);
            if(!(text.hasNext()))
                break;
            input = text.next();
            input = input.substring(1,input.length()-2);
            if(isValidDate(input))
            break;
        }
        return input;
    }
    
    //Finds the next DetectedText
    public static void getNextDetectedText(Scanner text) {
        String token = "\"DetectedText\":";
        while (text.hasNext()) {
            String input = text.next();
            if (input.equals(token)) {
                break;
            }
        }
    }
     //In the Rekognition txt, there are two "types" of json object, LINES and WORDS, 
     //I decided to skip the lines to work with the words, more predictable
    public static void skipLines(Scanner text){
        String token = "            \"Type\": \"WORD\", ";
//"            \"Type\": \"WORD\",";
        while (text.hasNextLine()) {
            String input = text.nextLine();
            if (input.equals(token)) {
                break;
            }
        }
    }
}
