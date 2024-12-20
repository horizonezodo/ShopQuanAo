package vn.horizonezodo.core.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchProduct {
    private String keyword;
    private String cateId;
    private double minPrice;
    private double maxPrice;
    private long startDate;
    private long endDate;
    private int viewCount;
    private String sortBy;
    private boolean activate;
    private boolean top;
    private int limit;
    private int page;
}
