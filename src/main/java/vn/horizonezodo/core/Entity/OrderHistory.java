package vn.horizonezodo.core.Entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistory {
    @Id
    private String id;
    private Long orderId;
    private Long walletId;
    private Long userId;
    private long dayPayment;
    @Enumerated(EnumType.STRING)
    private PAYMENTTYPE paymenttype;
    private double paymentAmount;
}
