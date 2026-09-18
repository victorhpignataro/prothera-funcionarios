package com.prothera.springapp.utils;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatters {
  public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private static Locale localeBR = new Locale("pt", "BR");

  public static NumberFormat numberFormatter = NumberFormat.getInstance(localeBR);

}
