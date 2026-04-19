package com.wardzionn.githubproxy.service;

import com.wardzionn.githubproxy.dto.RepoDetailsDto;
import com.wardzionn.githubproxy.exception.BaseApplicationException;
import com.wardzionn.githubproxy.mapper.RepoDetailsMapper;
import com.wardzionn.githubproxy.model.RepoDetails;
import com.wardzionn.githubproxy.repository.RepoDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {
    private final RepoDetailsRepository repoDetailsRepository;
    private final RepoDetailsMapper repoDetailsMapper;

    public RepoDetailsDto getRepository(String fullName) {
        log.info("Fetching repository details from local database for: {}", fullName);
        RepoDetails repoDetails = repoDetailsRepository.findByFullName(fullName)
                .orElseThrow(BaseApplicationException::entityNotFoundException);
        return repoDetailsMapper.toDto(repoDetails);
    }
}
