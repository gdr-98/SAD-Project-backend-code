package com.project.ProxyAccoglienza.web;

import java.util.HashMap;

public class Webhook{
    public static HashMap<String,String> Acceptance = new HashMap<String,String>();

    public Webhook(){}

    public static void Add_Acceptance(String ID, String URL){
        Acceptance.put(ID,URL);
    }

}
