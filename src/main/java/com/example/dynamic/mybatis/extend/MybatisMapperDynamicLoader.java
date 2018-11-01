package com.example.dynamic.mybatis.extend;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 监控mapper xml 文件
 */
@Component("mybatisMapperDynamicLoader")
public class MybatisMapperDynamicLoader implements DisposableBean, InitializingBean, ApplicationContextAware {

    private static final Logger logger = LogManager.getLogger(MybatisMapperDynamicLoader.class);

    private ConfigurableApplicationContext context = null;
    private transient String basePackage = null;
    private HashMap<String, String> fileMapping = new HashMap<String, String>();
    private Scanner scanner = null;
    private ScheduledExecutorService service = null;
    private final List<String> changeMapers = new ArrayList<String>();

    /**
     * 是否加载
     */
    @Value("${mybatis.mapper.autoload}")
    private boolean load;
    /**
     * 扫描间隔,默认5秒
     */
    @Value("${mybatis.mapper.interval}")
    private long interval;

    /**
     * mapper xml匹配路径
     */
    @Value("${mybatis.mapper.resource.pattern}")
    private String XML_RESOURCE_PATTERN;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = (ConfigurableApplicationContext) applicationContext;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            // 开启mybatis xml自动加载
            if (load) {
                if (interval == 0) {
                    interval = 5;
                }
                service = Executors.newScheduledThreadPool(1);
                // 获取xml所在包
                MapperScannerConfigurer config = context.getBean(MapperScannerConfigurer.class);
                Field field = config.getClass().getDeclaredField("basePackage");
                field.setAccessible(true);
                basePackage = (String) field.get(config);
                // 触发文件监听事件
                scanner = new Scanner();
                scanner.scan();
                service.scheduleAtFixedRate(new Task(), interval, interval, TimeUnit.SECONDS);
            }
        } catch (Exception e1) {
            logger.error("初始化参数异常：" + e1.getMessage(), e1);
        }

    }

    class Task implements Runnable {
        @Override
        public void run() {
            try {
                if (scanner.isChanged()) {
                    logger.info(changeMapers.toString() + "文件改变,重新加载.");
                    scanner.reloadXML();
                    logger.info("-------------mapper.xml加载完毕---------------------");
                }
            } catch (Exception e) {
                logger.error("文件改变时加载文件异常：" + e.getMessage(), e);
            }
        }

    }

    @SuppressWarnings({"rawtypes"})
    class Scanner {
        private String[] basePackages;

        private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        public Scanner() {
            basePackages = StringUtils.tokenizeToStringArray(MybatisMapperDynamicLoader.this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        }

        public Resource[] getResource(String basePackage, String pattern) throws IOException {
            return resourcePatternResolver.getResources(pattern);
        }

        public void reloadXML() throws Exception {
            DynamicSqlSessionTemplate dynamicSqlSessionTemplate = context.getBean(DynamicSqlSessionTemplate.class);
            Configuration configuration = dynamicSqlSessionTemplate.getConfiguration();
            // 移除加载项
            removeConfig(configuration);
            // 重新扫描加载
            for (String basePackage : basePackages) {
                Resource[] resources = getResource(basePackage, XML_RESOURCE_PATTERN);
                if (resources != null) {
                    for (int i = 0; i < resources.length; i++) {
                        if (resources[i] == null) {
                            continue;
                        }
                        try {
                            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resources[i].getInputStream(), configuration, resources[i].toString(), configuration.getSqlFragments());
                            xmlMapperBuilder.parse();
                        } catch (Exception e) {
                            throw new NestedIOException("Failed to parse mapping resource: '" + resources[i] + "'", e);
                        } finally {
                            ErrorContext.instance().reset();
                        }
                    }
                }
            }

        }

        private void removeConfig(Configuration configuration) throws Exception {
            Class<?> classConfig = configuration.getClass();
            clearMap(classConfig, configuration, "mappedStatements");
            clearMap(classConfig, configuration, "caches");
            clearMap(classConfig, configuration, "resultMaps");
            clearMap(classConfig, configuration, "parameterMaps");
            clearMap(classConfig, configuration, "keyGenerators");
            clearMap(classConfig, configuration, "sqlFragments");
            clearSet(classConfig, configuration, "loadedResources");
        }

        private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
            Field field = classConfig.getDeclaredField(fieldName);
            field.setAccessible(true);
            Map mapConfig = (Map) field.get(configuration);
            mapConfig.clear();
        }

        private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
            Field field = classConfig.getDeclaredField(fieldName);
            field.setAccessible(true);
            Set setConfig = (Set) field.get(configuration);
            setConfig.clear();
        }

        public void scan() throws IOException {
            if (!fileMapping.isEmpty()) {
                return;
            }
            for (String basePackage : basePackages) {
                Resource[] resources = getResource(basePackage, XML_RESOURCE_PATTERN);
                if (resources != null) {
                    for (int i = 0; i < resources.length; i++) {
                        String multi_key = getValue(resources[i]);
                        fileMapping.put(resources[i].getFilename(), multi_key);
                    }
                }
            }
        }

        private String getValue(Resource resource) throws IOException {
            String contentLength = String.valueOf((resource.contentLength()));
            String lastModified = String.valueOf((resource.lastModified()));
            return new StringBuilder(contentLength).append(lastModified).toString();
        }

        public boolean isChanged() throws IOException {
            boolean isChanged = false;
            changeMapers.clear();
            for (String basePackage : basePackages) {
                Resource[] resources = getResource(basePackage, XML_RESOURCE_PATTERN);
                if (resources != null) {
                    for (int i = 0; i < resources.length; i++) {
                        String name = resources[i].getFilename();
                        String value = fileMapping.get(name);
                        String multi_key = getValue(resources[i]);
                        if (!multi_key.equals(value)) {
                            changeMapers.add(name);
                            isChanged = true;
                            fileMapping.put(name, multi_key);
                        }
                    }
                }
            }
            return isChanged;
        }
    }

    @Override
    public void destroy() throws Exception {
        if (service != null) {
            service.shutdownNow();
        }
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

}
