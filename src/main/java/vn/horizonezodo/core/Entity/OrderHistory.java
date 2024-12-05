package vn.horizonezodo.core.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistory {
    @Id
    private String id;
    private String orderId;
    private String walletId;
    private long dayPayment;
    @Enumerated(EnumType.STRING)
    private PAYMENTTYPE paymenttype;
}
