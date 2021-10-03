package com.project.Proxy.web;

import java.util.ArrayList;
import java.util.List;

public class Webhook {
    private List<String> urls = new ArrayList<String>();

    public Webhook () {
    }

    public Webhook (List<String> urls) {
        this.urls = urls;
    }

    public void addUrl (String url) {
        this.urls.add(url);
    }

    public int removeUrl (String url) {
        if (this.urls.contains(url)) {
            this.urls.remove(url);
            return 0;
        }
        else {
            return -1;
        }
    }

    public final List<String> getUrls () {
        return urls;
    }
}
