package com.automonia.dialect;


import com.automonia.dialect.annotation.entity.WTField;
import com.automonia.dialect.annotation.query.WTSearch;
import com.automonia.dialect.annotation.query.WTSearchType;
import com.automonia.dialect.model.WTModel;
import com.automonia.dialect.query.WTOrder;
import com.automonia.dialect.query.WTQuery;
import com.automonia.tools.DateUtils;
import com.automonia.tools.StringUtils;
import com.automonia.tools.exception.ParameterIsNullException;
import com.automonia.tools.exception.WTException;
import com.automonia.tools.model.WTEnum;
import com.automonia.tools.tuple.Tuple;
import com.automonia.tools.tuple.TwoTuple;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者 温腾
 * @创建时间 2018年02月13日 上午10:25
 */
public enum DataBaseFactory {

    singleton;


    private EntityClassInfoFactory entityClassInfoFactory = EntityClassInfoFactory.singleton;

    /**
     * 储存实体类到相关信息
     */


    /**
     * 生成mysql的select items内容的数据库sql语句。
     * <p>
     * 再引入redis对实体类和模型类对属性到数据库字段进行存储，以加快生成sql的速度。
     *
     * @param fieldNames 指定了select获取的属性集合。如果为空，那么获取所有的属性
     * @param query      指定了select的获取范围
     * @param modelClass 指定了查询结构的封装目标类,即数据模型类(目前仅支持两级的继承)
     * @param <M>
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     */
    public <M extends WTModel> String getSqlOfSelectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, WTException {
        if (modelClass == null) {
            throw null;
        }

        StringBuilder sqlValue = new StringBuilder("select ");
        Class entityClass = getEntityClassInfoFactory().getEntityClass(modelClass);
        String entitySimpleName = entityClass.getSimpleName();

        StringBuilder sqlOfLeftJoinItems = new StringBuilder();
        Boolean selectItemsIsEmpty = fieldNames == null || fieldNames.isEmpty();

        /*
        获取查询语句的select内容，
        从entityClass获取的是数据库映射属性的查询内容
        从modelClass获取的是数据库扩展属性的关联查询内容
         */
        for (String fieldName : getEntityClassInfoFactory().getFieldCollection(entitySimpleName)) {
            // 是否将entityClass类的属性用于生成select item内容的前提条件是：fieldNames为空 或者fieldNames包括了该属性
            if (selectItemsIsEmpty || fieldNames.contains(fieldName)) {
                sqlValue.append(entitySimpleName).append(".").append(getEntityClassInfoFactory().getColumnName(entitySimpleName, fieldName)).append(" ").append(fieldName).append(",");
            }
        }

        /*
        modelClass是实体扩展类
         */
        if (modelClass != entityClass) {
            for (Field fieldOfModelClass : modelClass.getDeclaredFields()) {
                // 是否将modelClass类的属性用于生成select item内容的前提条件是：fieldNames为空 或者fieldNames包括了该属性
                if (selectItemsIsEmpty || fieldNames.contains(fieldOfModelClass.getName())) {
                    WTField wtFieldAnnotation = fieldOfModelClass.getAnnotation(WTField.class);
                    // 对于没有使用WTField注解的属性，不考虑用于生成sql语句
                    if (wtFieldAnnotation == null) {
                        continue;
                    }
                    String fieldEntityName = StringUtils.singleton.isEmpty(wtFieldAnnotation.entityName()) ? entitySimpleName : wtFieldAnnotation.entityName();
                    String fieldName = StringUtils.singleton.isEmpty(wtFieldAnnotation.fieldName()) ? fieldOfModelClass.getName() : wtFieldAnnotation.fieldName();
                    // 如果使用的三级表关联，那么第三级表的别名由entityName + midJoinOn决定，如果使用的二级表关联，那么第二级表的别名由entityName + fromOn决定
                    String entityNameFronOn = StringUtils.singleton.isEmpty(wtFieldAnnotation.midEntityName()) ? wtFieldAnnotation.fromOn() : wtFieldAnnotation.midJoinOn();

                    sqlValue.append(fieldEntityName).append(entityNameFronOn).append(".").append(getEntityClassInfoFactory().getColumnName(fieldEntityName, fieldName)).append(" ").append(fieldOfModelClass.getName()).append(",");

                    // 记录使用到的第三方关联表
                    getLeftJoinItemSql(wtFieldAnnotation, entitySimpleName, sqlOfLeftJoinItems);
                }
            }
        }
        sqlValue.deleteCharAt(sqlValue.length() - 1);

        /*
        追加from语句信息，追加数据模型类中涉及到的关联语句
         */
        sqlValue.append(" from ").append(getEntityClassInfoFactory().getTableName(entitySimpleName)).append(" ").append(entitySimpleName);

        /*
        获取where，group by， order by语句，和其涉及到的关联语句
        first: where， group by, order by语句
        second: 关联语句
         */
        TwoTuple whereAndLeftJoinItemsSql = getSqlWhereItems(query, entityClass, sqlOfLeftJoinItems, true, true, true);

        return sqlValue.append(whereAndLeftJoinItemsSql.second).append(whereAndLeftJoinItemsSql.first).toString();
    }

