package ru.kpfu.itis.kononenko.gtree2.service.impl;


import org.springframework.stereotype.Service;
import ru.kpfu.itis.kononenko.gtree2.entity.User;
import ru.kpfu.itis.kononenko.gtree2.entity.VerificationToken;
import ru.kpfu.itis.kononenko.gtree2.enums.TokenStatus;
import ru.kpfu.itis.kononenko.gtree2.repository.UserRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository tokenRepo;
    private final UserRepository userRepo;

    public VerificationTokenService(VerificationTokenRepository tokenRepo, UserRepository userRepo) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
    }

    public VerificationToken createToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        token.setUsed(false);
        token = tokenRepo.save(token);
        return token;
    }

    public TokenStatus verifyToken(String tokenValue) {
        Optional<VerificationToken> opt = tokenRepo.findByToken(tokenValue);
        if (opt.isEmpty()) {
            return TokenStatus.INVALID;
        }
        VerificationToken token = opt.get();
        if (token.isUsed()) {
            return TokenStatus.ALREADY_USED;
        }
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return TokenStatus.EXPIRED;
        }
        User user = token.getUser();
        user.setEmailVerified(true);
        userRepo.save(user);

        token.setUsed(true);
        tokenRepo.save(token);
        return TokenStatus.VALID;
    }

    public void deleteAllByUser(User user) {
        tokenRepo.deleteAllByUser(user);
    }
}
