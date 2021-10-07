package doubledeltas.constants;

public class Environment {
	/**
	 * MySQL의 기본값 포트.
	 */
    public static short MYSQL_DEFAULT_PORT = 3306;
	/**
	 * 클라이언트에서 서버로 전송하는 패킷의 포트
	 */
    public static short CLIENT_TO_SERVER_PORT = 50343; // 0xC4A7 for "CHAT"
	/**
	 * 서버에서 클라이언트로 전송하는 패킷의 포트
	 */
    public static short SERVER_TO_CLIENT_PORT = 50344;
}
