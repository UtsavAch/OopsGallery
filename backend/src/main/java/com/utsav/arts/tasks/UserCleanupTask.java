package com.utsav.arts.tasks;

import com.utsav.arts.models.User;
import com.utsav.arts.models.VerificationCode;
import com.utsav.arts.repository.UserRepository;
import com.utsav.arts.repository.VerificationCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserCleanupTask {

    private final UserRepository userRepository;
    private final VerificationCodeRepository codeRepository;

    public UserCleanupTask(UserRepository userRepository, VerificationCodeRepository codeRepository) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
    }

    // Runs every 24 hours
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void purgeUnverifiedUsers() {
        // Users must verify within 24 hours or be deleted
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);

        // Find all codes where the expiry date is before 'now'
        // AND the user was created before the 'cutoff'
        List<VerificationCode> expiredCodes =
                codeRepository.findExpiredUnverified(LocalDateTime.now(), cutoff);

        for (VerificationCode vCode : expiredCodes) {
            User user = vCode.getUser();

            if (!user.isEnabled()) {
                codeRepository.delete(vCode);
                userRepository.deleteById(user.getId());
            }
        }
    }
}