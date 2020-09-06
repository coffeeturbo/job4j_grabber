package ru.job4j.html;


import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

public class SqlRuParseTest {

    @Test
    public void whenParseDateWithStringMonthShort2() {
        String inputDate = "4 сен 20, 17:14";
        Date actualDate = SqlRuParse.parseDate(inputDate);
        String expectedDate = "Fri Sep 04 17:14:00 MSK 2020";

        assertThat(actualDate.toString(), is(expectedDate));
    }
    @Test
    public void whenParseDateWithStringMonthShort() {
        String inputDate = "22 авг 20, 07:44";
        Date actualDate = SqlRuParse.parseDate(inputDate);
        String expectedDate = "Sat Aug 22 07:44:00 MSK 2020";

        assertThat(actualDate.toString(), is(expectedDate));
    }

    @Test
    public void whenParseDateWithStringMonthShortMay() {
        String inputDate = "13 май 20, 19:23";
        Date actualDate = SqlRuParse.parseDate(inputDate);
        String expectedDate = "Wed May 13 19:23:00 MSK 2020";

        assertThat(actualDate.toString(), is(expectedDate));
    }

    @Test
    public void whenParseDateWithStringYeasterday() {
        String inputDate = "вчера, 22:35";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 35);
        cal.set(Calendar.SECOND, 0);
        Date actualDate = SqlRuParse.parseDate(inputDate);
        String expectedDate = cal.getTime().toString();

        assertThat(actualDate.toString(), is(expectedDate));
    }

    @Test
    public void whenParseDateWithStringToday() {
        String inputDate = "сегодня, 14:51";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 51);
        cal.set(Calendar.SECOND, 0);
        Date actualDate = SqlRuParse.parseDate(inputDate);
        String expectedDate = cal.getTime().toString();

        assertThat(actualDate.toString(), is(expectedDate));
    }
}