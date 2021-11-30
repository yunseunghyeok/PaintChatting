package doubledeltas.utils;

import java.util.Arrays;

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
    public boolean moveCursor(int d) { return setCursor(cur + d); }


    public String readInString(int count) {
    	cur += count;
    	return new String(Arrays.copyOfRange(bytes, cur-count, cur)).replaceAll("\0", "");
    }

    public int readInInteger(int count) {
        if (count < 0)
            return 0;
        if (count > 4) {
            cur += count - 4;
            return readInInteger(4);    // »óÀ§ ºñÆ® ÄÆ
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
    
    public void writeInteger(int n, boolean moveCursor) {
    	int res = 0;
    	bytes[cur + 0] = (byte)(n >> 24);
    	bytes[cur + 1] = (byte)(n >> 16);
    	bytes[cur + 2] = (byte)(n >> 8);
    	bytes[cur + 3] = (byte) n;
    	if (moveCursor) cur += 4;
    }
    public void writeInteger(int n) {
    	writeInteger(n, true);
    }
    
    
    public void writeString(String str, boolean moveCursor) {
    	byte[] bs = str.getBytes();
    	for (int i = 0; i < bs.length; i++)
    		bytes[cur + i] = bs[i];
    	if (moveCursor) cur += bs.length;
    }
    public void writeString(String str) {
    	writeString(str, true);
    }
}
