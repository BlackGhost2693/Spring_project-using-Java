package kristian9577.cardealer.validation.user;

        import kristian9577.cardealer.data.models.User;
        import kristian9577.cardealer.data.repository.UsersRepository;
        import kristian9577.cardealer.validation.ValidationConstants;
        import kristian9577.cardealer.validation.annotation.Validator;
        import kristian9577.cardealer.web.models.UserEditViewModel;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
        import org.springframework.validation.Errors;

@Validator
public class UserEditValidator implements org.springframework.validation.Validator {

    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserEditValidator(UsersRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEditViewModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserEditViewModel editViewModel = (UserEditViewModel) o;

        User user = this.userRepository.findByUsername(editViewModel.getUsername()).orElse(null);

        if (!this.bCryptPasswordEncoder.matches(editViewModel.getOldPassword(), user.getPassword())) {
            errors.rejectValue(
                    "oldPassword",
                    ValidationConstants.WRONG_PASSWORD,
                    ValidationConstants.WRONG_PASSWORD
            );
        }

        if (editViewModel.getPassword() != null && !editViewModel.getPassword().equals(editViewModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH,
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH
            );
        }

        if (!user.getEmail().equals(editViewModel.getEmail()) && this.userRepository.findByEmail(editViewModel.getEmail()).isPresent()) {
            errors.rejectValue(
                    "email",
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, editViewModel.getEmail()),
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, editViewModel.getEmail())
            );
        }
    }
}
