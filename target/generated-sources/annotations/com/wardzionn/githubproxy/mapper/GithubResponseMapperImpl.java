package com.wardzionn.githubproxy.mapper;

import com.wardzionn.githubproxy.dto.responses.GithubResponseDto;
import com.wardzionn.githubproxy.model.RepoDetails;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T23:03:58+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.18 (Ubuntu)"
)
@Component
public class GithubResponseMapperImpl implements GithubResponseMapper {

    @Override
    public RepoDetails toEntity(GithubResponseDto githubResponseDto) {
        if ( githubResponseDto == null ) {
            return null;
        }

        RepoDetails.RepoDetailsBuilder repoDetails = RepoDetails.builder();

        repoDetails.fullName( githubResponseDto.getFull_name() );
        repoDetails.cloneUrl( githubResponseDto.getClone_url() );
        repoDetails.stars( githubResponseDto.getStargazers_count() );
        repoDetails.description( githubResponseDto.getDescription() );
        repoDetails.created_at( githubResponseDto.getCreated_at() );

        return repoDetails.build();
    }
}
