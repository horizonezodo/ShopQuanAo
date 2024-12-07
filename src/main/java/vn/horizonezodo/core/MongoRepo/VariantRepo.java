package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Variant;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepo extends MongoRepository<Variant, String> {
    List<Variant> findAllByProductId(String id);

    //Optional<Variant> findByProductIdAndId(String proId, String id);
}
