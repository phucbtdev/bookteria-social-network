package com.recruitment.employer_service.mapper;

import com.recruitment.employer_service.dto.request.ReviewCreationRequest;
import com.recruitment.employer_service.dto.response.ReviewResponse;
import com.recruitment.employer_service.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "employer", ignore = true)
    Review toEntity(ReviewCreationRequest request);

    @Mapping(source = "employer.id", target = "employerId")
    @Mapping(source = "employer.companyName", target = "employerName")
    ReviewResponse toResponse(Review review);
}
