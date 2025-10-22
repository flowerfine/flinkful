package cn.sliew.flinkful.sql.gateway.embedded.service.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsolePageParam {

    /**
     * page number
     */
    @NotNull(message = "Pagination page number cannot be empty")
    @Min(value = 1, message = "Pagination page number must be greater than 0")
    private Integer pageNo;
    /**
     * Paging Size
     */
    @NotNull(message = "Paging size cannot be empty")
    private Integer pageSize;

    /**
     * Data source connection ID
     */
    private Long dataSourceId;

    /**
     * databaseName
     */
    private String databaseName;

    /**
     * ddl statement status: DRAFT/RELEASE
     */
    private String status;

    /**
     * search keyword
     */
    private String searchKey;

    /**
     * Whether it is opened in the tab, y means open, n means not opened
     */
    private String tabOpened;

    /**
     * orderBy modify time desc
     */
    private Boolean orderByDesc;

    /**
     * orderBy create time desc
     */
    private Boolean orderByCreateDesc;

    /**
     * operation type
     */
    private String operationType;

    /**
     * user id
     */
    private Long userId;
}
