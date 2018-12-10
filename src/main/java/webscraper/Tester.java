package webscraper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Tester {

    public static void main(String[] args) {

        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());

        TagCounter tc1 = new TagCounter("http://www.fck.dk");
        TagCounter tc2 = new TagCounter("http://www.google.com");
        TagCounter tc3 = new TagCounter("http://politiken.dk/");
        

        long start = System.nanoTime();
        tc1.start();
        tc2.start();
        tc3.start();
        
       
         try {
            tc1.join();
            tc2.join();
            tc3.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        long end = System.nanoTime();
        System.out.println("Time Sequential: " + ((end - start) / 100000));

    }
}
