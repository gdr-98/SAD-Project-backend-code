package com.project.ProxyCameriere;

import org.springframework.core.annotation.Order;

public class OrderJSON {

    private int id;
    private String title;

    public OrderJSON () {
        this.id = 0;
        this.title="";
    }

    public OrderJSON(int ID, String Title) {
        this.id=ID;
        this.title=Title;
    }

}
