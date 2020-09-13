package com.broada.demo.springbootjdbc.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ChengHui
 * @create 2020-05-11 10:28
 * @desc 数据库操作工具类
 */
public class DbUtil {

    private final static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private static Map<String, DruidDataSource> druidDataSources = new ConcurrentHashMap<>();


    /**
     * bind the data source to be used for the current thread
     *
     * @param prefix
     */
    public static void setDataSourceHolder(String prefix) {
        contextHolder.set(prefix);
    }

    /**
     * gets the datasource used by the current thread
     *
     * @return
     */
    public static String getDataSourceHolder() {
        return contextHolder.get();
    }

    /**
     * remove current thread threadlocalmap
     */
    public static void finalizeThreadLocal() {
        contextHolder.remove();
    }

    /**
     * 查询返回指定类型实体对象
     *
     * @param querySql
     * @param requiredType
     * @return
     * @throws Exception
     */
    public static Object queryForObject(String querySql, Class requiredType) throws Exception {
        Object result = requiredType.newInstance();
        BeanUtil.setValue(result, queryForMap(querySql));
        return result;
    }

    /**
     * 查询返回指定列数据
     * @param querySql
     * @param columnFieldname
     * @return
     * @throws Exception
     */
    public static Object queryForObject(String querySql, String columnFieldname) throws Exception {
        Map<String, Object> resultMap = queryForMap(querySql);
        return resultMap == null ? null : resultMap.get(columnFieldname);
    }

    /**
     * 查询返回单条行记录
     *
     * @param querySql
     * @return
     * @throws Exception
     */
    public static Map<String, Object> queryForMap(String querySql) throws Exception {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, Object> resultMap = new LinkedHashMap<>();

        try {
            con = getConnection();
            stmt = con.createStatement();

            logger.debug("execute sql:{}", querySql);
            rs = stmt.executeQuery(querySql);

            int rowSize = rs.getRow();
            if (rowSize > 1) {
                throw new RuntimeException("Expected results do not match, expected: 0 or 1, actual: " + rowSize);
            }

            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(columnName);
                    resultMap.put(columnName, columnValue);
                }
            }

            return resultMap;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            closeResultSet(rs);
            rs = null;
            closeStatement(stmt);
            stmt = null;
            releaseConnection(con);
            con = null;

