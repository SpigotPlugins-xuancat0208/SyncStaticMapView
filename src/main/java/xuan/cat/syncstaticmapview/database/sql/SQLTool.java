package xuan.cat.syncstaticmapview.database.sql;

import xuan.cat.syncstaticmapview.database.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.sql.builder.FieldStyle;

import java.util.*;

public interface SQLTool {


    /**
     * 複製陣列
     * @param origin 原始清單
     * @return 克隆清單
     */
    static <T> Collection<T> copyList(Collection<T> origin) {
        Collection<T> clone;
        if (origin instanceof LinkedHashSet) {
            clone = new LinkedHashSet<>( origin.size() );
        } else if (origin instanceof List) {
            clone = new ArrayList<>( origin.size() );
        } else {
            clone = new HashSet<>( origin.size() );
        }
        for (T t : origin)
            clone.add(tryClone(t));
        return clone;
    }



    static <T> T[] copyArray(T[] origin) {
        return Arrays.copyOf(origin, origin.length);
    }


    /**
     * 複製地圖組
     * @param origin 原始地圖組
     * @return 克隆地圖組
     */
    static <T, V> Map<T, V> copyMap(Map<T, V> origin) {
        Map<T, V> clone = origin instanceof LinkedHashMap ? new LinkedHashMap<>( origin.size() ) : new HashMap<>( origin.size() );
        origin.forEach((key, value) -> {
            clone.put(key, tryClone(value));
        });
        return clone;
    }




    static <T> T copySQLPart(T o) {
        return o instanceof SQLPart ? (T) ((SQLPart) o).clone() : o;
    }
    static <T> T copySQLBuilder(T o) {
        return o instanceof SQLCommand ? (T) ((SQLCommand) o).clone() : o;
    }




    interface ToStringFromListHandle<T> {
        void run(StringBuilder merge, T part);
    }
    static <T> StringBuilder toStringFromList(Collection<T> list, ToStringFromListHandle<T> handle) {
        StringBuilder builder = new StringBuilder();
        list.forEach((l) -> {
            if (builder.length() > 0)
                builder.append(',');   // 逗號

            handle.run(builder, l);
        });
        return builder;
    }

    interface ToStringFromMapHandle<K, V> {
        void run(StringBuilder merge, K key, V value);
    }
    static <K, V> StringBuilder toStringFromMap(Map<K, V> map, ToStringFromMapHandle<K, V> handle) {
        StringBuilder builder = new StringBuilder();
        map.forEach(((k, v) -> {
            if (builder.length() > 0)
                builder.append(',');   // 逗號

            handle.run(builder, k, v);
        }));
        return builder;
    }



    /**
     * 組合字符串, 每一個字服串中間插入空格
     * @param strings 字符串陣列
     * @return 組合的結果
     */
    static StringBuilder combination(CharSequence... strings) {
        StringBuilder builder = new StringBuilder();

        for (CharSequence s : strings) {
            if (builder.length() > 0)
                builder.append(' ');

            builder.append(s);
        }

        return builder;
    }


    /**
     * 括號
     * @param string 原始自服串
     * @return (原始字符串)
     */
    static StringBuilder brackets(CharSequence string) {
        return new StringBuilder().append('(').append(string).append(')');
    }







    static String toField(String field) {
        if (field == null) {
            return "NULL";
        } else {
            return '`' + safetyField(field) + '`';
        }
    }
    /**
     * SQL欄位字符串,安全處裡
     * 交由SQL命令生成器使用即可,正常不用使用到此API
     * `=``
     * null=NULL
     * @param field 欄位
     * @return 安全修改過的值
     */
    static String safetyField(String field) {
        return field.replace("`","``");
    }





    static <T> String toValue(Field<T> field, T value) {
        if (value == null)
            return "NULL";
        return toValue(field.style(), value);
    }
    static <T> String toValue(FieldStyle<T> style, T value) {
        return style.str(value);
    }
    /**
     * SQL資料字符串,安全處裡
     * 交由SQL命令生成器使用即可,正常不用使用到此API
     * \=\\
     * '=\'
     * "=\"
     * null=NULL
     * @param value 值
     * @return 安全修改過並加括號的值
     */
    static String safetyValue(String value) {
        value = value.replace("\\", "\\\\");
        value = value.replace("'",  "\\'");
        return value;
    }




    /**
     * 壓縮UUID
     * 交由SQL命令生成器使用即可,正常不用使用到此API
     * @param uuid UUID
     * @return 壓縮後的UUID
     */
    static StringBuilder zipUUID(UUID uuid) {
        String s = uuid.toString();
        // 去掉 - 並返回
        return new StringBuilder().append(s,0,8).append(s,9,13).append(s,14,18).append(s,19,23).append(s, 24, 36);
    }


    static <T> T[] tryClone(T[] t) {
        return t == null ? null : copyArray(t);
    }
    static <T> T tryClone(T t) {
        if (t == null) {
            return null;
        } else if (t instanceof Map<?, ?>) {
            return (T) copyMap((Map<?, ?>) t);
        } else if (t instanceof Set<?>) {
            return (T) copyList((Set<?>) t);
        } else if (t instanceof SQLPart) {
            return copySQLPart(t);
        } else if (t instanceof SQLCommand) {
            return copySQLBuilder(t);
        } else {
            return t;
        }
    }
}
