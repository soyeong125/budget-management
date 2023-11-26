package com.wanted.domain.auth.application;

import com.wanted.domain.auth.dao.token.RefreshTokenRepository;
import com.wanted.domain.auth.entity.token.RefreshToken;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.domain.member.dto.request.MemberLoginReqDto;
import com.wanted.domain.member.dto.request.MemberSignUpReqDto;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import com.wanted.global.security.TokenProvider;
import com.wanted.global.security.data.TokenDto;
import com.wanted.global.security.data.TokenReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    
    private final MemberRepository memberRepository;
    // SecurityConfig 에서 등록한 Bcrypt 인코더
    private final PasswordEncoder passwordEncoder;
    // 토큰 관리자
    private final TokenProvider tokenProvider;
    // 검증 관리자
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원 가입
     *
     * @param reqDto 회원가입 입력 데이터
     * @return 생성된 회원의 id
     */
    public Long signUp(MemberSignUpReqDto reqDto){
        //아이디 중복 확인
        checkDuplicateAccount(reqDto.getAccount());

        //패스워드 암호화 및 권한 부여
        Member member = reqDto.toEntity(passwordEncoder);

        //저장
        Member createdMember = memberRepository.save(member);

        return createdMember.getId();
    }

    /**
     * 회원 아이디 중복 확인
     *
     * @param account 확인할 회원 계정명(id)
     */
    public void checkDuplicateAccount(String account){
        if(memberRepository.findByAccount(account).isPresent()){
            throw new BusinessException(account, "account", ErrorCode.MEMBER_ACCOUNT_DUPLICATE);
        }
    }

    /**
     * 로그인 + JWT 토큰 발급
     *
     * @param reqDto
     * @return
     */
    @Transactional
    public TokenDto login(MemberLoginReqDto reqDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = reqDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        //authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    /**
     * 토큰 재발급
     *
     * @param token 토큰 dto
     * @return 재발급한 토큰
     */
    @Transactional
    public TokenDto reissue(TokenReqDto token) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(token.getRefreshToken())) {
            throw new BusinessException(null, "token", ErrorCode.REFRESH_TOKEN_BAD_REQUEST);
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(
                token.getAccessToken());

        // 3. 저장소에서 회원 ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() ->
                        new BusinessException(authentication.getName(), "account", ErrorCode.MEMBER_LOGOUT));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(token.getRefreshToken())) {
            throw new BusinessException(null, "refreshToken", ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    /**
     * 로그아웃 - 토큰 삭제
     *
     * @param token
     */
    @Transactional
    public void logout(TokenReqDto token) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(token.getRefreshToken())) {
            throw new BusinessException(null, "token", ErrorCode.REFRESH_TOKEN_BAD_REQUEST);
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(
                token.getAccessToken());

        // 3. Refresh Token 이 DB에 있는지 확인
        refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() ->
                        new BusinessException(authentication.getName(), "account", ErrorCode.MEMBER_LOGOUT));

        // 4. 저장소에서 회원 ID 를 기반으로 Refresh Token 삭제
        refreshTokenRepository.deleteByKey(authentication.getName());
    }
}
