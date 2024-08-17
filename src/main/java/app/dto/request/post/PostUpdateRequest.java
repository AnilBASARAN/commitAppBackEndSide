package app.dto.request.post;

import lombok.Data;

@Data
public class PostUpdateRequest {
    String title;
    String text;
}
