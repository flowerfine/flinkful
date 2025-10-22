package cn.sliew.flinkful.sql.gateway.embedded.service.impl;

import cn.sliew.flinkful.sql.gateway.embedded.service.FlinkfulConsoleService;
import cn.sliew.flinkful.sql.gateway.embedded.service.dto.ConsoleDTO;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.ConsoleAddParam;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.ConsolePageParam;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.ConsoleUpdateParam;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FlinkfulConsoleServiceImpl implements FlinkfulConsoleService {

    private Map<Long, ConsoleDTO> store = Maps.newConcurrentMap();

    private final AtomicLong idGenerator = new AtomicLong(0L);

    @Override
    public List<ConsoleDTO> page(ConsolePageParam param) {
        return store.values().stream().toList();
    }

    @Override
    public Long add(ConsoleAddParam param) {
        ConsoleDTO dto = new ConsoleDTO();
        BeanUtils.copyProperties(param, dto);
        dto.setId(idGenerator.incrementAndGet());
        store.put(dto.getId(), dto);
        return dto.getId();
    }

    @Override
    public void update(ConsoleUpdateParam param) {
        ConsoleDTO dto = new ConsoleDTO();
        BeanUtils.copyProperties(param, dto);
        dto.setId(param.getId());
        store.put(dto.getId(), dto);
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}
