package at.stnwtr.qusaml.entity;

import java.time.Instant;

public interface Entity<ID> {
    ID id();

    Instant createdAt();

    Instant updatedAt();
}
