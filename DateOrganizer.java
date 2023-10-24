package dateorganizer;

/**
 * A testbed for a binary heap implementation of a priority queue using 
 * various comparators to sort Gregorian dates
 * @author Duncan, Matthew Benfield
 * @see Date, PQueueAPI, PQueue
 * <pre>
 * Date: 9-24-23
 * Course: csc 3102
 * File: DateOrganizer.java
 * Instructor: Dr. Duncan
 * </pre>
 */

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DateOrganizer {
    /**
     * Gives the integer value equivalent to the day of the
     * week of the specified date
     * 
     * @param d a date on the Gregorian Calendar
     * @return 0->Sunday, 1->Monday, 2->Tuesday, 3->Wednesday,
     *         4->Thursday, 5->Friday, 6->Saturday; otherwise, -1
     */
    public static int getDayNum(Date d) {
        String dayOfWeek = d.getDayOfWeek();

        switch (dayOfWeek) {
            case "Sunday":
                return 0;
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
            case "Saturday":
                return 6;
            default:
                return -1;
        }
    }

    public static void main(String[] args) throws IOException, PQueueException {
        String usage = "DateOrganizer <date-file-name> <sort-code>%n";
        usage += "sort-code: -2 -month-day-year%n";
        usage += "           -1 -year-month-day%n";
        usage += "            0 +weekDayNumber+monthName+day+year%n";
        usage += "            1 +year+month+day%n";
        usage += "            2 +month+day+year";
        if (args.length != 2) {
            System.out.println("Invalid number of command line arguments");
            System.out.printf(usage + "%n");
            System.exit(1);
        }

        String fileName = args[0];

        int sortCode = Integer.parseInt(args[1]);

        Comparator<Date> comparator = null;
        switch (sortCode) {
            case -2:
                comparator = Comparator.comparing(Date::getMonth)
                        .thenComparing(Date::getDay)
                        .thenComparing(Date::getYear);
                break;
            case -1:
                comparator = Comparator.comparing(Date::getYear)
                        .thenComparing(Date::getMonth)
                        .thenComparing(Date::getDay);
                break;
            case 0:
                comparator = Comparator.comparingInt(DateOrganizer::getDayNum)
                        .thenComparing(Date::getMonthName)
                        .thenComparing(Date::getDay)
                        .thenComparing(Date::getYear);
                break;
            case 1:
                comparator = Comparator.comparing(Date::getYear)
                        .thenComparing(Date::getMonth)
                        .thenComparing(Date::getDay);
                break;
            case 2:
                comparator = Comparator.comparing(Date::getMonth)
                        .thenComparing(Date::getDay)
                        .thenComparing(Date::getYear);
                break;
            default:
                System.out.println("Invalid sort code");
                System.exit(1);
        }

        if (sortCode == -2 || sortCode == -1) {
            comparator = comparator.reversed();
        }

        Scanner scanner = new Scanner(new FileReader(fileName));

        PQueue<Date> dateQueue = new PQueue<>(comparator);

        while (scanner.hasNext()) {

            String line;

            while (scanner.hasNext()) {
                line = scanner.next();

                String[] dateParts = line.split("/");

                int month = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                Date date = new Date(month, day, year);

                dateQueue.insert(date);
            }
        }

        scanner.close();

        System.out.println(dateQueue);

        Map<Integer, String> sortCodeNames = new HashMap<>();
        sortCodeNames.put(-2, "month-day-year");
        sortCodeNames.put(-1, "year-month-day");
        sortCodeNames.put(0, "+weekDayNumber+monthName+day+year");
        sortCodeNames.put(1, "+year+month+day");
        sortCodeNames.put(2, "+month+day+year");

        System.out.println("_______________________________________________");
        System.out.println("Dates from " + fileName + " in " + sortCodeNames.get(sortCode) + " Order:");

        while (!dateQueue.isEmpty()) {
            Date date = dateQueue.remove();
            System.out.println(
                    date.getDayOfWeek() + ", " + date.getMonthName() + " " + date.getDay() + ", " + date.getYear());
        }

        System.out.println("_______________________________________________");

    }
}
