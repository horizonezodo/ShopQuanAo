package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Product;

import java.util.List;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {
    Page<Product> findAllByCategoryId(String cateId, Pageable pageable);
    Page<Product> findAllByTop(boolean isTop, Pageable pageable);
    List<Product> findAllByCategoryId(String cateId);
}
