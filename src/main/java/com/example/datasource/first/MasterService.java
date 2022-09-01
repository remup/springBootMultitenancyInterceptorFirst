package com.example.datasource.first;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MasterService {
	//

	private static DataSource sqlDataSource;
	private static DataSource H2DataSource;
	
	@Autowired
	@Qualifier("SQL")
	private DataSource sqlDataSourceObj;

	@Autowired
	@Qualifier("H2")
	private DataSource H2DataSourceObj;

	@PostConstruct
	public void init() {
		MasterService.sqlDataSource = sqlDataSourceObj;
		MasterService.H2DataSource = H2DataSourceObj;
	}

	public static Map<String, DataSource> getDataSourceHashMap() throws SQLException {

		Map<String, DataSource> hashMap = new HashMap<String, DataSource>();

		hashMap.put("SQL", sqlDataSource);
		hashMap.put("H2", H2DataSource);
		return hashMap;
	}
}
