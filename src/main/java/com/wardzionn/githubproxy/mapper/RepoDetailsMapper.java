package com.wardzionn.githubproxy.mapper;

import com.wardzionn.githubproxy.dto.RepoDetailsDto;
import com.wardzionn.githubproxy.model.RepoDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepoDetailsMapper {
    RepoDetailsDto toDto(RepoDetails repoDetails);
}
