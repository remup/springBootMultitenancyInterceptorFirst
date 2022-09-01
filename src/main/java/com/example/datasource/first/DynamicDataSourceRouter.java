package com.example.datasource.first;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Service;

@SuppressWarnings("serial")
@Service
//public class DynamicDataSourceRouter extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl{
//
//	
//	
//	
//	
//	protected DataSource selectAnyDataSource() {
//
//		try {
//			return  MasterService.getDataSourceHashMap().get("SQL");
//		} catch (SQLException e) {
//			
//			return null;
//		}
//	}
//
//	public DynamicDataSourceRouter() {
//		
//		
//	}
//
//	protected DataSource selectDataSource(String tenantIdentifier) {
//		
//		try {
//			return  MasterService.getDataSourceHashMap().get(tenantIdentifier);
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//
//
//}
public class DynamicDataSourceRouter implements MultiTenantConnectionProvider{

	//private static final String DEFAULT_TENANT_ID = "dbo";

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		String tenant = CurrentTenant.getCurrentTenant();
		Connection connection =  MasterService.getDataSourceHashMap().get(tenantIdentifier).getConnection();
       try {

          if (tenant != null && tenant.equalsIgnoreCase("SQL") ) {
          	
               //connection.createStatement().execute("USE " + "dbo");
//        	  connection.createStatement()
//              .execute(String.format("SET SCHEMA %s;", "dbo"));
        	  connection.setSchema("dbo");

           } 
//            else {
//
//               connection.createStatement().execute("USE " + DEFAULT_TENANT_ID);
//
//           }

       }
      catch ( SQLException e ) {

            throw new HibernateException(

                    "Problem setting schema to " + tenantIdentifier,

                   e

            );

        }
		return  connection;
		
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean supportsAggressiveRelease() {
		// TODO Auto-generated method stub
		return false;
	}
	
}