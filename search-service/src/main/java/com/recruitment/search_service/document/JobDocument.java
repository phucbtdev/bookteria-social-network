package com.recruitment.search_service.document;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.UUID;

@Document(indexName = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobDocument {
    @Id
    UUID id;

    @Field(type = FieldType.Keyword)
    UUID employerId;

    @Field(type = FieldType.Text, analyzer = "standard")
    String title;

    @Field(type = FieldType.Keyword)
    String slug;

    @Field(type = FieldType.Text, analyzer = "standard")
    String description;

    @Field(type = FieldType.Keyword)
    String industry;

    @Field(type = FieldType.Keyword)
    String jobLevel;

    @Field(type = FieldType.Keyword)
    String experienceLevel;

    @Field(type = FieldType.Keyword)
    String salaryRange;

    @Field(type = FieldType.Keyword)
    String workType;

    @Field(type = FieldType.Integer)
    Integer numberOfPositions;

    @Field(type = FieldType.Text, analyzer = "standard")
    String skillsRequired;

    @Field(type = FieldType.Keyword)
    String genderRequirement;

    @Field(type = FieldType.Text)
    String address;

    @Field(type = FieldType.Keyword)
    String status;
}