package mil.army.a1div.jinu.a625app;

public class Soldier {
    private String name;
    private Rank rank;
    private Sosok sosok;

    private int year; // If this value is 0, means that there is no date data.
    private int month;
    private int day;

    // private String date; // 순서 비교하기 위한 원본 날짜 값 // --> 미사용


    public Soldier() {
        //maybe we make unknown soldier?
        // We are the world!!
    }

    public String toString() {
        String answer = "";
        answer += sosok;
        answer += ',';
        answer += name;
        answer += ',';
        answer += rank;
        answer += ',';
        answer += year;
        if (month < 10) {
            answer += '0';
        }
        answer += month;
        if (day < 10) {
            answer += '0';
        }
        answer += day;
        return answer;
    }

    public String nameRank() {
        String answer = "";
        answer += rank;
        answer += ' ';
        answer += name;
        return answer;
    }

    public Soldier(String sosok, String name, Rank rank, int year, int month, int day) {
        if(sosok.equals("제11연대"))
            this.sosok = Sosok.제11연대;
        else if(sosok.equals("제12연대"))
            this.sosok = Sosok.제12연대;
        else if(sosok.equals("제15연대"))
            this.sosok = Sosok.제15연대;
        else if(sosok.equals("사령부"))
            this.sosok = Sosok.사령부;

        this.name = name;
        this.rank = rank;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public Rank getRank() {
        return rank;
    }

    public Sosok getSosok() {
        return sosok;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}