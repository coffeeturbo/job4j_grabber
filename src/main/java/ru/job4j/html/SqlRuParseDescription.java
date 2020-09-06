package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SqlRuParseDescription {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t").get();
        Elements elems = doc.select("tr .msgBody");
        Elements dateElems = doc.select("tr td.msgFooter");


        String description = elems.get(1).text();
        String date = parseDate(dateElems.get(1).text());

        System.out.println(description);
        System.out.println(date);

    }

    private static String parseDate(String txt) {
        Pattern pattern = Pattern.compile("(^\\d{1,2}\\s.*)\\[");
        Matcher matcher = pattern.matcher(txt);
        matcher.find();
        return matcher.group(1);
    }
}
