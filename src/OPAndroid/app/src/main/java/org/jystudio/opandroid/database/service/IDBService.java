package org.jystudio.opandroid.database.service;

import java.util.List;
import java.util.Map;

public interface IDBService {

    /**
     * 获得数据库版本，
     * 客户端据此判断是否需要同步。
     * @return 返回的 当前数据库的记录数 和 最后 更新时间戳。
     */
    Map<String, Object> getDbVersion();
    int getRecordNum();
    String getLasModify();

    /**
     * 服务器，通过这个函数找到，需要同步给 客户端的 所有记录
     *
     * @param lastModify 这是客户端 已同步的最后一条记录。
     * @param count 默认同步 10 条记录。 避免数据太多。
     * @return 返回记录的集合。 失败则返回空。
     */
    List<Map<String,Object>> findRecordsByLastModify(String lastModify, int count);

    /**
     * 通过 sync flag 找出客户端 增加，修改过的记录
     * @return 记录列表
     */
    List<Map<String,Object>> findLocalNewRecords();

    Map<String, Object> findRecordById(int id);

    /**
     * 获得当前表单中 最大 id， 当 客户端 增加的 记录 id 和 服务器冲突时， 需要更新 客户端记录的id。
     * @return
     */
    int getMaxId();

    /**
     * 把当前记录的 id 更新为 当前表单中最大 id + 1
     * 当 客户端 增加记录的 id 和 服务器冲突时， 需要更新 客户端记录的id
     * @param id
     * @return 成功或失败
     */
     boolean updateIdToMax(int id);

    /***
     * 将一条客户端增加/修改的记录，同步到服务器 数据库
     * @param record
     * @return 返回 插入后的 id 和 lastModify。 本地记录 更新到 服务器后， id 和 lastModify 以服务器返回的为准。
     *  返回空，则表明插入失败
     */
    Map<String, Object> sync2ServerDb(Object record);

    /***
     * 将一条记录（如 网页端生成的记录），插入到服务器 数据库
     * 直接插入即可
     * @param record
     * @return 返回 true false
     */
    boolean insert2ServerDb(Object record);


    /***
     * 将一条服务器端增加/修改的记录，同步到 本地 数据库
     * @param record
     * @return 返回 true false
     */
    boolean sync2Local(Object record);

    /***
     * 将一条本地记录（如手机客户端 创建的记录），插入到 本地 数据库
     * 插入时，需要将 id 设置为一个 和 服务器端 不容易 冲突的数。 如服务器 id 从 1 开始增加。
     * 而 客户端从 90000000 开始增加
     * @param record
     * @return 返回 true false
     */
    boolean insert2Local(Object record);


    boolean delRecord(Object record);


}