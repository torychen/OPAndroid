package org.jystudio.opandroid.database.service;

import java.util.List;
import java.util.Map;

public interface IDBService {

    /**
     * 设置每次查询最大返回数量，默认10.
     * @param count 数量
     * @return true or false
     */
    boolean setQueryResultCount(int count);

    /**
     * 获得数据库 表单版本，
     * 客户端据此判断是否需要同步。
     * @param tableName  表单名称
     * @return 返回的 当前数据库的记录数 和 最后 更新时间戳。
     */
    DatabaseTableVersion getTableVersion(String tableName);
    long getRecordCount(String tableName);
    String getLasModify(String tableName);

    /**
     * 服务器，通过这个函数找到，需要同步给 客户端的 所有记录
     *
     * @param tableName  表单名称
     * @param lastModify 这是客户端 已同步的最后一条记录。
     * @param count 默认同步 10 条记录。 避免数据太多。
     * @return 返回记录的集合。 失败则返回空。
     */
    List<Object> findRecordsByLastModify(String tableName, String lastModify, int count);

    List<Object> findRecordsByLastModify(String tableName, String lastModify);

    /**
     * 通过 sync flag 找出客户端 增加，修改过的记录
     * @param tableName  表单名称
     * @return 记录列表
     */
    List<Object> findLocalNewRecords(String tableName, int count);

    List<Object> findLocalNewRecords(String tableName);

    /**
     * 通过 id 查找单条记录
     * @param tableName the table name
     * @param id the id
     * @return null or the record
     */
    Object findRecordById(String tableName, long id);

    /**
     * 通过 sql 查找记录
     * @param tableName the table name
     * @param sql the raw sql
     * @return null or record list
     */
    List<Object> findRecords(String tableName, String sql);

    /**
     * 获得当前表单中 最大 id， 当 客户端 增加的 记录 id 和 服务器冲突时， 需要更新 客户端记录的id。
     * @param tableName  表单名称
     * @return 0-表示失败
     */
    long getMaxId(String tableName);

    /**
     * 把当前记录的 id 更新为 当前表单中最大 id + 1
     * 当 客户端 增加记录的 id 和 服务器冲突时， 需要更新 客户端记录的id
     * @param tableName  表单名称
     * @param orgId  记录初始id， 会被更新为 新的 最大 id
     * @return 成功或失败
     */
     boolean updateIdToNewMax(String tableName, long orgId);

    /***
     * sync2Server
     *
     * 将一条客户端 增加/修改 的记录，同步到服务器 数据库
     * 增加：
     * 插入一条新的记录到数据库。返回 id 和 lastmodify
     *
     * 修改：
     * 按照 id， 更新原有记录， 返回 id 和 lastmodify
     *
     * @param tableName  表单名称
     * @param record 一条记录
     * @return 返回 插入后的 id 和 lastModify。 本地记录 更新到 服务器后， id 和 lastModify 以服务器返回的为准。
     *  返回空，则表明操作失败
     */
    Map<String, Object> sync2Server(String tableName, Object record);

    /***
     * insert2Server
     *
     * 将一条记录（如 网页端生成的记录），插入到服务器 数据库
     * id， 由 服务器数据库自动生成。其它字段的内容不变。
     *
     * @param tableName  表单名称
     * @param record 一条记录
     * @return 返回 true false
     */
    boolean insert2Server(String tableName, Object record);


    /***
     * sync2Local
     *
     * 将一条服务器端 增加/修改 的记录，同步到 本地 数据库
     * 如果 id 有冲突，说明本地数据库的记录占用了 当前 id。
     * 把 本地数据库记录的 id 变更为新的最大 id。
     *
     *
     * @param tableName  表单名称
     * @param record 一条记录
     * @return 返回 true false
     */
    boolean sync2Local(String tableName, Object record);

    /***
     * 将一条本地记录（如手机客户端 创建的记录），插入到 本地 数据库
     *
     * 插入时，
     * 1. 需要将 id 设置为一个 和 服务器端 不容易 冲突的数。
     * 如服务器 id 从 1 开始增加。
     * 而 客户端从 一个大数  开始增加
     *
     * 2. 需要将 lastmodify 固定为一个假的较早的时间点，避免和服务器创建的记录冲突。
     *
     * @param tableName  表单名称
     * @param record 一条记录
     * @return 返回 true false
     */
    boolean insert2Local(String tableName, Object record);


    /**
     * del a record by id.
     * @param tableName the table name.
     * @param id the id
     * @return true or false
     */
    boolean delRecord(String tableName, long id);

    /**
     * update a record each field except the 'id'.
     * @param tableName table name
     * @param record the record
     * @return true or false
     */
    boolean updateRecord(String tableName, Object record);


    /**
     * insert a record directly.
     * @param tableName the table name
     * @param question  the record.
     * @return true or false
     */
    boolean insertRecord(String tableName, Question question);


}
