package com.project.ProxyCameriere.ProxyCameriere.web;

import java.util.HashMap;

public class Webhook{
    public static HashMap<String,String> Waiters = new HashMap<String,String>();
    public static HashMap<String,String> Chef = new HashMap<String,String>();
    public static HashMap<String,String> Pizza_maker = new HashMap<String,String>();
    public static HashMap<String,String> Acceptance = new HashMap<String,String>();
    public static HashMap<String,String> Barman = new HashMap<String,String>();

    public Webhook(){}

    // Add an entry to one of the hashmap
    public static void Add_Waiter(String ID, String URL){
        Waiters.put(ID,URL);
    }

    public static  void Add_Chef(String ID, String URL){
        Chef.put(ID,URL);
    }

    public static void Add_Pizza_maker(String ID, String URL){
        Pizza_maker.put(ID,URL);
    }

    public static void Add_Barman(String ID, String URL){
        Barman.put(ID,URL);
    }

    public static void Add_Acceptance(String ID, String URL){
        Acceptance.put(ID,URL);
    }

}
