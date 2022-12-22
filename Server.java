package com.mycompany.networking;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.net.*;

public class Server{

    int port=2222;

    InetAddress ServerIp;
    int ParentP;
    InetAddress ParentA;
    int KidP;
    InetAddress KidA;          

  
    
    public Server() throws Exception{
    
    // Create Server IP Address - تجهيز عنوان الخادم 
    ServerIp=InetAddress.getLocalHost();
    
// Create UDP Server Socket - صنع مقبس من نوع يو دي بي
    DatagramSocket DS = new DatagramSocket(port);
    
// keep the server running - اابقاء الخادم متصلا
    while(true){
    
//  Create Buffer for receiving request - صنع مخزن مؤقت لاستقبال الطلبات
        byte receive[] = new byte[1024];
        
//Create Packet for receiving request - صنع حزمة بيانات لاستقبال الطلبات
        DatagramPacket DP = new DatagramPacket(receive, receive.length);
        
// receiving requests - استقبال الطلبات
        DS.receive(DP);
        
// Store received request - تخزين الطلب المستقبل
       String Request = ReceivedReq(DP);

   //get Parent info - تخزين معلومات الاب
   if(Request.trim().equalsIgnoreCase("Parent")){
       
      ParentA = DP.getAddress(); 
      ParentP = DP.getPort();

    }// Parent info method finished - نهاية ميثود الاب
   
      //get Kid info - تخزين معلومات الابن
   if(Request.trim().equalsIgnoreCase("Kid")){
       
     KidA = DP.getAddress(); 
      KidP = DP.getPort();
   byte[] SB = new byte[1024];
   String ON="im_Online";
   SB = ON.getBytes();
    // create datagram packet to Send to Kid - صنع حزمة بيانات لارسالها للابن
    DatagramPacket PE = new DatagramPacket(SB, SB.length, KidA, KidP);
    DS.send(PE);

    }// Kid info method finished-  نهاية ميثود الابن
   

    //First request\ Kid Day over - الطلب الاول انتهاء يوم الطفل
    if(Request.trim().equalsIgnoreCase("Day_Over")){
    
   byte[] SB = new byte[1024];
   String end="DayOver";
   SB = end.getBytes();
    // create datagram packet to Send to parent - صنع حزمة بيانات لارسالها للاب 
    DatagramPacket PE = new DatagramPacket(SB, SB.length, ParentA, ParentP);
    DS.send(PE);
        
    }// Kid Day over method finished - نهاية ميثود انتهاء يوم الطفل
    
//Second request\ parent call is calling - الطلب الثاني اتصال الاب
    else if(Request.trim().equalsIgnoreCase("Call_Kid")){
   
   byte[]SB = "im_out".getBytes();
    //create datagram packet to Send to Kid - صنع حزمة بيانات لارسالها للابن
    DatagramPacket KC = new DatagramPacket(SB, SB.length, KidA, KidP);
    DS.send(KC);
    
    }// parent is calling method is finished - انتهاء ميثود اتصال الاب
    }// while loop finished - انتهاء لوب الوابل
    }// constructor finished - انتهاء الكونستركتر

    // Create method to read the request and print it - صنع ميثود لقرائة الطلبات 
public String ReceivedReq(DatagramPacket DP) throws Exception {
       
        byte[] buf = DP.getData();
        // create Input Stream reader - صنع ستريم ريدر للادخال
        InputStreamReader ISR = new InputStreamReader(new ByteArrayInputStream(buf));
        // Create Buffer Reader - صنع قارئ البفر
        BufferedReader BR = new BufferedReader(ISR);
        // store it in String - تخزينه في سترينج
        String request = BR.readLine();
        
        System.out.println("Message from " + DP.getAddress().getHostAddress()+ ": " + request);
    
        return request;
        
}// ReceivedReq method finished - نهاية ميثود استلام الطلبات
    
    //MAIN - الكلاس الرئيسي


    public static void main (String[]args) throws Exception{
    // call the server - استدعاء السيرفر
    new Server();
    
    
    }
}