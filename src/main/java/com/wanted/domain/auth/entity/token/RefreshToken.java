package com.wanted.domain.auth.entity.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * refresh 토큰
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    // id는 MemberId가 들어감
    @Id
    @Column(name = "rt_key", nullable = false)
    private String key;

    // Refresh 토큰 값
    @Column(name = "rt_value", nullable = false)
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * refresh 토큰 업데이트
     *
     * @param token 새로운 토큰
     * @return 새로운 토큰을 가진 RefreshToken
     */
    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
