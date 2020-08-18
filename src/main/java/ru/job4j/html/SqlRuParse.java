package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements trs = doc.select(".forumTable tr");
        for (Element row : trs) {

            System.out.println(row.child(1).text());
            System.out.println(row.child(1).attr("href"));
            System.out.println(row.child(5).text());

        }
    }
}
