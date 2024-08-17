package app.dto.request.refresh;

import lombok.Data;

@Data
public class RefreshRequest {

    Long userId;
    String refreshToken;
}