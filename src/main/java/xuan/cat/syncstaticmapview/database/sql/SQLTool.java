package xuan.cat.syncstaticmapview.database.sql;

import xuan.cat.syncstaticmapview.database.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.sql.builder.FieldStyle;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
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
    static <T> StringBuilder toStringFromArray(T[] list, ToStringFromListHandle<T> handle) {
        return toStringFromList(Arrays.asList(list), handle);
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




//    interface ToListHandle<T> {
//        T run(String part);
//    }
//    static <T> List<T> toList(String string, ToListHandle<T> handle) {
//        List<String>    split   = split(string, ',', StandardCharsets.UTF_8, 256);
//        List<T>         list    = new ArrayList<>(split.size());
//
//        for (String part : split)
//            list.add(handle.run(part));
//
//        return list;
//    }
//
//
//
//
//    static String[] toStringList(Enum<?>[] enums) {
//        String[] strings = new String[ enums.length ];
//        for (int i = 0 ; i < enums.length ; ++i)
//            strings[ i ] = enums[ i ].name();
//        return strings;
//    }




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
            return new StringBuilder().append('`').append(safetyField(field)).append('`').toString();
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
    /**
     * 解壓縮UUID
     * 交由SQL命令生成器使用即可,正常不用使用到此API
     * @param uuid UUID
     * @return 解壓縮後的UUID
     */
    private static UUID unzipUUID(String uuid) {
        return UUID.fromString(uuid.substring(0, 8) + '-' + uuid.substring(8, 12) + '-' + uuid.substring(12, 16) + '-' + uuid.substring(16, 20) + '-' + uuid.substring(20, 32));
    }




    /**
     * 分割字串
     * @param text 原始字串
     * @param breakpoint 斷點字符
     * @param charset 編碼方式
     * @param limit 切割限制
     * @return 已分割字串陣列
     */
    static List<String> split(String text, char breakpoint, Charset charset, int limit) {
        return split(text, breakpoint, charset, limit, (conversion) -> conversion);
    }
    static List<String> split(String text, char breakpoint, Charset charset, int limit, java.util.function.Function<String, String> conversion) {
        List<String>            list    = new ArrayList<>();
        int                     read;
        int                     sweep   = 0;
        char[]                  chars   = text.toCharArray();
        ByteArrayOutputStream output  = new ByteArrayOutputStream();
        while (sweep < chars.length && list.size() < limit) {
            if ((read = chars[sweep]) == breakpoint) {
                if (output.size() > 0) {
                    list.add(conversion.apply(output.toString(charset)));
                    output.reset();
                }
            } else {
                output.write(read);
            }
            sweep++;
        }
        if (output.size() > 0)
            list.add(conversion.apply(output.toString(charset)));
        return Collections.unmodifiableList(list);
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
