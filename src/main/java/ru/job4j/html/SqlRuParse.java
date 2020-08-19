package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    private static final Integer LINK_TD = 1;
    private static final Integer DATE_TD = 5;

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements trs = doc.select(".forumTable tr");
        for (Element row : trs) {

            System.out.println(row.child(LINK_TD).text());
            System.out.println(row.child(LINK_TD).attr("href"));
            System.out.println(row.child(DATE_TD).text());

        }
    }
}
