package com.wardzionn.githubproxy.mapper;

import com.wardzionn.githubproxy.dto.responses.GithubResponseDto;
import com.wardzionn.githubproxy.dto.RepoDetailsDto;
import com.wardzionn.githubproxy.model.RepoDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GithubResponseMapper {
    @Mapping(source = "full_name", target = "fullName")
    @Mapping(source = "clone_url", target = "cloneUrl")
    @Mapping(source = "stargazers_count", target = "stars")
    RepoDetails toEntity(GithubResponseDto githubResponseDto);

    @Mapping(source = "full_name", target = "fullName")
    @Mapping(source = "clone_url", target = "cloneUrl")
    @Mapping(source = "stargazers_count", target = "stars")
    RepoDetailsDto toRepoDetailsDto(GithubResponseDto githubResponseDto);
}
