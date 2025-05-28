package com.recruitment.search_service.document;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(indexName = "jobs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setting(settingPath = "elasticsearch/job-settings.json")
@Mapping(mappingPath = "elasticsearch/job-mapping.json")
public class JobDocument {

    @Id
    String id;

    @Field(type = FieldType.Keyword)
    UUID jobId;

    @Field(type = FieldType.Keyword)
    UUID employerId;

    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    String title;

    @Field(type = FieldType.Keyword)
    String slug;

    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    String description;

    @Field(type = FieldType.Nested)
    IndustryInfo industry;

    @Field(type = FieldType.Nested)
    JobLevelInfo jobLevel;

    @Field(type = FieldType.Nested)
    ExperienceLevelInfo experienceLevel;

    @Field(type = FieldType.Nested)
    SalaryRangeInfo salaryRange;

    @Field(type = FieldType.Nested)
    WorkTypeInfo workType;

    @Field(type = FieldType.Integer)
    Integer numberOfPositions;

    @Field(type = FieldType.Text, analyzer = "keyword_analyzer")
    List<String> skills;

    @Field(type = FieldType.Keyword)
    String genderRequirement;

    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    String address;

    @Field(type = FieldType.Nested)
    GeoPoint location;

    @Field(type = FieldType.Date, format = DateFormat.date)
    LocalDate applicationDeadline;

    @Field(type = FieldType.Keyword)
    String status;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndustryInfo {
        @Field(type = FieldType.Keyword)
        UUID id;

        @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
        String name;

        @Field(type = FieldType.Keyword)
        String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobLevelInfo {
        @Field(type = FieldType.Keyword)
        UUID id;

        @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
        String name;

        @Field(type = FieldType.Integer)
        Integer level;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceLevelInfo {
        @Field(type = FieldType.Keyword)
        UUID id;

        @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
        String name;

        @Field(type = FieldType.Integer)
        Integer minYears;

        @Field(type = FieldType.Integer)
        Integer maxYears;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalaryRangeInfo {
        @Field(type = FieldType.Keyword)
        UUID id;

        @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
        String name;

        @Field(type = FieldType.Long)
        Long minSalary;

        @Field(type = FieldType.Long)
        Long maxSalary;

        @Field(type = FieldType.Keyword)
        String currency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkTypeInfo {
        @Field(type = FieldType.Keyword)
        UUID id;

        @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
        String name;

        @Field(type = FieldType.Keyword)
        String type; // FULL_TIME, PART_TIME, CONTRACT, etc.
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeoPoint {
        Double lat;
        Double lon;

        public static GeoPoint of(BigDecimal latitude, BigDecimal longitude) {
            if (latitude == null || longitude == null) {
                return null;
            }
            return GeoPoint.builder()
                    .lat(latitude.doubleValue())
                    .lon(longitude.doubleValue())
                    .build();
        }
    }
}