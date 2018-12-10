/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Magnus West Madsen
 */
public class Rewrite {

    public static void main(String[] args) throws Exception {

        String[] hostList = {
            "http://www.fck.dk", "http://www.google.com", "http://politiken.dk/"
        };
        long start = System.nanoTime();
        ExecutorService es = Executors.newFixedThreadPool(10);

        List<Future<String>> list = new ArrayList<>();

        for (String url : hostList) {

            Callable<String> callable = new webscraper.MyCallable(url);
            Future<String> future = es.submit(callable);
            list.add(future);
        }

        for (Future<String> fut : list) {

            System.out.println(fut.get());
        }

        es.shutdown();
        long end = System.nanoTime();
        System.out.println("Time Sequential: " + ((end - start) / 100000));
    }

}

class MyCallable implements Callable<String> {

    String url;
    String title;
    int h1Count, h2Count, divCount, bodyCount;

    public MyCallable(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            // get page title
            this.title = doc.title();
            Elements h1s = doc.select("h1");
            this.h1Count = h1s.size();
            Elements h2s = doc.select("h2");
            this.h2Count = h2s.size();
            Elements divs = doc.select("div");
            this.divCount = divs.size();
            Elements bodys = doc.select("body");
            this.bodyCount = bodys.size();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "Title: " + getTitle() + "\nDiv's " + getDivCount() + "\nBody's " + getBodyCount();
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public int getH1Count() {
        return h1Count;
    }

    public int getH2Count() {
        return h2Count;
    }

    public int getDivCount() {
        return divCount;
    }

    public int getBodyCount() {
        return bodyCount;
    }

}
