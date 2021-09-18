package xuan.cat.databasecatmini.api.sql.builder;

public interface Order {
    Order clone();


    /**
     * 遞增
     * 小到大
     * 少到多
     * 舊到新
     * @param field 欄位
     * @return 排序
     */
    <T> Order increment(Field<T> field);


    /**
     * 遞減
     * 大到小
     * 多到少
     * 新到舊
     * @param field 欄位
     * @return 排序
     */
    <T> Order decrement(Field<T> field);
}
