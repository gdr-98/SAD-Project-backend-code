package com.project.ProxyRealizzatore.ProxyRealizzatore.web;

import java.util.HashMap;

public class Webhook{
    public static HashMap<String,String> Chef = new HashMap<String,String>();
    public static HashMap<String,String> Pizza_maker = new HashMap<String,String>();

    public Webhook(){}

    public static void Add_Chef(String ID, String URL){
        Chef.put(ID,URL);
    }
    public static void Add_Pizza_maker(String ID, String URL){
        Pizza_maker.put(ID,URL);
    }

}
