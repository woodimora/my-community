package com.sparta.community.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.community.dto.KakaoUserInfoDto;
import com.sparta.community.model.User;
import com.sparta.community.repository.UserRepository;
import com.sparta.community.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class KakaoUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public KakaoUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerKakaoUser(KakaoUserInfoDto infoDto, String profileImage) {
        String nickname = infoDto.getNickname();
        User findByNickname = userRepository.findByNickname(nickname).orElse(null);
        if (findByNickname != null) {
            throw new IllegalArgumentException("중복된 별명를 사용하는 사용자가 존재합니다.");
        }
        String password = UUID.randomUUID().toString();

        User user = new User(infoDto, passwordEncoder.encode(password), profileImage);
        userRepository.save(user);
        forceLogin(user);
    }

    @Transactional
    public KakaoUserInfoDto kakaoLogin(String code) throws JsonProcessingException {

        String accsee_token = getAccessToken(code);

        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accsee_token);

        User findByKakaoId = userRepository.findByKakaoId(kakaoUserInfo.getId()).orElse(null);
        if(findByKakaoId == null){
            String email = kakaoUserInfo.getEmail();
            User findByEmail = userRepository.findByEmail(email).orElse(null);
            if (findByEmail != null) {
                findByEmail.updateKakaoId(kakaoUserInfo.getId());
                forceLogin(findByEmail);
            }
            else {
                return new KakaoUserInfoDto(kakaoUserInfo.getId(), null, kakaoUserInfo.getEmail());
            }
        }
        else {
            forceLogin(findByKakaoId);
        }

        return kakaoUserInfo;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "34859b74f2e726356d9d9c561f7a8932");
        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        // HTTP Header 생성
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        System.out.println("responseBody = " + responseBody);

        return new KakaoUserInfoDto(
                jsonNode.get("id").asLong(),
                jsonNode.get("properties").get("nickname").asText(),
                jsonNode.get("kakao_account").get("email").asText()
                );
    }

    private void forceLogin(User kakaoUser) {
        // 4. 강제 로그인 처리
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
