package com.wardzionn.githubproxy.model;

import com.wardzionn.githubproxy.dto.responses.GithubResponseDto;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepoDetails extends BaseEntity {
    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private LocalDateTime created_at;

    public void update(GithubResponseDto responseDto) {
        this.fullName = responseDto.getFull_name();
        this.description = responseDto.getDescription();
        this.cloneUrl = responseDto.getClone_url();
        this.stars = responseDto.getStargazers_count();
        this.created_at = responseDto.getCreated_at();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepoDetails other)) {
            return false;
        }
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
