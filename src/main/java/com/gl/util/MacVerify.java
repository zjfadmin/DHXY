package com.gl.util;

import java.util.Calendar;
import java.util.Date;

public class MacVerify {
   public static void main(String[] args) {
      System.out.println(System.currentTimeMillis());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DAY_OF_MONTH, 100);//+1今天的时间加一天
      Date date = calendar.getTime();
      System.out.println(date);
      System.out.println(date.getTime());
   }
}
