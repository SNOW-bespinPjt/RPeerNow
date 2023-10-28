package com.example.peernow360.security;




import com.example.peernow360.dto.UserMemberDto;
import com.example.peernow360.mappers.IUserMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailServiceImp implements UserDetailsService {

    private final IUserMemberMapper iUserMemberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[UserDetailServiceImp] loadUserByUsername()");

        UserMemberDto userMemberDto = new UserMemberDto();

        try {
            userMemberDto.setId(username);
            UserMemberDto selectUserMemberDto = iUserMemberMapper.selectUserForLogin(userMemberDto);

                log.info("LOGIN SUCCESS AT DB");

                return User.builder()
                        .username(selectUserMemberDto.getId())
                        .password(selectUserMemberDto.getPw())
                        .roles("user")
                        .build();

        } catch (UsernameNotFoundException e) {
            log.info("YOUR ID IS NOT EXIST");
            return (UserDetails) e;

        }

    }

}
