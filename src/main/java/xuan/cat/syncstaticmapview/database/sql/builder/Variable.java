package xuan.cat.syncstaticmapview.database.sql.builder;

import java.util.Locale;

/**
 * SQL變量
 */
public abstract class Variable {

    public abstract StringBuilder part();

    /**
     * 用於存16進制自符串
     */
    private static final char[] CARRY_16_LIST = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static class UNHEX extends Variable {
        private final char[] chars;

        public UNHEX(String value) {
            this.chars = value.toUpperCase(Locale.ROOT).toCharArray();
        }
        public UNHEX(byte[] value) {
             chars = new char[value.length << 1];
            for (int i = 0 ; i < value.length ; ++i) {
                chars[(i << 1)]     = CARRY_16_LIST[(value[i] & 0xF0) >> 4];
                chars[(i << 1) + 1] = CARRY_16_LIST[(value[i] & 0x0F)     ];
            }
        }

        public StringBuilder part() {
            return new StringBuilder(chars.length + 9).append("UNHEX('").append(chars).append("')");
        }
    }
}
