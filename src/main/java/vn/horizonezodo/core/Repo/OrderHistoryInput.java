package vn.horizonezodo.core.Repo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderHistoryInput {
    private Long userId;
    private Long orderId;
    private String paymentType;
}
