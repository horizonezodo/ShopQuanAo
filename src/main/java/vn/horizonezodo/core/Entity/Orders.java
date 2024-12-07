package vn.horizonezodo.core.Entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
    private long orderDate;
    private double totalPrice;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String shippingAddress;
    @Enumerated(EnumType.STRING)
    private ORDERSTATUS orderstatus;
    private long createAt;
    private long updateAt;
    private String userNote;
}
