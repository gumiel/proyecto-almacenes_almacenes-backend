package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.config.CacheConfig;
import com.gestion.almacenes.commons.util.PagePojo;
import com.gestion.almacenes.dtos.ConfigDto;
import com.gestion.almacenes.entities.Config;
import com.gestion.almacenes.mappers.ConfigMapper;
import com.gestion.almacenes.repositories.ConfigRepository;
import com.gestion.almacenes.services.ConfigService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.gestion.almacenes.servicesImpls.ExceptionsCustom.*;

@Service
@AllArgsConstructor
public class ConfigServiceImpl implements
    ConfigService {

  private final ConfigRepository configRepository;
  private final ConfigMapper configMapper;

  @Override
  public List<Config> getAll() {
    return configRepository.findAllByActiveIsTrue();
  }

  @Override
  public Config create(ConfigDto configdto) {

    if (configRepository.existsByCodeAndActiveIsTrue(configdto.getCode())) {
      errorDuplicateInFieldCode(ConfigDto.class, "code", configdto.getCode());
    }

    Config config = configMapper.fromDto(configdto, null);
    return configRepository.save(config);
  }

  @Override
  public Config update(Integer id, ConfigDto configdto) {
    Config configFound = this.findConfigById(id);
    if (configRepository.existsByCodeAndIdNotAndActiveIsTrue(configdto.getCode(),
        configFound.getId())) {
      errorDuplicateInFieldCode(ConfigDto.class, "code", configdto.getCode());
    }
    Config config = configMapper.fromDto(configdto, configFound);
    //config.setId(id);
    return configRepository.save(config);
  }

  @Override
  public Config getById(Integer id) {
    return this.findConfigById(id);
  }

  @Cacheable(value = CacheConfig.USER_INFO_CACHE, unless = "#result == null")
  @Override
  public Config getByCode(String code) {
    return configRepository.findByCodeAndActiveTrue(code).orElseThrow(
        errorEntityNotFound(Config.class, "code", code)
    );
  }

  @Override
  public void delete(Integer id) {
    Config config = this.findConfigById(id);
    if (config.getActive()) {
      config.setActive(false);
      configRepository.save(config);
    } else {
      errorAlreadyDeleted(Config.class, config.getId());
    }
  }


  @Override
  public List<Config> getFiltered(String code, String name) {
    return configRepository.findAll();
  }

  @Override
  public PagePojo<Config> getByPageAndFilters(Integer pageNumber, Integer pageSize,
      String sortField, String sortOrder, String code, String name) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<Config> configPage = configRepository.findAll(pageable);

    return configMapper.fromEntity(configPage);
  }

  private Config findConfigById(Integer id) {

    return configRepository.findByIdAndActiveIsTrue(id).orElseThrow(
        errorEntityNotFound(Config.class, id)
    );
  }

}