    public <M extends WTModel> String getSqlOfSelectList(WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, WTException {
        return getSqlOfSelectList(null, query, modelClass);
    }


    public <M extends WTModel> String getSqlOfSelectPage(WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, WTException {
        return getSqlOfSelectPage(null, query, modelClass);
    }

    public <M extends WTModel> String getSqlOfSelectPage(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, WTException {
        return getSqlOfSelectList(fieldNames, query, modelClass) + getLimitSql(query);
    }

    public <M extends WTModel> String getSqlOfSelectOne(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        return getSqlOfSelectOne(null, query, modelClass);
    }

    public <M extends WTModel> String getSqlOfSelectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        return getSqlOfSelectList(fieldNames, query, modelClass) + " limit 1";
    }

    public <M extends WTModel> String getSqlOfSelectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        return "select count(*)" + getSqlOfFromWhereGroupOrder(query, modelClass);
    }

    public String getSqlOfInsert(WTModel model) throws NoSuchFieldException, WTException, ClassNotFoundException {
        if (model == null) {
            throw null;
        }
        Class<? extends WTModel> entityClass = getEntityClassInfoFactory().getEntityClass(model.getClass());
        String entitySimpleName = entityClass.getSimpleName();

        StringBuilder sql = new StringBuilder("insert into ").append(getEntityClassInfoFactory().getTableName(entitySimpleName));

        StringBuilder insertIntoColumnItemSql = new StringBuilder();
        StringBuilder insertIntoValueItemSql = new StringBuilder();

        for (String fieldItem : getEntityClassInfoFactory().getFieldCollection(entitySimpleName)) {
            TwoTuple<Object, Class> valueInfo = getFieldValue(model, fieldItem);
            if (valueInfo == null || valueInfo.first == null) {
                continue;
            }

            insertIntoColumnItemSql.append(",").append(getEntityClassInfoFactory().getColumnName(entitySimpleName, fieldItem));
            insertIntoValueItemSql.append(",").append(getInsertValueString(valueInfo));
        }

        return sql.append(" (").append(insertIntoColumnItemSql.substring(1)).append(") values (").append(insertIntoValueItemSql.substring(1)).append(")").toString();
    }

    public String getSqlOfUpdate(WTQuery query, WTModel model) throws NoSuchFieldException, ClassNotFoundException, WTException {
        if (model == null) {
            throw null;
        }
        Class entityClass = getEntityClassInfoFactory().getEntityClass(model.getClass());
        String entitySimpleName = entityClass.getSimpleName();

        StringBuilder sqlValue = new StringBuilder("update ").append(getEntityClassInfoFactory().getTableName(entitySimpleName)).append(" ").append(entityClass.getSimpleName());

        TwoTuple whereAndLeftJoinItemsSql = getSqlWhereItems(query, entityClass, null, true, false, false);
        sqlValue.append(whereAndLeftJoinItemsSql.second);
        sqlValue.append(" set ");

        for (String fieldItem : getEntityClassInfoFactory().getFieldCollection(entitySimpleName)) {
            TwoTuple<Object, Class> valueInfo = getFieldValue(model, fieldItem);
            if (valueInfo == null || valueInfo.first == null) {
                continue;
            }
            sqlValue.append(entityClass.getSimpleName()).append(".").append(getEntityClassInfoFactory().getColumnName(entitySimpleName, fieldItem)).append("=").append(getInsertValueString(valueInfo)).append(",");
        }
        sqlValue.deleteCharAt(sqlValue.length() - 1);
        sqlValue.append(whereAndLeftJoinItemsSql.first);

        return sqlValue.toString();
    }

    public <M extends WTModel> String getSqlOfDelete(WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, WTException {
        return "delete " + getEntityClassInfoFactory().getEntityClass(modelClass).getSimpleName() + " " + getSqlOfFromWhereGroupOrder(query, modelClass);
    }


    ////////////////////////////////////

    /**
     * 获取sql的limit限制查询数量语句
     *
     * @param query 查询对象
     * @return limit语句
     */
    private String getLimitSql(WTQuery query) {
        Integer pageNo = query.getPageNo() == null ? 1 : query.getPageNo();
        return " limit " + (pageNo - 1) * query.getPageSize() + ", " + query.getPageSize();
    }

    /**
     * 从@Search注解中获取关联语句
     *
     * @param searchAnnotation @Search注解
     * @return
     */
    private void getLeftJoinItemSql(WTSearch searchAnnotation, String entityName, StringBuilder leftJoinSqlValue) throws ClassNotFoundException, WTException {
        if (searchAnnotation == null || StringUtils.singleton.isEmpty(entityName)) {
            throw new ParameterIsNullException("@WTSearch注解对象为空, 实体类名为空");
        }
        getLeftJoinItemSql(leftJoinSqlValue, entityName, searchAnnotation.entityName(), searchAnnotation.fromOn(), searchAnnotation.joinOn(), searchAnnotation.midEntityName(), searchAnnotation.midFromOn(), searchAnnotation.midJoinOn());
    }

    private void getLeftJoinItemSql(WTField fieldAnnotation, String entityName, StringBuilder leftJoinSqlValue) throws WTException, ClassNotFoundException {
        if (fieldAnnotation == null || StringUtils.singleton.isEmpty(entityName)) {
            throw new ParameterIsNullException("@WTField注解对象为空, 实体类名为空");
        }
        getLeftJoinItemSql(leftJoinSqlValue, entityName, fieldAnnotation.entityName(), fieldAnnotation.fromOn(), fieldAnnotation.joinOn(), fieldAnnotation.midEntityName(), fieldAnnotation.midFromOn(), fieldAnnotation.midJoinOn());
    }

    /**
     * 处理left join关联关系
     * 可以同时为WTField， @WTSearch提供服务
     * <p>
     * 参数内容参考@WTSearch类信息说明
     *
     * @param entitySimpleName
     * @param entityName
     * @param fromOn
     * @param joinOn
     * @param midEntityName
     * @param midFromOn
     * @param midJoinOn
     * @return
     */
    private void getLeftJoinItemSql(StringBuilder leftJoinSqlValue, String entitySimpleName, String entityName, String fromOn, String joinOn, String midEntityName, String midFromOn, String midJoinOn) throws WTException, ClassNotFoundException {
        if (StringUtils.singleton.isEmpty(entityName)) {
            return;
        }

        String entitySimpleNameAlias = entitySimpleName;

        // 是否关联的前提是该关联关系未加入过

        // 三级关联
        if (!StringUtils.singleton.isEmpty(midEntityName)) {
            String midEnittyNameAslias = midEntityName + fromOn;
            if (leftJoinSqlValue.indexOf(midEntityName + fromOn) < 0) {
                leftJoinSqlValue.append(" left join ").append(getEntityClassInfoFactory().getTableName(midEntityName))
                        .append(" ")
                        .append(midEnittyNameAslias)
                        .append(" on ")
                        .append(entitySimpleNameAlias)
                        .append(".")
                        .append(getEntityClassInfoFactory().getColumnName(entitySimpleName, fromOn))
                        .append("=")
                        .append(midEnittyNameAslias)
                        .append(".")
                        .append(getEntityClassInfoFactory().getColumnName(midEntityName, midFromOn));
            }
            // 保持匹配格式对应得上
            entitySimpleName = midEntityName;
            fromOn = midJoinOn;
            entitySimpleNameAlias = midEnittyNameAslias;
        }

        if (leftJoinSqlValue.indexOf(entityName + fromOn) < 0) {
            // 都需要处理的二级查询
            leftJoinSqlValue.append(" left join ")
                    .append(getEntityClassInfoFactory().getTableName(entityName))
                    .append(" ")
                    .append(entityName).append(fromOn)
                    .append(" on ")
                    .append(entitySimpleNameAlias)
                    .append(".")
                    .append(getEntityClassInfoFactory().getColumnName(entitySimpleName, fromOn))
                    .append("=")
                    .append(entityName).append(fromOn)
                    .append(".")
                    .append(getEntityClassInfoFactory().getColumnName(entityName, joinOn));
        }
    }


    /**
     * 考虑到统计数量的sql， 删除语句的sql 有共同指出(除了from之前的内容)。故提取from后面的逻辑
     *
     * @param <M>
     * @param query      查询对象
     * @param modelClass 数据模型对象
     * @return
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     */
    private <M extends WTModel> String getSqlOfFromWhereGroupOrder(WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, WTException {
        if (modelClass == null) {
            throw new ParameterIsNullException("数据模型对象未指定，无法生成sql");
        }
        Class entityClass = getEntityClassInfoFactory().getEntityClass(modelClass);
        String entitySimpleName = entityClass.getSimpleName();
        StringBuilder sqlValue = new StringBuilder(" from ").append(getEntityClassInfoFactory().getTableName(entitySimpleName)).append(" ").append(entityClass.getSimpleName());

        // 获取查询条件语句，并获取其中涉及到的关联语句
        TwoTuple whereAndLeftJoinItemsSql = getSqlWhereItems(query, entityClass, null, true, false, false);

        sqlValue.append(whereAndLeftJoinItemsSql.second).append(whereAndLeftJoinItemsSql.first);

        return sqlValue.toString();
    }

    /**
     * 获取query对象对应的sql的查找过滤条件
     *
     * @param query 查询对象
     * @return
     */
    private <E extends WTModel> TwoTuple<String, String> getSqlWhereItems(WTQuery query, Class<E> entityClass, StringBuilder sqlOfLeftJoinItems, Boolean generateLeftJoinSql, Boolean checkGroupBy, Boolean checkOrderBy) throws NoSuchFieldException, ClassNotFoundException, WTException {
        if (query == null) {
            return Tuple.tuple("", "");
        }
        if (sqlOfLeftJoinItems == null) {
            sqlOfLeftJoinItems = new StringBuilder();
        }


        StringBuilder sqlValue = new StringBuilder(" where 1=1 ");
        Class queryClass = query.getClass();
        String entitySimpleName = entityClass.getSimpleName();

        /*
        先根据group对查询条件进行分组，相同gorup间的用or。不痛group间用and
         */
        Map<String, StringBuilder> searchSqlMap = new HashMap<>();
        for (Field fieldOfQuery : queryClass.getDeclaredFields()) {
            WTSearch searchAnnotation = fieldOfQuery.getAnnotation(WTSearch.class);
            if (searchAnnotation == null) {
                continue;
            }
            TwoTuple<Object, Class> fieldValueInfo = getFieldValue(query, fieldOfQuery.getName());
            if (fieldValueInfo == null || (StringUtils.singleton.isEmpty(com.automonia.tools.StringUtils.singleton.getString(fieldValueInfo.first)) && searchAnnotation.type() != WTSearchType.nullable)) {
                continue;
            }
            String fieldEntityName = StringUtils.singleton.isEmpty(searchAnnotation.entityName()) ? entitySimpleName : searchAnnotation.entityName();
            String fieldName = StringUtils.singleton.isEmpty(searchAnnotation.fieldName()) ? fieldOfQuery.getName() : searchAnnotation.fieldName();
            String fromOn = StringUtils.singleton.isEmpty(searchAnnotation.midJoinOn()) ? searchAnnotation.fromOn() : searchAnnotation.midJoinOn();

            if (!searchSqlMap.containsKey(searchAnnotation.group())) {
                searchSqlMap.put(searchAnnotation.group(), new StringBuilder());
            }
            searchSqlMap.get(searchAnnotation.group()).append(StringUtils.singleton.isEmpty(searchAnnotation.group()) ? "and(" : "or(");

            searchSqlMap.get(searchAnnotation.group()).append(fieldEntityName).append(fromOn).append(".").append(getEntityClassInfoFactory().getColumnName(fieldEntityName, fieldName)).append(searchAnnotation.type().getSearchValue(getDataBaseValueString(fieldValueInfo))).append(")");

            /*
            如果设置了generateLeftJoinSql，并且也设置了关联属性，那么生成其left join语句，并追加到leftJoinSqlValue
             */
            if (generateLeftJoinSql) {
                getLeftJoinItemSql(searchAnnotation, entitySimpleName, sqlOfLeftJoinItems);
            }
        }
        for (String searchSqlItemKey : searchSqlMap.keySet()) {
            StringBuilder searchSqlItem = searchSqlMap.get(searchSqlItemKey);
            sqlValue.append("and(").append(searchSqlItem.substring(StringUtils.singleton.isEmpty(searchSqlItemKey) ? 3 : 2)).append(")");
        }


        /*
        如果设置了checkGroupBy，并且也设置了group by的属性。那么生成其group by语句。并追加之
         */
        if (checkGroupBy && query.getGroupBy() != null) {
            sqlValue.append(" group by ").append(entitySimpleName).append(".").append(getEntityClassInfoFactory().getColumnName(entitySimpleName, query.getGroupBy().getFieldName()));
        }

        /*
        如果设置了checkOrderBy，并且orderList中有指定排序内容，那么生成其order by语句，并追加之
         */
        if (checkOrderBy && query.getOrderList() != null && !query.getOrderList().isEmpty()) {
            StringBuilder sqlOfOrderByItems = new StringBuilder();
            for (WTOrder item : query.getOrderList()) {
                sqlOfOrderByItems.append(",")
                        .append(entityClass.getSimpleName())
                        .append(".")
                        .append(getEntityClassInfoFactory().getColumnName(entitySimpleName, item.getFieldName()))
                        .append(" ")
                        .append(item.getOrderType().toString());
            }
            if (sqlOfOrderByItems.length() > 0) {
                sqlValue.append(" order by ").append(sqlOfOrderByItems.substring(1));
            }
        }

        return Tuple.tuple(sqlValue.toString(), sqlOfLeftJoinItems.toString());
    }


    public <M extends WTModel> String getIncrementOrDecrementSql(String fieldName, WTQuery query, Class<M> modelClass, Boolean isIncrement) throws NoSuchFieldException, ClassNotFoundException, WTException {
        if (modelClass == null || StringUtils.singleton.isEmpty(fieldName)) {
            return null;
        }
        Class<M> entityClass = getEntityClassInfoFactory().getEntityClass(modelClass);
        String entitySimpleName = entityClass.getSimpleName();

        String tableName = getEntityClassInfoFactory().getTableName(entitySimpleName);
        String columnName = getEntityClassInfoFactory().getColumnName(entitySimpleName, fieldName);

        String rementItemSql = isIncrement ? " + 1 " : " - 1 ";

        StringBuilder updateSqlValue = new StringBuilder().append("update ").append(tableName).append(" ").append(entitySimpleName).append(" set ").append(columnName).append("=").append(columnName).append(rementItemSql);

        // 获取查询条件语句，并获取其中涉及到的关联语句
        TwoTuple whereAndLeftJoinItemsSql = getSqlWhereItems(query, entityClass, null, true, false, false);
        updateSqlValue.append(whereAndLeftJoinItemsSql.second).append(whereAndLeftJoinItemsSql.first);

        return updateSqlValue.toString();
    }


    /**
     * 从实体对象获取属性的值
     *
     * @param model     实体对象
     * @param fieldName 属性名称
     * @return 属性的值
     */
    private <M> TwoTuple<Object, Class> getFieldValue(M model, String fieldName) {
        if (model == null || StringUtils.singleton.isEmpty(fieldName)) {
            return null;
        }
        try {
            Method getFieldMethod = model.getClass().getMethod(StringUtils.singleton.getGetMethodName(fieldName));

            return Tuple.tuple(getFieldMethod.invoke(model), getFieldMethod.getReturnType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象的值(Object)转化未存储在数据库中的内容
     * <p>
     * 返回的内容会根据数据类型进行转化，字符和日期会用单引号
     *
     * @param valueInfo 属性值信息(first是内容，second是类型)
     * @return 存储在数据中的内容
     */
    private String getInsertValueString(TwoTuple<Object, Class> valueInfo) {

        TwoTuple<String, Boolean> valueTuple = getValueString(valueInfo.first, valueInfo.second);

        if (valueTuple.second) {
            return "'" + valueTuple.first + "'";
        } else {
            return valueTuple.first;
        }
    }


    /**
     * 获取value对象在数据库中存放的数值
     *
     * @param valueInfo 待存放的数据对象(first是内容，second是类型)
     * @return
     */
    private String getDataBaseValueString(TwoTuple<Object, Class> valueInfo) {
        return getValueString(valueInfo.first, valueInfo.second).first;
    }

    /**
     * 将对象的值(Object)转化未存储在数据库中的内容
     * <p>
     * 返回的内容会根据数据类型进行转化，字符和日期会用单引号
     *
     * @param value          属性值
     * @param valueTypeClass 属性Class对象
     * @return String:存储在数据中的内容, Boolean:是否需要加上'(单引号)
     */
    private TwoTuple<String, Boolean> getValueString(Object value, Class valueTypeClass) {
        if (value == null) {
            return Tuple.tuple("", false);
        }
        if (String.class.isAssignableFrom(valueTypeClass)) {
            String valueString = value.toString();

            if (valueString.contains("'")) {
                valueString = valueString.replaceAll("'", "\"");
            }

            return Tuple.tuple(valueString, true);
        }

        /**
         * 将日期的更新操作转变层字符串，数据库不保存日期对象的最初格式内容
         */
        else if (Date.class.isAssignableFrom(valueTypeClass)) {
            return Tuple.tuple(DateUtils.singleton.getDateTimeString((Date) value), true);
        }

        /**
         * 将布尔对象的更新转变为整数，true-1， false-0
         */
        else if (Boolean.class.isAssignableFrom(valueTypeClass)) {
            return Tuple.tuple((String.valueOf(value)).equals("true") ? "1" : "0", false);
        }

        /**
         * 如果属性是布尔对象，那么将其的value数值保存到数据库
         * 系统的所有枚举对象都必须实现 BaseEnum
         */
        else if (valueTypeClass.isEnum()) {
            return Tuple.tuple(((WTEnum) value).getValue().toString(), false);
        }

        return Tuple.tuple(value.toString(), false);
    }


    public EntityClassInfoFactory getEntityClassInfoFactory() {
        return entityClassInfoFactory;
    }
}
