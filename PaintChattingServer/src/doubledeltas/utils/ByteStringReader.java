package doubledeltas.utils;

import java.nio.charset.StandardCharsets;

public class ByteStringReader {
    private int cur = 0;
    private byte[] bytes;

    public ByteStringReader(byte[] bytes) {
        this.bytes = bytes;
    }

    public boolean setCursor(int i) {
        if (i < 0)
            return false;
        if (i >= bytes.length)
            return false;
        cur = i;
        return true;
    }

    public int getCursor() { return cur; }

    public byte[] read(int count) {
        int len = Math.min(count, bytes.length - cur);
        if (len < 1) return null;

        byte[] res = new byte[len];
        for(int j=0; j<len; j++)
            res[j] = bytes[cur+j];
        cur += len;
        return res;
    }

    public String readInString(int count) {
        return new String(read(count));
    }

    public int readInInteger(int count) {
        if (count < 0)
            return 0;
        if (count > 4) {
            cur += count - 4;
            return readInInteger(4);    // 상위 비트 컷
        }

        int res = 0;
        for (int i = 0; i < count; i++) {
            res <<= 8;
            res += (int)(bytes[cur] < 0 ? bytes[cur]+ 0x100 : bytes[cur]);
            cur++;
        }
        return res;
    }

    public int readInInteger() {
        return readInInteger(4);
    }
}
