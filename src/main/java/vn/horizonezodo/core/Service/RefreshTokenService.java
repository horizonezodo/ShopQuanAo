package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.RefreshToken;
import vn.horizonezodo.core.Entity.User;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Repo.RefreshTokenRepo;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo repo;

    @Autowired
    private UserService service;

    @Value("${jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> findByToken(String token){
        return repo.findByToken(token);
    }
    @Transactional
    public RefreshToken createRefreshToken(Long userId){
        RefreshToken refreshToken = new RefreshToken();
        User user = service.getUserById(userId).orElseThrow(() -> new MessageException("Không tìm thấy user theo id: " + userId));
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken = repo.save(refreshToken);
        return refreshToken;
    }
    @Transactional
    public RefreshToken updateRefreshToken(String token){
        RefreshToken refreshToken = repo.findByToken(token).orElseThrow(() -> new MessageException("Không tìm thấy refresh token"));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = repo.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0){
            repo.delete(refreshToken);
            throw new MessageException("Refresh token is expired");
        }
        return refreshToken;
    }

    @Transactional
    public int deleteAllUserRefreshToken(Long userId){
        User user = service.getUserById(userId).orElseThrow(() -> new MessageException("Không tìm thấy user theo id: " + userId));
        return repo.deleteAllByUser(user);
    }
}
