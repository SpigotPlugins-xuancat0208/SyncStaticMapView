package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.builder.Variable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * SQL變量
 */
public abstract class CodeVariable implements Variable {

    public abstract StringBuilder part();

    /**
     * 取得現在時間
     * yyyy-MM-dd HH:mm:ss
     */
    public static class TIME extends CodeVariable implements Variable.TIME {
       public StringBuilder part() {
            return new StringBuilder().append('\'').append(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))).append('\'');
        }
    }

    /**
     * 用於存16進制自符串
     */
    private static final char[] CARRY_16_LIST = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final static byte getIndexFromChar(char c) {
        for (byte i = 0 ; i < CARRY_16_LIST.length ; ++i)
            if (CARRY_16_LIST[i] == c)
                return i;
        return -1;
    }
    public static class UNHEX extends CodeVariable implements Variable.UNHEX {
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
    /**
     * 用於解碼16進制自符串
     */
    public static byte[] HEX(String value) {
        char[] chars    = value.toUpperCase(Locale.ROOT).toCharArray();
        byte[] builder  = new byte[value.length() >> 1];

        byte high;
        byte low;

        for (int i = 0 ; i < builder.length ; ++i) {
            high        = (byte) (getIndexFromChar(chars[(i << 1)    ]) << 4);
            low         = (byte) (getIndexFromChar(chars[(i << 1) + 1])     );
            builder[i]  = (byte) (high | low);
        }

        return builder;
    }
}
