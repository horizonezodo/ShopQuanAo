package vn.horizonezodo.core.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Wallet;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Long> {
}
