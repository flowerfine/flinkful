package cn.sliew.flinkful.sql.gateway.embedded.service;

import cn.sliew.flinkful.sql.gateway.embedded.service.dto.ConsoleDTO;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.ConsoleAddParam;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.ConsolePageParam;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.ConsoleUpdateParam;

import java.util.List;

public interface FlinkfulConsoleService {

    List<ConsoleDTO> page(ConsolePageParam param);

    Long add(ConsoleAddParam param);

    void update(ConsoleUpdateParam param);

    void delete(Long id);
}
