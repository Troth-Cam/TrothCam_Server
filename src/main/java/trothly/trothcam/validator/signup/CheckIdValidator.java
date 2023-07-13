package trothly.trothcam.validator.signup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import trothly.trothcam.dto.auth.signup.SignupReqDto;
import trothly.trothcam.repository.MemberRepository;

@RequiredArgsConstructor
@Component
public class CheckIdValidator extends AbstractValidator<SignupReqDto> {

    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(SignupReqDto dto, Errors errors) {
        if (memberRepository.existsMemberByEmail(dto.getId())) {
            errors.rejectValue("id", "이미 사용중인 아이디입니다.");
        }
    }

}
