package com.recruitment.candidate_service.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract  class SoftDeletable {
    private boolean deleted = false;
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
