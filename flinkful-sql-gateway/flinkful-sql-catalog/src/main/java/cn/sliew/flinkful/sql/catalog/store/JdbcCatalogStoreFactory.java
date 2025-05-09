/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sliew.flinkful.sql.catalog.store;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.catalog.CatalogStore;
import org.apache.flink.table.catalog.exceptions.CatalogException;
import org.apache.flink.table.factories.CatalogStoreFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static cn.sliew.flinkful.sql.catalog.store.JdbcCatalogStoreOptions.IDENTIFIER;

public class JdbcCatalogStoreFactory implements CatalogStoreFactory {

    @Override
    public CatalogStore createCatalogStore() {
        return new JdbcCatalogStore();
    }

    @Override
    public void open(Context context) throws CatalogException {
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        if (Objects.isNull(applicationContext)) {
            throw new CatalogException("Lack spring application context unable to open JdbcCatalogStore.");
        }
        FactoryUtil.FactoryHelper factoryHelper = new CatalogStoreFactoryHelper(this, context);
        factoryHelper.validate();

        ReadableConfig options = factoryHelper.getOptions();
    }

    @Override
    public void close() throws CatalogException {

    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        return Collections.emptySet();
    }

    public static class CatalogStoreFactoryHelper extends FactoryUtil.FactoryHelper<CatalogStoreFactory> {
        public CatalogStoreFactoryHelper(CatalogStoreFactory catalogStoreFactory, CatalogStoreFactory.Context context) {
            super(catalogStoreFactory, context.getOptions(), FactoryUtil.PROPERTY_VERSION);
        }
    }
}
