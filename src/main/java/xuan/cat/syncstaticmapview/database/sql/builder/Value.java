package xuan.cat.syncstaticmapview.database.sql.builder;

import java.sql.Time;
import java.util.Date;

/**
 * 一些特殊的數值
 */
public interface Value {
    /** 表示當前時間 */
    Date        NOW_DATE        = new NOW_DATE();
    class NOW_DATE extends Date {
    }

    class NOW_TIME_ADD extends Time {
        public final long add;

        public NOW_TIME_ADD(long add) {
            super(0);
            this.add = add;
        }
    }
}