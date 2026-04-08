package org.demo.lesson.practice;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Demo {
    
    public static void main(String[] args) {

        String zdt = ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("MMMM d, yyyy h:mma z", Locale.ENGLISH));
        System.out.println("Zoned: " + zdt);

        System.out.println("Regular: " + ZonedDateTime.now());

        System.out.println("String: " + ZonedDateTime.now().toString());

        System.out.println("Instant: " + ZonedDateTime.now().toInstant());

        System.out.println("w Truncating: " + ZonedDateTime.now().toInstant().truncatedTo(ChronoUnit.MINUTES).toString());

        String ldt = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"));
        System.out.println("Local: " + ldt);
    }
}
