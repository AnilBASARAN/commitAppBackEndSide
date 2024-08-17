package app.dto.request.post;

import lombok.Data;

@Data
public class LikeUpdateRequest {
    Long id;
    Long postId;
    Long userId;
}
