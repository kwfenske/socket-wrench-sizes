/*
  Metric SAE Sizes #3 - Metric Wrench Sizes Close To SAE Sizes
  Written by: Keith Fenske, http://kwfenske.github.io/
  Tuesday, 22 November 2022
  Java class name: MetricSaeSizes3
  Copyright (c) 2022 by Keith Fenske.  Apache License or GNU GPL.

  MetricSaeSizes is a Java 1.4 console application to calculate how close
  metric bolt/nut/socket/wrench sizes are to fractional/inch/SAE/standard
  sizes.  This program and its output are not meant to be pretty.  There are no
  options or command-line parameters.  You must edit the program to change the
  output.  (See the "constants" section below.)

  Rounding towards the nearest measurement unit ignores an obvious physical
  reality: a wrench that is too small is more of a problem than a wrench that
  is too big.  There should be a bias added so the rounding occurs in a range,
  say from 30% of a unit smaller to 70% bigger, instead of 50-50.  Explaining
  that, or any other technical details, defeats the point of a simple chart.
  You would also need to justify the chosen bias or range.

  Apache License or GNU General Public License
  --------------------------------------------
  MetricSaeSizes3 is free software and has been released under the terms and
  conditions of the Apache License (version 2.0 or later) and/or the GNU
  General Public License (GPL, version 2 or later).  This program is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY,
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
  PARTICULAR PURPOSE.  See the license(s) for more details.  You should have
  received a copy of the licenses along with this program.  If not, see the
  http://www.apache.org/licenses/ and http://www.gnu.org/licenses/ web pages.
*/

import java.io.*;                 // standard I/O
import java.text.*;               // number formatting

public class MetricSaeSizes3
{
  /* constants */

  static final double BIAS = 0.0; // shift range when rounding to integer units
  static final double MM_PER_INCH = 25.4; // exact millimeters per inch
  static final int UNITS_PER_INCH = 16; // power of two, usually 16 or 32
  static final int UNITS_PER_MM = 1; // normally one unit or two (0.5 mm)

/*
  main() method

  We run as a console application.  There is no graphical interface.
*/
  public static void main(String[] args)
  {
    String flag;                  // quick visual for how close
    NumberFormat formatPointOne, formatPointThree, formatPointFive;
                                  // format with decimal digits
    int i;                        // index variable
    double mm_exact;              // exact metric size in millimeters
    double mm_round;              // metric size after rounding (mm)
    int mm_units;                 // number of metric units (rounded)
    double ratio;                 // rounded divided by exact
    double sae_exact;             // exact SAE size in inches
    double sae_round;             // SAE size after rounding (inch)
    int sae_units;                // number of SAE units (rounded)

    formatPointOne = NumberFormat.getInstance(); // current locale
    formatPointOne.setMaximumFractionDigits(1); // maybe one decimal digit
    formatPointOne.setMinimumFractionDigits(0); // and possibly no decimal

    formatPointThree = NumberFormat.getInstance();
    formatPointThree.setMaximumFractionDigits(3); // always 3 decimal digits
    formatPointThree.setMinimumFractionDigits(3);

    formatPointFive = NumberFormat.getInstance();
    formatPointFive.setMaximumFractionDigits(5); // always 5 decimal digits
    formatPointFive.setMinimumFractionDigits(5);

    /* metric to SAE */

    System.out.println();         // blank line
    System.out.println("Metric/millimeter to fractional/inch/SAE/standard:");
    for (i = (4 * UNITS_PER_MM); i <= (32 * UNITS_PER_MM); i ++)
    {                             // from 4mm to 32mm
      mm_exact = (double) i / (double) UNITS_PER_MM;
      sae_exact = mm_exact / MM_PER_INCH; // exact size in inches
      sae_units = (int) Math.round(sae_exact * UNITS_PER_INCH + BIAS);
      sae_round = (double) sae_units / (double) UNITS_PER_INCH;
      ratio = sae_round / sae_exact; // ratio of rounded to exact
      if ((ratio > 0.997) && (ratio < 1.003)) flag = " very good";
      else if ((ratio > 0.992) && (ratio < 1.008)) flag = " good";
      else flag = "";             // nothing special, not close enough
      System.out.println(formatPointOne.format(mm_exact) + " mm = "
        + formatPointThree.format(sae_exact * UNITS_PER_INCH) + " / "
        + UNITS_PER_INCH + " inch, rounded to "
        + fraction(sae_units, UNITS_PER_INCH) + " has ratio "
        + formatPointFive.format(ratio) + flag);
    }

    /* SAE to metric */

    System.out.println();
    System.out.println("Fractional/inch/SAE/standard to metric/millimeter:");
    for (i = (UNITS_PER_INCH / 8); i <= (UNITS_PER_INCH * 5 / 4); i ++)
    {                             // from 1/8" to 1-1/4"
      sae_exact = (double) i / (double) UNITS_PER_INCH;
      mm_exact = sae_exact * MM_PER_INCH; // exact size in millimeters
      mm_units = (int) Math.round(mm_exact * UNITS_PER_MM + BIAS);
      mm_round = (double) mm_units / (double) UNITS_PER_MM;
      ratio = mm_round / mm_exact; // ratio of rounded to exact
      if ((ratio > 0.997) && (ratio < 1.003)) flag = " very good";
      else if ((ratio > 0.992) && (ratio < 1.008)) flag = " good";
      else flag = "";             // nothing special, not close enough
      System.out.println(fraction(i, UNITS_PER_INCH) + " inch = "
        + formatPointThree.format(mm_exact) + " mm, rounded to "
        + formatPointOne.format(mm_round) + " has ratio "
        + formatPointFive.format(ratio) + flag);
    }

  } // end of main() method

/*
  fraction() method

  Convert an integer numerator and denominator into a fraction as a string.
  The numerator must be non-negative and the denominator is a power of two.
*/
  static String fraction(int top, int bottom)
  {
    int denom, num, whole;        // individual parts of the fraction
    String result;                // what we return to the caller

    whole = top / bottom;         // integer part
    num = top % bottom;           // starting numerator for fraction
    denom = bottom;               // reduces while inside <while> loop
    while ((num > 0) && ((num % 2) == 0)) { num /= 2; denom /= 2; }
    if (num == 0) result = String.valueOf(whole); // example: one inch
    else if (whole == 0) result = num + "/" + denom; // example: 15/16
    else result = whole + "-" + num + "/" + denom; // example: 1-1/4
    return(result);
  }

} // end of MetricSaeSizes3 class

/* Copyright (c) 2022 by Keith Fenske.  Apache License or GNU GPL. */
