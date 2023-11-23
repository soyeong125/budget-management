package com.wanted.domain.member.dto.request;

import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.entity.Role;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.wanted.domain.member.constants.MemberConstant.ACCOUNT_MAX_LENGTH;
import static com.wanted.domain.member.constants.MemberConstant.ACCOUNT_MIN_LENGTH;

/**
 * 회원가입 할 때 입력받는 request DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignUpReqDto {

    /**
     * 대,소문자 + 특수문자로 구성된 8~16 자리인 정규식
     */
    public static final String PASSWORD_REGEX_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";


    // 계정명
    @NotNull(message = "계정명을 입력해주세요.")
    @Length(min = ACCOUNT_MIN_LENGTH, max = ACCOUNT_MAX_LENGTH,
            message = "계정명을 {min} ~ {max} 사이로 입력해주세요.")
    private String account;

    //비밀번호
    @NotNull(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = PASSWORD_REGEX_PATTERN, message = "비밀번호는 특수문자를 포함한 8~16자리 수 여야만 합니다.")
    private String password;

    // 비밀번호 확인
    @NotNull(message = "비밀번호 확인을 입력해주세요.")
    @Pattern(regexp = PASSWORD_REGEX_PATTERN, message = "비밀번호는 특수문자를 포함한 8~16자리 수 여야만 합니다.")
    private String passwordConfirm;

    public void validPasswordConfirm(){
        if(!password.equals(passwordConfirm)){
            throw new BusinessException(null, "passwordConfirm", ErrorCode.MEMBER_WRONG_PASSWORD_CONFIRM);
        }
    }

    public Member toEntity(PasswordEncoder passwordEncoder){
        return Member.builder()
                .account(this.account)
                .password(passwordEncoder.encode(this.password))
                .role(Role.USER)
                .build();
    }
}
