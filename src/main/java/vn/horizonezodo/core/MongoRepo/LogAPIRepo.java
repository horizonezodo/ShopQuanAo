package vn.horizonezodo.core.MongoRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.LogAPI;

@Repository
public interface LogAPIRepo extends MongoRepository<LogAPI,String> {
    Page<LogAPI> findAll(Pageable pageable);
}
