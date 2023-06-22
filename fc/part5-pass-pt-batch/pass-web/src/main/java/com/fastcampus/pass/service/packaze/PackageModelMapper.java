package com.fastcampus.pass.service.packaze;

import com.fastcampus.pass.repository.packaze.PackageEntity;
import com.fastcampus.pass.repository.pass.BulkPassEntity;
import com.fastcampus.pass.service.pass.BulkPass;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PackageModelMapper {
    PackageModelMapper INSTANCE = Mappers.getMapper(PackageModelMapper.class);

    List<Package> map(List<PackageEntity> packageEntities);

}
