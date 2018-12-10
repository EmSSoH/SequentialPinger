package SequentalPinger;

/*
 * Code taken from 
 * http://crunchify.com/how-to-get-ping-status-of-any-http-end-point-in-java/
 */
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SequentialPinger {

    public static void main(String args[]) throws Exception {

        String[] hostList = {"http://crunchify.com", "http://yahoo.com",
            "http://www.ebay.com",
            "http://google.com",
            "http://www.example.co",
            "https://paypal.com",
            "http://bing.com/",
            "http://techcrunch.com/",
            "http://mashable.com/",
            "http://thenextweb.com/",
            "http://wordpress.com/",
            "http://cphbusiness.dk/",
            "http://example.com/",
            "http://sjsu.edu/",
            "http://ebay.co.uk/",
            "http://google.co.uk/",
            "http://www.wikipedia.org/",
            "http://dr.dk",
            "http://pol.dk",
            "https://www.google.dk",
            "http://phoronix.com",
            "http://www.webupd8.org/",
            "https://studypoint-plaul.rhcloud.com/",
            "http://stackoverflow.com",
            "http://docs.oracle.com",
            "https://fronter.com",
            "http://imgur.com/",
            "http://www.imagemagick.org"
        };

        ExecutorService es = Executors.newFixedThreadPool(10);

        List<Future<String>> list = new ArrayList<>();

        for (String url : hostList) {

            Callable<String> callable = new MyCallable(url);
            Future<String> future = es.submit(callable);
            list.add(future);
        }

        for (Future<String> fut : list) {

            System.out.println(fut.get());
        }

        es.shutdown();

    }

}

class MyCallable implements Callable<String> {

    private final String url;

    public MyCallable(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        String result = "Error";
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            if (code == 200) {
                result = "Green";
            }
            if (code == 301) {
                result = "Redirect";
            }
            } catch (SocketTimeoutException e) {
            result = "TIMEOUT";

        } catch (Exception e) {
            result = "->Red<-";
        }

        return url + "\t\tStatus:" + result;
    }

}
