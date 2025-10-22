package cn.sliew.flinkful.sql.gateway.embedded.controller;

import cn.sliew.flinkful.sql.gateway.embedded.service.FlinkfulConsoleService;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.CatalogInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.ConsoleDTO;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.ConsolePageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/flinkful/console")
@Tag(name = "Sql Console接口")
public class FlinkfulConsoleController {

    @Autowired
    private FlinkfulConsoleService consoleService;

    @GetMapping("page")
    @Operation(summary = "分页查询 console", description = "分页查询 console")
    public List<ConsoleDTO> page(@Valid ConsolePageParam param) {
        return consoleService.page(param);
    }

    

}
