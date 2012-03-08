/*************************************************************************
 *  Kompilacja:  javac Date.java
 *  Wykonanie:    java Date
 *
 *  Niezmienny typ danych dla dat.
 *
 *************************************************************************/

public class Date implements Comparable<Date> {
    private static final int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private final int month;   // Miesi¹c (od 1 do 12)
    private final int day;     // Dzieñ   (od 1 do DAYS[month]
    private final int year;    // Rok

    // Sprawdzanie ograniczeñ w celu upewnienia siê, ¿e obiekt reprezentuje prawid³ow¹ datê
    public Date(int month, int day, int year) {
        if (!isValid(month, day, year)) throw new RuntimeException("Nieprawid³owa data");
        this.month = month;
        this.day   = day;
        this.year  = year;
    }

    // Tworzenie nowej daty przez przetwarzanie ³añcucha znaków w postaci mm/dd/rr
    public Date(String date) {
        String[] fields = date.split("/");
        if (fields.length != 3) {
            throw new RuntimeException("B³¹d przetwarzania daty");
        }
        month = Integer.parseInt(fields[0]);
        day   = Integer.parseInt(fields[1]);
        year  = Integer.parseInt(fields[2]);
        if (!isValid(month, day, year)) throw new RuntimeException("Nieprawid³owa data");
    }

    public int month() { return month; }
    public int day()   { return day;   }
    public int year()  { return year;  }


    // Czy dana data jest prawid³owa?
    private static boolean isValid(int m, int d, int y) {
        if (m < 1 || m > 12)      return false;
        if (d < 1 || d > DAYS[m]) return false;
        if (m == 2 && d == 29 && !isLeapYear(y)) return false;
        return true;
    }

    // Czy y to rok przestêpny?
    private static boolean isLeapYear(int y) {
        if (y % 400 == 0) return true;
        if (y % 100 == 0) return false;
        return (y % 4 == 0);
    }

    // Zwracanie daty nastêpnego dnia
    public Date next() {
        if (isValid(month, day + 1, year))    return new Date(month, day + 1, year);
        else if (isValid(month + 1, 1, year)) return new Date(month + 1, 1, year);
        else                                  return new Date(1, 1, year + 1);
    }

    // Czy dany obiekt Date reprezentuje datê póŸniejsz¹ ni¿ b?
    public boolean isAfter(Date b) {
        return compareTo(b) > 0;
    }

    // Czy dany obiekt Date reprezentuje datê wczeœniejsz¹ ni¿ b?
    public boolean isBefore(Date b) {
        return compareTo(b) < 0;
    }

    // Porównanie danego obiektu Date z obiektem that
    public int compareTo(Date that) {
        if (this.year  < that.year)  return -1;
        if (this.year  > that.year)  return +1;
        if (this.month < that.month) return -1;
        if (this.month > that.month) return +1;
        if (this.day   < that.day)   return -1;
        if (this.day   > that.day)   return +1;
        return 0;
    }

    // Zwracanie ³añcucha znaków reprezentuj¹cego dan¹ datê
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    // Czy dany obiekt Date jest równy x?
    public boolean equals(Object x) {
        if (x == this) return true;
        if (x == null) return false;
        if (x.getClass() != this.getClass()) return false;
        Date that = (Date) x;
        return (this.month == that.month) && (this.day == that.day) && (this.year == that.year);
    }

    // Przyk³adowy klient testowy
    public static void main(String[] args) {
        Date today = new Date(2, 25, 2004);
        StdOut.println(today);
        for (int i = 0; i < 10; i++) {
            today = today.next();
            StdOut.println(today);
        }

        StdOut.println(today.isAfter(today.next()));
        StdOut.println(today.isAfter(today));
        StdOut.println(today.next().isAfter(today));

        Date birthday = new Date(10, 16, 1971);
        StdOut.println(birthday);
        for (int i = 0; i < 10; i++) {
            birthday = birthday.next();
            StdOut.println(birthday);
        }
    }
}
