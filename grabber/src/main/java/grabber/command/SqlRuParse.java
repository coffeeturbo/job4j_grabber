package grabber.command;


import grabber.Parse;
import grabber.model.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SqlRuParse implements Parse {
    private static final Integer LINK_TD = 1;
    private static final Integer DATE_TD = 5;

    @Override
    public List<Post> list(String link) {
        ArrayList<Post> posts = new ArrayList<>();
        Elements trs = new Elements();

        for (int i = 1; i < 2; i++) {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elems = doc.select(".forumTable tr");
            trs.addAll(elems);
        }

        for (Element row : trs) {

            String date = row.child(DATE_TD).text();
            Date dt = parseDate(date);
            Post post = new Post(
                row.child(LINK_TD).text(),
                row.child(LINK_TD).text(),
                row.child(LINK_TD).attr("href"),
                dt
            );

            posts.add(post);

        }

        return posts;
    }

    @Override
    public Post detail(String link) {
        return null;
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
