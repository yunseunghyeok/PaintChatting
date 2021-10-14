package doubledeltas.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    /**
     * Logger는 객체를 생성할 수 없습니다. static 메소드를 호출해주세요.
     */
    private Logger() {} // object creation prevented

    /**
     * <code>stdout</code>에 현재 timestamp를 출력합니다.
     */
    public static void l() {
        l("");
    }

    /**
     * 서버 로그 한 줄을 timestamp와 함께 <code>stdout</code>에 출력합니다.
     * @param txt 출력할 텍스트
     */
    public static void l(String txt) {
        String formattedNow = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM/dd hh:mm:ss")
        );
        System.out.println("[" + formattedNow + "] " + txt);
    }

    /**
     * 서버 로그 여러 줄을 하나의 timestamp를 붙여 <code>stdout</code>에 출력합니다.
     * @param txts 출력할 텍스트 배열
     */
    public static void l(String[] txts) {
        String formattedNow = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM/dd hh:mm:ss")
        );
        System.out.println("[" + formattedNow + "] ");
        for(String s: txts) {
            System.out.println("\t" + s);
        }
    }
}
