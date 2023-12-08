package com.mubaracktahir.jpos.demo;

public class Utils {
    public static byte[] hexStringToBytes(String s) {
        byte[] ret;

        if (s == null) return null;

        int sz = s.length();

        char c;
        for (int i = 0; i < sz; ++i) {
            c = s.charAt(i);
            if (!((c >= '0') && (c <= '9'))
                    && !((c >= 'A') && (c <= 'F'))
                    && !((c >= 'a') && (c <= 'f'))) {
                s = s.replaceAll("[^[0-9][A-F][a-f]]", "");
                sz = s.length();
                break;
            }
        }

        ret = new byte[sz / 2];

        for (int i = 0; i < sz; i += 2) {
            ret[i / 2] = (byte) ((hexCharToInt(s.charAt(i)) << 4)
                    | hexCharToInt(s.charAt(i + 1)));
        }

        return ret;
    }

    static int hexCharToInt(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 10);

        throw new RuntimeException("invalid hex char '" + c + "'");
    }
    public static String bytesToHexString(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
