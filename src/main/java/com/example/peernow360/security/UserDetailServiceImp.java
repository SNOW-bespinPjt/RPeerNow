package com.example.peernow360.security;




import com.example.peernow360.dto.UserMemberDto;
import com.example.peernow360.mappers.IUserMemberMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;


@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailServiceImp implements UserDetailsService {

    private final IUserMemberMapper iUserMemberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[UserDetailServiceImp] loadUserByUsername()");

        UserMemberDto userMemberDto = new UserMemberDto();
        Map<String, Object> data = new HashMap<>();

        try {

            /*
             * RequestContextHolder: Spring 컨텍스트에서 HttpServletRequest 에 직접 접근할 수 있도록 도와주는 역할
             * HttpServletRequest 사용하기 위해 이를 메소드 파라미터로 연이어 넘겨받을 필요가 없다.
             * currentRequestAttributes() 외에 getRequestAttributes() 도 있다.
             * 둘의 차이는 구해오려는 RequestAttributes 가 없을 경우 전자는 예외를 발생시키고 후자는 그냥 null 을 리턴
             */
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            String project_no = request.getHeader("project_no");

            data.put("project_no", project_no);
            data.put("id", username);

            userMemberDto.setId(username);
            UserMemberDto selectUserMemberDto = iUserMemberMapper.selectUserForLogin(userMemberDto);
            String roles = iUserMemberMapper.getRoleInPJT(data);

            if(!StringUtils.hasText(roles)){
                roles = "";

            }

            log.info("LOGIN SUCCESS AT DB");

                return User.builder()
                        .username(selectUserMemberDto.getId())
                        .password(selectUserMemberDto.getPw())
                        .roles(roles)
                        .build();

        } catch (UsernameNotFoundException e) {
            log.info("YOUR ID IS NOT EXIST");
            return (UserDetails) e;

        }

    }

}
