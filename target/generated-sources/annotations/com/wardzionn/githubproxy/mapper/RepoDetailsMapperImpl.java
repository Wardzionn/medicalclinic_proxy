package com.wardzionn.githubproxy.mapper;

import com.wardzionn.githubproxy.dto.RepoDetailsDto;
import com.wardzionn.githubproxy.model.RepoDetails;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T23:03:58+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.18 (Ubuntu)"
)
@Component
public class RepoDetailsMapperImpl implements RepoDetailsMapper {

    @Override
    public RepoDetailsDto toDto(RepoDetails repoDetails) {
        if ( repoDetails == null ) {
            return null;
        }

        RepoDetailsDto.RepoDetailsDtoBuilder repoDetailsDto = RepoDetailsDto.builder();

        repoDetailsDto.fullName( repoDetails.getFullName() );
        repoDetailsDto.description( repoDetails.getDescription() );
        repoDetailsDto.cloneUrl( repoDetails.getCloneUrl() );
        repoDetailsDto.stars( repoDetails.getStars() );
        repoDetailsDto.created_at( repoDetails.getCreated_at() );

        return repoDetailsDto.build();
    }
}
