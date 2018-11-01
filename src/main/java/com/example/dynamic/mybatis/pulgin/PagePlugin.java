package com.example.dynamic.mybatis.pulgin;

import com.example.dynamic.mybatis.pulgin.dialet.Dialect;
import com.example.dynamic.mybatis.pulgin.dialet.MySQLDialect;
import com.example.dynamic.mybatis.pulgin.dialet.OracleDialect;
import com.example.dynamic.utils.ReflectionUtil;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.xml.bind.PropertyException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Mybatis的分页查询插件，通过拦截StatementHandler的prepare方法来实现。
 * 只有在参数列表中包括Page类型的参数时才进行分页查询。 在多参数的情况下，只对第一个Page类型的参数生效。
 * 另外，在参数列表中，Page类型的参数无需用@Param来标注
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PagePlugin implements Interceptor {

    private static final Logger logger = LogManager.getLogger(PagePlugin.class);
    /**
     * 数据库方言
     */
    private Dialect dialectObject = null;
    /**
     * mybaits的数据库xml映射文件中需要拦截的ID(正则匹配)
     */
    @Value("${mybatis.configuration.plugins.pagePlugin.pageSqlId}")
    private String pageSqlId = "";

    public final static String DBTYPE_MYSQL = "mysql";
    public final static String DBTYPE_ORACLE = "oracle";

    public PagePlugin(Dialect dialect) {
        this.dialectObject = dialect;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectionUtil.getValueByFieldName(statementHandler, "delegate");
            MappedStatement mappedStatement = (MappedStatement) ReflectionUtil.getValueByFieldName(delegate, "mappedStatement");
            /**
             * 方法1：通过ＩＤ来区分是否需要分页．.*query.* 方法2：传入的参数是否有page参数，如果有，则分页，
             */
            // 拦截需要分页的SQL
            if (mappedStatement.getId().matches(pageSqlId)) {
                BoundSql boundSql = delegate.getBoundSql();
                // 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject == null) {
                    return invocation.proceed();
                } else {
                    Pager pager = null;
                    //参数就是Pages实体
                    if (parameterObject instanceof Pager) {
                        pager = (Pager) parameterObject;
                    } else if (parameterObject instanceof Map) {
                        for (Map.Entry<?, ?> entry : (Set<Map.Entry<?, ?>>) ((Map) parameterObject).entrySet()) {
                            if (entry.getValue() instanceof Pager) {
                                pager = (Pager) entry.getValue();
                                break;
                            }
                        }
                    } else { // 参数为某个实体，该实体拥有Pages属性
                        pager = ReflectionUtil.getValueByFieldType(parameterObject, Pager.class);
                    }
                    if (pager == null) {
                        return invocation.proceed();
                    }
                    String sql = boundSql.getSql();
                    PreparedStatement countStmt = null;
                    ResultSet rs = null;
                    try {
                        Connection connection = (Connection) invocation.getArgs()[0];
                        // 记录统计
                        String countSql = "select count(1) from (" + sql + ") tmp_count";
                        countStmt = connection.prepareStatement(countSql);
                        ReflectionUtil.setValueByFieldName(boundSql, "sql", countSql);
                        DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
                        parameterHandler.setParameters(countStmt);
                        rs = countStmt.executeQuery();
                        int count = 0;
                        if (rs.next()) {
                            count = ((Number) rs.getObject(1)).intValue();
                        }
                        pager.setTotal(count);
                    } finally {
                        try {
                            if (rs != null) {
                                rs.close();
                            }
                            if (countStmt != null) {
                                countStmt.close();
                            }
                        } catch (Exception e) {
                            logger.error("rs countstmt close exception:" + e.getMessage(), e);
                        }
                    }
                    String pageSql = generatePagesSql(sql, pager);
                    logger.info("pagination sql:\r\n\r\n\t" + pageSql + "\r\n");
                    // 将分页sql语句反射回BoundSql.
                    ReflectionUtil.setValueByFieldName(boundSql, "sql", pageSql);
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 根据数据库方言，生成特定的分页sql
     *
     * @param sql
     * @param pager
     * @return
     */
    private String generatePagesSql(String sql, Pager pager) {
        if (pager != null) {
            String dbType = pager.getDbType();
            if (dbType == null || DBTYPE_MYSQL.equals(dbType)) {
                dialectObject = new MySQLDialect();
            } else if (DBTYPE_ORACLE.equals(dbType)) {
                dialectObject = new OracleDialect();
            }
            // pageNow默认是从1，而已数据库是从0开始计算的．所以(page.getPageNow()-1)
            return dialectObject.getLimitString(sql, pager.getStartRow(), pager.getPageSize(), pager.getSortOrder(), pager.getSortField());
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * xml配置时 加载
     *
     * @param p
     */
    @Override
    public void setProperties(Properties p) {
        // 数据库方言
        String dialect = p.getProperty("dialect");
        if (StringUtils.isEmpty(dialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            try {
                dialectObject = (Dialect) Class.forName(dialect).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(dialect + ", init fail!\n" + e);
            }
        }
        // 根据id来区分是否需要分页
        pageSqlId = p.getProperty("pageSqlId");
        if (StringUtils.isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
