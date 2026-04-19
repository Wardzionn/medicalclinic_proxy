package com.wardzionn.githubproxy.service;

import com.wardzionn.githubproxy.client.PatientClient;
import com.wardzionn.githubproxy.dto.RepoDetailsDto;
import com.wardzionn.githubproxy.exception.BaseApplicationException;
import com.wardzionn.githubproxy.mapper.GithubResponseMapper;
import com.wardzionn.githubproxy.mapper.RepoDetailsMapper;
import com.wardzionn.githubproxy.model.RepoDetails;
import com.wardzionn.githubproxy.repository.RepoDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {
    private final RepoDetailsRepository repoDetailsRepository;
    private final PatientClient patientClient;
    private final GithubResponseMapper githubResponseMapper;
    private final RepoDetailsMapper repoDetailsMapper;

    public RepoDetailsDto getRepository(String owner, String repositoryName) {
        log.info("Fetch repository details from GitHub for: {}/{}", owner, repositoryName);
        return githubResponseMapper.toRepoDetailsDto(patientClient.getRepositoryDetails(owner, repositoryName));
    }

    @Transactional
    public RepoDetailsDto saveRepository(String owner, String repositoryName) {
        log.info("Save repository details to local database for: {}/{}", owner, repositoryName);
        Optional<RepoDetails> repoDetailsOptional = repoDetailsRepository.findByFullName(owner + "/" + repositoryName);
        if (repoDetailsOptional.isPresent()) {
            return repoDetailsMapper.toDto(repoDetailsOptional.get());
        } else {
            return getAndSaveRepo(owner, repositoryName);
        }
    }

    @Transactional
    public RepoDetailsDto updateRepository(String owner, String repositoryName) {
        log.info("Update repository details in local database for: {}/{}", owner, repositoryName);
        RepoDetails repository = getByFullName(owner, repositoryName);
        repository.update(patientClient.getRepositoryDetails(owner, repositoryName));
        return repoDetailsMapper.toDto(repository);
    }

    @Transactional
    public void deleteRepository(String owner, String repositoryName) {
        log.info("Delete repository details from local database for: {}/{}", owner, repositoryName);
        repoDetailsRepository.deleteByFullName(owner + "/" + repositoryName);
    }

    RepoDetails getByFullName(String owner, String repositoryName) {
        log.info("Fetch repository details from local database for: {}/{}", owner, repositoryName);
        return repoDetailsRepository.findByFullName(owner + "/" + repositoryName).orElseThrow(BaseApplicationException::entityNotFoundException);
    }

    private RepoDetailsDto getAndSaveRepo(String owner, String repositoryName) {
        RepoDetails repoDetails = githubResponseMapper.toEntity(
                patientClient.getRepositoryDetails(owner, repositoryName)
        );
        return repoDetailsMapper.toDto(repoDetailsRepository.save(repoDetails));
    }
}
