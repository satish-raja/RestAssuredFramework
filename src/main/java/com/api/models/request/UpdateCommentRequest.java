package com.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📝 UpdateCommentRequest – Used to update an existing comment.
 * Fields allowed for update:
 * - name
 * - email
 * - body
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentRequest {
    private String name;
    private String email;
    private String body;
}
