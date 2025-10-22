package cn.sliew.flinkful.sql.gateway.embedded.service.dto;

import lombok.Data;

@Data
public class ConsoleDTO  {

    private Long id;

    /**
     * file alias
     */
    private String name;

    /**
     * Data source id
     */
    private Long dataSourceId;

    /**
     * DB name
     */
    private String databaseName;

    /**
     * The space where the table is located
     */
    private String schemaName;

    /**
     * ddl language type
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
