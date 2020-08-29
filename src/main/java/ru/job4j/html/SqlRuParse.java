package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SqlRuParse {
    private static final Integer LINK_TD = 1;
    private static final Integer DATE_TD = 5;


    public static void main(String[] args) throws Exception {
        Elements trs = new Elements();

        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements elems = doc.select(".forumTable tr");
            trs.addAll(elems);
        }

        for (Element row : trs) {

            System.out.println(row.child(LINK_TD).text());
            System.out.println(row.child(LINK_TD).attr("href"));

            String date = row.child(DATE_TD).text();
            Date dt = parseDate(date);

            System.out.println(date + " = " + dt);
        }

    }

    private static Date parseDate(String date) {
        Date parsedDate = null;
        String[] splitDate = date.split(" ");

        if (splitDate.length > 1) {
            splitDate[1] = splitDate[1].concat(".");

            String concat = String.join(" ", splitDate);

            SimpleDateFormat formatter = new SimpleDateFormat("dd LLL yy, HH:mm", Locale.getDefault());

            try {
                parsedDate = formatter.parse(concat);
            } catch (ParseException e) {
            }
        }

        return parsedDate;
    }
}
