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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuParse implements Parse {
    private static final Integer LINK_TD = 1;
    private static final Integer DATE_TD = 5;

    public static void main(String[] args) {

        SqlRuParse parse = new SqlRuParse();
        List<Post> posts = parse.list("https://www.sql.ru/forum/job-offers/");
        posts.forEach(System.out::println);
    }

    @Override
    public List<Post> list(String link) {
        ArrayList<Post> posts = new ArrayList<>();
        Elements trs = new Elements();

        for (int i = 1; i < 2; i++) {
            Document doc = null;
            try {
                doc = Jsoup.connect(link + i).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elems = doc.select(".forumTable tr");
            trs.addAll(elems);
        }

        for (Element row : trs) {
            Post post;
            try {
                String url = row.child(LINK_TD).child(1).attr("href");
                if (url.length() > 0) {
                    post = this.detail(url);
                    post.setName(row.child(LINK_TD).text());
                    posts.add(post);
                }

            } catch (Exception e) {
                continue;
            }
        }

        return posts;
    }

    @Override
    public Post detail(String link) throws Exception {

        Post post = new Post();
        Document doc = Jsoup.connect(link).get();
        Elements elems = doc.select("tr .msgBody");
        Elements dateElems = doc.select("tr td.msgFooter");

        String description = elems.get(1).text();
        String date = parseDescriptionDate(dateElems.get(1).text());
        Date created = parseDate(date);
        post.setCreatedAt(created);
        post.setText(description);
        post.setLink(link);

        return post;
    }


    public static Date parseDate(String date) {
        Date parsedDate = null;
        String[] splitDate = date.split(" ");

        if (splitDate.length > 1) {
            if (splitDate[1].toLowerCase().contains("сен")) {
                splitDate[1] = splitDate[1].concat("т");
            }
            if (splitDate[1].toLowerCase().contains("ноя")) {
                splitDate[1] = splitDate[1].concat("б");
            }

            if (!splitDate[1].toLowerCase().contains("май")) {
                splitDate[1] = splitDate[1].concat(".");
            }

            SimpleDateFormat format = new SimpleDateFormat("dd LLL yy,", Locale.getDefault());
            Calendar cal = Calendar.getInstance();

            if (date.toLowerCase().contains("сегодня")) {
                splitDate[0] = format.format(cal.getTime());
            }

            if (date.toLowerCase().contains("вчера")) {
                cal.add(Calendar.DATE, -1);
                splitDate[0] = format.format(cal.getTime());
            }

            String concat = String.join(" ", splitDate);
            SimpleDateFormat formatter = new SimpleDateFormat("dd LLL yy, HH:mm", Locale.getDefault());

            try {
                parsedDate = formatter.parse(concat);
            } catch (ParseException e) {
                System.out.println(String.format("cant parse string '%s'", concat));
            }
        }

        return parsedDate;
    }

    private static String parseDescriptionDate(String txt) {
        Pattern pattern = Pattern.compile("(^\\d{1,2}\\s.*)\\[");
        Matcher matcher = pattern.matcher(txt);
        matcher.find();
        return matcher.group(1);
    }
}
