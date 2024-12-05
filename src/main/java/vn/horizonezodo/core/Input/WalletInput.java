package vn.horizonezodo.core.Input;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletInput {
    private BigDecimal price;
    private Long userId;
}
