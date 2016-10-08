package org.seckill;

import org.junit.Test;
import sun.util.calendar.CalendarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 2016/9/4.
 */
public class IntegerTest {

    private static String startDate = "2016-09-23";
    private static String endDate = "2017-09-03";
    private static String payDayOfMonth = "31";

    private static Calendar startCal;
    private static Calendar endCal;
    private static int payDayOfMonthInt;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static{
        try {
            startCal = Calendar.getInstance();
            startCal.setTime(sdf.parse(startDate));

            endCal = Calendar.getInstance();
            endCal.setTime(sdf.parse(endDate));

            payDayOfMonthInt = Integer.parseInt(payDayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static final int MONTH_LENGTH[]
            = {31,28,31,30,31,30,31,31,30,31,30,31}; // 0-based
    static final int LEAP_MONTH_LENGTH[]
            = {31,29,31,30,31,30,31,31,30,31,30,31}; // 0-based
    @Test
    public void testGenerator(){
        List<Calendar> list = genPayDaySchedule();
        printCalendarList(list);
    }

    private void printCalendarList(List<Calendar> list) {
        for(Calendar cal : list){
            System.out.print(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "  ");
        }
    }

    public List<Calendar> genPayDaySchedule(){
        List<Calendar> calList = new LinkedList<>();
        Calendar nextCal = (Calendar) startCal.clone();
        while(nextCal!=null){
            calList.add(nextCal);
            nextCal = nextPayDate(nextCal);
        }
        calList.add(endCal);
        return calList;

    }

    public int maxDayOfMonth(Calendar cal){
        return CalendarUtils.isGregorianLeapYear(cal.get(Calendar.YEAR))?LEAP_MONTH_LENGTH[cal.get(Calendar.MONTH)]:MONTH_LENGTH[cal.get(Calendar.MONTH)];
    }

    public Calendar nextPayDate(Calendar curCal){
        Calendar clonedCurCal = (Calendar) curCal.clone();
        Calendar nextPayCal = Calendar.getInstance();
        int maxDayOfMonth = maxDayOfMonth(clonedCurCal);
        //判断是否第一期（当且仅当当前日期为开始日期，并且付款日大于当前日期的day_of_month时，需要本月还款）
        if(clonedCurCal.equals(startCal)&&clonedCurCal.get(Calendar.DAY_OF_MONTH)<payDayOfMonthInt){
            nextPayCal = getPayDateInCurMonth(clonedCurCal,maxDayOfMonth);
        }else{
            nextPayCal = nextMonth(clonedCurCal);
        }


        if(nextPayCal.after(endCal)||nextPayCal.equals(endCal)){
            return null;
        }

        return nextPayCal;

    }

    public Calendar nextMonth(Calendar curCal){
        int curDayOfMonth = curCal.get(Calendar.DAY_OF_MONTH);

        curCal.set(Calendar.DAY_OF_MONTH,1);
        curCal.add(Calendar.MONTH,1);

        printCalendar(curCal);

        int actualMaxDayOfMonth = maxDayOfMonth(curCal);

        if(actualMaxDayOfMonth<payDayOfMonthInt){
            curCal.set(Calendar.DAY_OF_MONTH,actualMaxDayOfMonth);
        }else curCal.set(Calendar.DAY_OF_MONTH,payDayOfMonthInt);

        printCalendar(curCal);
        return curCal;

    }

    public Calendar getPayDateInCurMonth(Calendar cal,int maxDayOfMonth){
        int actualPayDayOfMonthInt = payDayOfMonthInt;
        if(actualPayDayOfMonthInt>maxDayOfMonth){
            actualPayDayOfMonthInt = maxDayOfMonth;
        }
        cal.set(Calendar.DAY_OF_MONTH,actualPayDayOfMonthInt);
        return cal;
    }

    public void printCalendar(Calendar cal){
        System.out.println(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH));
    }

}
