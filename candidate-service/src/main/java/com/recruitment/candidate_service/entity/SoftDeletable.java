package com.recruitment.candidate_service.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract  class SoftDeletable {
    @Builder.Default
    private boolean deleted = false;
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
