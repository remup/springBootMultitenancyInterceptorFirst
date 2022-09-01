package com.example.datasource.first;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

//@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TenantResolver implements CurrentTenantIdentifierResolver {

	private static final String DEFAULT_TENANT_ID = "H2";

	@Override
	public String resolveCurrentTenantIdentifier() {
		String currentDataSource = CurrentTenant.getCurrentTenant();

		if (currentDataSource != null) {

			return currentDataSource;

		}

		return DEFAULT_TENANT_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {

		return true;
	}

}
