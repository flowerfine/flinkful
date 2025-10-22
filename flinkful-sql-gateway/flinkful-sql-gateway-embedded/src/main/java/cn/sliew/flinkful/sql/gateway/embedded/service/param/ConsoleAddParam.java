package cn.sliew.flinkful.sql.gateway.embedded.service.param;

import lombok.Data;

@Data
public class ConsoleAddParam {

    /**
     * save name
     */
    private String name;

    /**
     * Data source connection ID
     */
    private Long dataSourceId;

    /**
     * databaseName
     */
    private String databaseName;

    /**
     * The space where the table is located
     */
    private String schemaName;

    /**
     * Database type
     */
    private String type;

    /**
     * ddl content
     */
    private String ddl;

    /**
     * ddl statement status: DRAFT/RELEASE
     */
    private String status;

    /**
     * Whether it is opened in the tab, y means open, n means not opened
     */
    private String tabOpened;

    /**
     * operation type
     */
    private String operationType;
}
