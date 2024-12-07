package vn.horizonezodo.core.Output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.horizonezodo.core.Entity.PAYMENTTYPE;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryOutput {
    private String id;
    private List<OrderItemOutput> orderItemOutput;
    private double paymentAmount;
    private long dayPayment;
    private PAYMENTTYPE paymenttype;
    private String userPayment;
}
