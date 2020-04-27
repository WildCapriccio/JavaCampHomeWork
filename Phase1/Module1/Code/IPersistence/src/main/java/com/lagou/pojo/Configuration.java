package com.lagou.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private DataSource dataSource;

    /*
     *   key: statementid  value:封装好的mappedStatement对象
     * statementId = namespace.id
     * 这样，Configuration对象中就包含了数据库配置，也包含了SQL语句配置。
     * */
    Map<String,MappedStatement> mappedStatementMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
