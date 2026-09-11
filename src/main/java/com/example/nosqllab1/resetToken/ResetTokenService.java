package com.example.nosqllab1.resetToken;

import com.example.nosqllab1.models.ResetTokenData;
import com.example.nosqllab1.repository.ResetTokenDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ResetTokenService {

    private final ResetTokenDataRepository resetTokenDataRepository;


    private static final long TTL_SECONDS = 120;
    private static final String BASE_URL = "http://localhost:8080/api/resetData/reset?token=";

    public String createResetToken(){
        String token = UUID.randomUUID().toString();
        long expiresAt = Instant.now().getEpochSecond() + TTL_SECONDS;

        ResetTokenData resetTokenData = new ResetTokenData(token, expiresAt);

        resetTokenDataRepository.save(resetTokenData);

        return BASE_URL + token;

    }

    public String activateToken (String token){
        ResetTokenData resetTokenData = resetTokenDataRepository.findById(token)
                .orElseThrow(() -> new RuntimeException("token not found"));

        if (resetTokenData.isExpired()){
            resetTokenDataRepository.delete(token);
            throw new RuntimeException("token outdated(");
        }

        resetTokenDataRepository.delete(token);
        return "Данные успешно обновлены";
    }


}
