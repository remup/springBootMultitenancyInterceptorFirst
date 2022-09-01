package com.example.datasource.first;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.hibernate.EmptyInterceptor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Lazy
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({ JpaProperties.class })
@EnableJpaRepositories("com.example.datasource.first")
@PropertySource("classpath:database.properties")
public class ConfigurationClass {

	private final String PROPERTY_DRIVER = "driver";
	private final String PROPERTY_URL = "url";
	private final String PROPERTY_USERNAME = "user";
	private final String PROPERTY_PASSWORD = "password";
	private final String PROPERTY_SHOW_SQL = "hibernate.show_sql";
	private final String PROPERTY_DIALECT = "hibernate.dialect";

	private final String PROPERTY_DRIVER2 = "driver2";
	private final String PROPERTY_URL2 = "url2";
	private final String PROPERTY_USERNAME2 = "user2";
	private final String PROPERTY_PASSWORD2 = "password2";

	@Autowired
	Environment environment;

	@Autowired
	private JpaProperties jpaproperties;

	@Bean(name = "SQL")
	@Primary
	DataSource dataSource() throws SQLException {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(environment.getProperty(PROPERTY_URL));
		ds.setUsername(environment.getProperty(PROPERTY_USERNAME));
		ds.setPassword(environment.getProperty(PROPERTY_PASSWORD));
		ds.setDriverClassName(environment.getProperty(PROPERTY_DRIVER));
		System.out.println("test Connection   " + ds.getConnection().getMetaData().getUserName());
		return ds;
	}

	@Bean(name = "H2")
	DataSource dataSource_H2() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(environment.getProperty(PROPERTY_URL2));
		ds.setUsername(environment.getProperty(PROPERTY_USERNAME2));
		ds.setPassword(environment.getProperty(PROPERTY_PASSWORD2));
		ds.setDriverClassName(environment.getProperty(PROPERTY_DRIVER2));
		return ds;
	}

	@Bean(name = "datasourceBasedMultitenantConnectionProvider")
	public MultiTenantConnectionProvider dynamicDataSourceRouter() {
		return new DynamicDataSourceRouter();
	}

	@Bean(name = "currentTenantIdentifierResolver")
	public CurrentTenantIdentifierResolver tenantResolver() {
		return new TenantResolver();
	}

	@PersistenceContext
	@Primary
	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier("datasourceBasedMultitenantConnectionProvider") MultiTenantConnectionProvider dynamicDataSourceRouter,
			@Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {
		LocalContainerEntityManagerFactoryBean lfb = new LocalContainerEntityManagerFactoryBean();
		// lfb.setDataSource(dataSource());

		Map<String, Object> jpaProperties = new LinkedHashMap<>();
		jpaProperties.putAll(this.jpaproperties.getProperties());
		jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
		jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, dynamicDataSourceRouter);

		jpaProperties.put(PROPERTY_DIALECT, environment.getProperty(PROPERTY_DIALECT));
		jpaProperties.put(PROPERTY_SHOW_SQL, environment.getProperty(PROPERTY_SHOW_SQL));
		//jpaProperties.put("hibernate.ejb.interceptor",hibernateInterceptor() );
		//jpaProperties.put("hibernate.default_schema", "dbo");

		lfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lfb.setPersistenceUnitName("Customer");
		lfb.setPackagesToScan("com.example.datasource.first");
		lfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		lfb.setJpaPropertyMap(jpaProperties);
		return lfb;
	}

	@SuppressWarnings("serial")
	private EmptyInterceptor hibernateInterceptor() {
		
		return new EmptyInterceptor(){
			public String onPrepareStatement(String sql){
				String sqlQ = super.onPrepareStatement(sql);
				if(CurrentTenant.getCurrentTenant().equalsIgnoreCase("H2")){
					sqlQ=sqlQ.replace("dbo.", "");
				}
				return sqlQ;
			}
		};
	}

	@Bean
	JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return transactionManager;
	}

}
