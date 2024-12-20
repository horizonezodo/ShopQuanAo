package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.horizonezodo.core.Entity.Product;
import vn.horizonezodo.core.Output.ProductOutput;

import java.util.List;

public interface ProductRepoCustom {
    Page<ProductOutput> searchProductsForUser(
            String keyword,
            String cateId,
            double minPrice,
            double maxPrice,
            long startDate,
            long endDate,
            int viewCount,
            String sortBy,
            boolean activate,
            Pageable pageable
    );

    Page<ProductOutput> searchProductsForAdmin(
            String keyword,
            String cateId,
            double minPrice,
            double maxPrice,
            long startDate,
            long endDate,
            int viewCount,
            String sortBy,
            Pageable pageable
    );

    List<Product> searchPublicProduct(
            String keyword,
            String cateId,
            double minPrice,
            double maxPrice,
            long startDate,
            long endDate,
            int viewCount,
            String sortBy,
            boolean activate,
            boolean top,
            int limit
    );
}
