package kristian9577.cardealer.validation.user;

import kristian9577.cardealer.data.repository.UsersRepository;
import kristian9577.cardealer.validation.ValidationConstants;
import kristian9577.cardealer.validation.annotation.Validator;
import kristian9577.cardealer.web.models.RegisterUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

@Validator
public class UserRegisterValidator implements org.springframework.validation.Validator {

    private final UsersRepository userRepository;

    @Autowired
    public UserRegisterValidator(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RegisterUserModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegisterUserModel registerUserModel = (RegisterUserModel) o;

        if (this.userRepository.findByUsername(registerUserModel.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, registerUserModel.getUsername()),
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, registerUserModel.getUsername())
            );
        }

        if (registerUserModel.getUsername().length() < 3 || registerUserModel.getUsername().length() > 10) {
            errors.rejectValue(
                    "username",
                    ValidationConstants.USERNAME_LENGTH,
                    ValidationConstants.USERNAME_LENGTH
            );
        }

        if (!registerUserModel.getPassword().equals(registerUserModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH,
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH
            );
        }

        if (this.userRepository.findByEmail(registerUserModel.getEmail()).isPresent()) {
            errors.rejectValue(
                    "email",
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, registerUserModel.getEmail()),
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, registerUserModel.getEmail())
            );
        }
    }
}
