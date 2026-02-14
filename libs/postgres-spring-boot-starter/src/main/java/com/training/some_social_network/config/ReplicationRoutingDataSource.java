package com.training.some_social_network.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        boolean currentTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        return currentTransactionReadOnly ? DatabaseType.REPLICA : DatabaseType.MASTER;
    }
}
