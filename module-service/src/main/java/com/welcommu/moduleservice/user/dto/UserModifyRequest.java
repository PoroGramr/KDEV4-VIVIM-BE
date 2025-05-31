package com.welcommu.moduleservice.user.dto;

import com.welcommu.modulecommon.exception.CustomErrorCode;
import com.welcommu.modulecommon.exception.CustomException;
import com.welcommu.moduledomain.company.Company;
import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class UserModifyRequest {
    @NotNull
    private String name;
    @Email
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private Long companyId;

    @NotBlank(message = "Idempotency Key는 필수입니다")
    private String idempotencyKey;

    public User modifyUser(User user,Company company) {
        // IdempotencyKey 검증
        if (user.getIdempotencyKey() != null &&
            user.getIdempotencyKey().equals(this.idempotencyKey)) {
            log.info("동일한 IdempotencyKey로 요청이 들어옴 - key: {}", this.idempotencyKey);
            return user;
        }

        // 새로운 IdempotencyKey로 요청이 들어온 경우
        log.info("새로운 IdempotencyKey로 요청이 들어옴 - 기존: {}, 새로운: {}",
            user.getIdempotencyKey(), this.idempotencyKey);

        user.setName(this.name);
        user.setEmail(this.email);
        user.setPhone(this.phone);
        user.setCompany(company);
        user.setModifiedAt(LocalDateTime.now());
        user.setIdempotencyKey(this.idempotencyKey);

        return user;
    }
}