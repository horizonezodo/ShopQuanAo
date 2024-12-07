package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Category;

import java.util.List;

@Repository
public interface CategoryRepo extends MongoRepository<Category, String> {
    List<Category> findAllById(String id);
    List<Category> findAllByLevel(int level);
}
