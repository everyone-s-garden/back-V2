package com.garden.back.auth;

import com.garden.back.auth.client.MemberProvider;
import com.garden.back.auth.jwt.TokenProvider;
import com.garden.back.auth.jwt.response.TokenResponse;
import com.garden.back.member.Member;
import com.garden.back.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final Map<AuthProvider, MemberProvider> authRegistrations;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(
        Map<AuthProvider, MemberProvider> authRegistrations,
        MemberRepository memberRepository,
        TokenProvider tokenProvider
    ) {
        this.authRegistrations = authRegistrations;
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public TokenResponse login(AuthProvider authProvider, String accessToken) {
        Member member = authRegistrations.get(authProvider).getMember(accessToken);
        Member savedMember = saveOrUpdate(member);
        return tokenProvider.generateTokenDto(savedMember);
    }

    private Member saveOrUpdate(Member member) {
        Member savedMember = memberRepository.findByEmail(member.getEmail()).orElse(member);
        return memberRepository.save(savedMember);
    }
}
