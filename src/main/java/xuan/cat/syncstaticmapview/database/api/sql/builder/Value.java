package xuan.cat.syncstaticmapview.database.api.sql.builder;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 一些特殊的數值
 */
public interface Value {
    /** 表示當前時間 */
    Date        NOW_DATE        = new NOW_DATE();
    class NOW_DATE extends Date {
    }
    class NOW_DATE_ADD extends Date {
        public final long add;

        public NOW_DATE_ADD(long add) {
            this.add = add;
        }
    }
    class NOW_DATE_REMOVE extends Date {
        public final long remove;

        public NOW_DATE_REMOVE(long remove) {
            this.remove = remove;
        }
    }



    class NOW_TIMESTAMP extends Timestamp {
        public NOW_TIMESTAMP() {
            super(NOW_DATE.getTime());
        }
    }
    class NOW_TIMESTAMP_ADD extends Timestamp {
        public final long add;

        public NOW_TIMESTAMP_ADD(long add) {
            super(0);
            this.add = add;
        }
    }
    class NOW_TIMESTAMP_REMOVE extends Timestamp {
        public final long remove;

        public NOW_TIMESTAMP_REMOVE(long remove) {
            super(0);
            this.remove = remove;
        }
    }



    class NOW_TIME extends Time {
        public NOW_TIME() {
            super(NOW_DATE.getTime());
        }
    }
    class NOW_TIME_ADD extends Time {
        public final long add;

        public NOW_TIME_ADD(long add) {
            super(0);
            this.add = add;
        }
    }
    class NOW_TIME_REMOVE extends Time {
        public final long remove;

        public NOW_TIME_REMOVE(long remove) {
            super(0);
            this.remove = remove;
        }
    }


}