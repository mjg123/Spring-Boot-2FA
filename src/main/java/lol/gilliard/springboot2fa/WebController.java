package lol.gilliard.springboot2fa;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import me.legrange.haveibeenpwned.HaveIBeenPwndApi;
import me.legrange.haveibeenpwned.HaveIBeenPwndException;
import me.legrange.haveibeenpwned.PwnedHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

@Controller
public class WebController {

    private static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HaveIBeenPwndApi hibpApi;

    @GetMapping("/")
    public ModelAndView showHomepage(Principal principal){
        if (principal == null){
            return new ModelAndView("home");
        } else {
            return new ModelAndView("home", "user", principal);
        }
    }

    @GetMapping("/user/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "registration";
    }



    private int getPwnedHashCount(String password) throws HaveIBeenPwndException {
        String pwHash = HaveIBeenPwndApi.makeHash(password);

        String pwHashFirst5 = pwHash.substring(0, 5);
        String pwHashTheRest = pwHash.substring(5);

        return hibpApi.searchByRange(pwHashFirst5).stream()
            .filter(pwnedHash -> pwnedHash.getHash().equals(pwHashTheRest))
            .findFirst()
            .map(PwnedHash::getCount)
            .orElse(0);
    }


    @PostMapping("/user/registration")
    public ModelAndView registerNewUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, RedirectAttributes redirect){

        if (result.hasErrors()){
            return new ModelAndView("registration", "user", userDto);
        }

        try {

            int pwnedHashCount = getPwnedHashCount(userDto.getPassword());

            if (pwnedHashCount > 0){
                result.rejectValue("password", "password.overused", new Integer[]{pwnedHashCount}, "");
                return new ModelAndView("registration");
            }

            userService.createNewUser(userDto);
            redirect.addFlashAttribute("user", userDto);
            return new ModelAndView("redirect:/user/registered");

        } catch (HaveIBeenPwndException ex){
            logger.error("HaveIBeenPwndException", ex);
            return new ModelAndView("registration");

        } catch (UserService.UserAlreadyExistsException ex){

            userService.loadUserByUsername(userDto.getUsername());

            logger.warn("Attempt to register existing user username: {}", userDto.getUsername());
            result.rejectValue("username", "already.exists");

            return new ModelAndView("registration");
        }
    }

    @GetMapping("/user/registered")
    public String showRegisteredPage(Model model) throws UnsupportedEncodingException {

        UserDto userDto = (UserDto) (model.asMap().get("user"));

        if (userDto == null){
            // this check prevents people browsing straight to this page
            return "redirect:/user/registration";
        }

        // TODO: (DONE registration) If the userDto has extra things on it that the user will need, we could add them to the model here
        // (see also /src/main/resources/templates/registered.html)
        String otpUrl = OTP.getURL(userDto.getSecret(), 6, Type.TOTP, "spring-boot-2fa-demo", userDto.getUsername());

        String twoFaQrUrl = String.format(
            "https://chart.googleapis.com/chart?cht=qr&chs=200x200&chl=%s",
            URLEncoder.encode(otpUrl, "UTF-8")); // "UTF-8" shouldn't be unsupported but it's a checked exception :(

        model.addAttribute("twoFaQrUrl", twoFaQrUrl);

        return "registered";
    }

    @RequestMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @RequestMapping("/user/home")
    public ModelAndView showUserHome(Principal principal){
        return new ModelAndView("userhome.html", "user", principal);
    }

}
