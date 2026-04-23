package com.wardzionn.githubproxy.mapper;

import com.wardzionn.githubproxy.dto.DoctorDto;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.responses.DoctorResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorDtoMapper {

    @Mapping(source = "accountDto.firstName", target = "firstName")
    @Mapping(source = "accountDto.lastName", target = "lastName")
    @Mapping(source = "accountDto.email", target = "email")
    DoctorDto toDoctorDto(DoctorResponseDto doctorResponseDto);

    List<DoctorDto> toDoctorDtos(List<DoctorResponseDto> doctorResponseDtos);

    default PageDto<DoctorDto> toDoctorPage(PageDto<DoctorResponseDto> page) {
        return new PageDto<>(
                page.getPageNumber(),
                page.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                toDoctorDtos(page.getContent())
        );
    }
}
