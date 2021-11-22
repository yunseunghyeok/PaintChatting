package doubledeltas.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    /**
     * Logger�� ��ü�� ������ �� �����ϴ�. static �޼ҵ带 ȣ�����ּ���.
     */
    private Logger() {} // object creation prevented

    /**
     * <code>stdout</code>�� ���� timestamp�� ����մϴ�.
     */
    public static void l() {
        l("");
    }

    /**
     * ���� �α� �� ���� timestamp�� �Բ� <code>stdout</code>�� ����մϴ�.
     * @param txt ����� �ؽ�Ʈ
     */
    public static void l(String txt) {
        String formattedNow = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("MM/dd hh:mm:ss")
        );
        System.out.println("[" + formattedNow + "] " + txt);
    }

    /**
     * ���� �α� ���� ���� �ϳ��� timestamp�� �ٿ� <code>stdout</code>�� ����մϴ�.
     * @param txts ����� �ؽ�Ʈ �迭
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
