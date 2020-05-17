package org.codwh.redis.custom;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "org.codwh.redis.dao",
        sqlSessionFactoryRef = "sqlSessionFactory"
)
@ComponentScan("org.codwh.redis.custom")
@SuppressWarnings("all")
public class MyBatisDataSourceAutoConfiguration {

    //dao层接口扫描路径
    public static final String PACKAGE = "org.codwh.redis.custom.dao";

    //mybatis mapper扫描路径
    public static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Primary
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Primary
    @Bean(name = "dataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) {
        try {
            final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
            sqlSessionFactory.setDataSource(dataSource);
            sqlSessionFactory
                    .setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
            return sqlSessionFactory.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
