package com.recruitment.candidate_service.service;

import com.recruitment.event.dto.CandidateCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidateUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidateResponse;
import com.recruitment.candidate_service.entity.Candidate;
import com.recruitment.candidate_service.exception.AppException;
import com.recruitment.candidate_service.exception.ErrorCode;
import com.recruitment.candidate_service.mapper.CandidateMapper;
import com.recruitment.candidate_service.repository.CandidateRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateService {

    CandidateMapper candidateMapper;
    CandidateRepository candidateRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "candidate-registration")
    public void createCandidateFromIdentity(CandidateCreationRequest creationRequest){
        try {
            Candidate candidate = candidateMapper.toCandidate(creationRequest);
            candidateRepository.save(candidate);
            kafkaTemplate.send("candidate-creation-success", Map.of(
                    "userId", creationRequest.getUserId().toString(),
                    "candidateId", candidate.getId().toString()
            ));
        } catch (Exception e) {
            kafkaTemplate.send("candidate-creation-failed", Map.of(
                    "userId", creationRequest.getUserId(),
                    "reason", "Exception: " + e.getMessage()
            ) );
        }
    }

    public CandidateResponse createCandidate(CandidateCreationRequest request){
        Candidate candidate = candidateMapper.toCandidate(request);
        log.info("Candidate created: {}", candidate.toString());
        candidate = candidateRepository.save(candidate);
        return candidateMapper.toCandidateResponse(candidate);
    }

    public CandidateResponse updateCandidate(UUID id, CandidateUpdateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        candidateMapper.updateCandidateFromRequest(candidate,request);
        return candidateMapper.toCandidateResponse(candidate);
    }

    public List<CandidateResponse> getCandidateList() {
        return candidateRepository.findAll().stream()
                .map(candidateMapper::toCandidateResponse)
                .toList();
    }

    public CandidateResponse getCandidateById(UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        return candidateMapper.toCandidateResponse(candidate);
    }


    public void deleteCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        candidateRepository.delete(candidate);
    }

}