            throw e;

        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            releaseConnection(con);
        }
    }

    /**
     * 查询返回集合
     *
     * @param querySql
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> queryForList(String querySql) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            stmt = con.createStatement();

            logger.debug("execute sql:{}", querySql);
            rs = stmt.executeQuery(querySql);

            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> resultMap = new LinkedHashMap<>();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object columnValue = rs.getObject(columnName);
                    resultMap.put(columnName, columnValue);
                }
                resultList.add(resultMap);
            }

            return resultList;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            closeResultSet(rs);
            rs = null;
            closeStatement(stmt);
            stmt = null;
            releaseConnection(con);
            con = null;

            throw e;

        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            releaseConnection(con);
        }
    }

    /**
     * 执行插入语句
     *
     * @param insertSql
     * @throws Exception
     */
    public static long executeInsert(String insertSql) throws Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

            logger.debug("execute sql:{}", insertSql);
            stmt.executeUpdate();
            con.commit();

            Long generateKey = null;
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            while (generatedKeys.next()) {
                generateKey = generatedKeys.getLong(1);
            }
            return generateKey;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            con.rollback();
            closeStatement(stmt);
            stmt = null;
            releaseConnection(con);
            con = null;

            throw e;

        } finally {
            closeStatement(stmt);
            releaseConnection(con);
        }
    }

    /**
     * 执行删除语句
     * @param deleteSql
     * @return
     * @throws Exception
     */
    public static boolean executeDelete(String deleteSql) throws Exception{
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(deleteSql);

            logger.debug("execute sql:{}", deleteSql);
            int i = stmt.executeUpdate();
            con.commit();

            return i != 0;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            con.rollback();
            closeStatement(stmt);
            stmt = null;
            releaseConnection(con);
            con = null;

            throw e;

        } finally {
            closeStatement(stmt);
            releaseConnection(con);
        }
    }

    /**
     * 执行更新语句
     *
     * @param updateSql
     * @throws Exception
     */
    public static void executeUpdate(String updateSql) throws Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(updateSql);

            logger.info("execute sql:{}", updateSql);
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            con.rollback();
            closeStatement(stmt);
            stmt = null;
            releaseConnection(con);
            con = null;

            throw e;

        } finally {
            closeStatement(stmt);
            releaseConnection(con);
        }
    }

    /**
     * 执行更新语句
     *
     * @param prepareStatementSql
     * @param batchSqlList
     * @param submitSize          一次提交多少条SQL
     */
    public static void executeBatch(String prepareStatementSql, List<String> batchSqlList, int submitSize) throws Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(prepareStatementSql);

            int submitIndex = 0;
            int index = 0;
            for (String batchSql : batchSqlList) {
                //logger.info("execute sql:{}", batchSql);
                stmt.addBatch(batchSql);

                submitIndex++;
                if (submitIndex % submitSize == 0) {
                    stmt.executeBatch();
                    con.commit();
                    stmt.clearBatch();

                    index++;

                    logger.debug("execute batch sql, the number of submissions is {}", index);
                }
            }

            if (submitIndex % submitSize > 0) {
                stmt.executeBatch();
                con.commit();
                stmt.clearBatch();

                logger.debug("execute batch sql, the number of submissions is {}", index);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            con.rollback();
            closeStatement(stmt);
            stmt = null;
            releaseConnection(con);
            con = null;

            throw e;

        } finally {
            closeStatement(stmt);
            releaseConnection(con);
        }
    }

    /**
     * 批量更新
     * @param sql 更新 sql
     * @param paramList sql 需要传递的参数
     * @throws Exception
     */
    public static void executeBatchUpdate(String sql, List<List> paramList) throws Exception {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql);

            // 使用Statement同时收集多条sql语句
            for (int i = 0, size = paramList.size(); i < size; i++) {
                List params = paramList.get(i);
                for (int j = 0, paramsize = params.size(); j < paramsize; j++) {
                    stmt.setObject(j+1, params.get(j));
                }

                stmt.addBatch();
            }

            stmt.executeBatch();
            con.commit();
            stmt.clearBatch();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            con.rollback();
            closeStatement(stmt);
            stmt = null;
            releaseConnection(con);
            con = null;
            throw e;
        } finally {
            closeStatement(stmt);
            releaseConnection(con);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.trace("Could not close JDBC ResultSet", ex);
            } catch (Throwable ex) {
                logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.trace("Could not close JDBC Statement", ex);
            } catch (Throwable ex) {
                logger.trace("Unexpected exception on closing JDBC Statement", ex);
            }
        }
    }

    public static void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                logger.error("Could not close JDBC Connection", ex);
            } catch (Throwable ex) {
                logger.error("Unexpected exception on closing JDBC Connection", ex);
            }
        }
    }

    public static <T> T requiredSingleResult(Collection<T> results) throws Exception {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            return null;
        } else if (results.size() > 1) {
            throw new RuntimeException("Expected results do not match, expected: 1, actual: " + size);
        } else {
            return results.iterator().next();
        }
    }

    /**
     * 初始化数据源
     *
     * @param dbPrefix
     */
    public static void initDruidDataSource(String dbPrefix) {
        contextHolder.set(dbPrefix);
        try {
            getDruidDataSource();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            contextHolder.remove();
        }
    }

    /**
     * 获取Druid数据源
     *
     * @return
     * @throws SQLException
     */
    private static DruidDataSource getDruidDataSource() throws Exception {

        String dbPrefix = getDataSourceHolder();
        if (druidDataSources.containsKey(dbPrefix)) {
            logger.debug("the datasource named {} has already been initialized..", dbPrefix);
            return druidDataSources.get(dbPrefix);

        } else {
            synchronized (DbUtil.class) {
                if (!druidDataSources.containsKey(dbPrefix)) {
                    DruidDataSource druidDataSource = createDruidDataSource(dbPrefix);
                    druidDataSources.put(dbPrefix, druidDataSource);
                    return druidDataSource;
                }
            }
        }
        logger.warn("the datasource with the name {} could not be found and the datasource initialization failed...", dbPrefix);
        return null;
    }

    /**
     * 创建Druid数据源
     *
     * @param dbPrefix 数据源标识
     * @return
     * @throws Exception
     */
    private static DruidDataSource createDruidDataSource(String dbPrefix) throws Exception {

        Assert.hasLength(dbPrefix, "the database prefix must not be null...");
        DruidDataSource druidDataSource = new DruidDataSource();

        // 根据 dbPrefix 找到指定数据源连接属性，包括 url username passwod
        if (StringUtils.isNotEmpty(dbPrefix)) {

            String jdbcUrl = System.getProperty(String.format("%s.%s", dbPrefix, "jdbc.url"));
            String jdbcUser = System.getProperty(String.format("%s.%s", dbPrefix, "jdbc.username"));
            String jdbcPass = System.getProperty(String.format("%s.%s", dbPrefix, "jdbc.password"));

            logger.debug("the datasource named {}, the actual value of its jdbcurl is {}", dbPrefix, jdbcUrl);
            //druidDataSource.setDriverClassName(prop.getProperty("jdbc.driverClassName"));
            druidDataSource.setUrl(jdbcUrl);
            druidDataSource.setUsername(jdbcUser);
            druidDataSource.setPassword(jdbcPass);
        }

        // common db properties
        druidDataSource.setInitialSize(Integer.parseInt(System.getProperty("spring.datasource.druid.initialSize")));
        druidDataSource.setMaxActive(Integer.parseInt(System.getProperty("spring.datasource.druid.maxActive")));
        druidDataSource.setMinIdle(Integer.parseInt(System.getProperty("spring.datasource.druid.minIdle")));
        druidDataSource.setMaxWait(Integer.parseInt(System.getProperty("spring.datasource.druid.maxWait")));
        druidDataSource.setValidationQuery(System.getProperty("spring.datasource.druid.validationQuery"));
        druidDataSource.setDefaultAutoCommit(false);
        druidDataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(System.getProperty("spring.datasource.druid.timeBetweenEvictionRunsMillis")));
        druidDataSource.setMinEvictableIdleTimeMillis(Long.parseLong(System.getProperty("spring.datasource.druid.minEvictableIdleTimeMillis")));
        druidDataSource.setTestWhileIdle(Boolean.parseBoolean(System.getProperty("spring.datasource.druid.testWhileIdle")));
        druidDataSource.setTestOnBorrow(Boolean.parseBoolean(System.getProperty("spring.datasource.druid.testOnBorrow")));
        druidDataSource.setTestOnReturn(Boolean.parseBoolean(System.getProperty("spring.datasource.druid.testOnReturn")));
        druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(System.getProperty("spring.datasource.druid.poolPreparedStatements")));
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(System.getProperty("spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize")));
        druidDataSource.setValidationQueryTimeout(Integer.parseInt(System.getProperty("spring.datasource.druid.validationQueryTimeout")));
        druidDataSource.setFilters("stat");

        druidDataSource.init();

        logger.info("the datasource with the name {} initialize success...", dbPrefix);
        return druidDataSource;
    }

    /**
     * 获取Druid连接
     *
     * @return
     * @throws Exception
     */
    private static DruidPooledConnection getConnection() throws Exception {
        DruidDataSource druidDataSource = getDruidDataSource();
        DruidPooledConnection connection = druidDataSource.getConnection();
        return connection;
    }


}
