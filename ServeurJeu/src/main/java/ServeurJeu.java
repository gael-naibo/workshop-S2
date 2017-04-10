import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;


@Controller
@EnableAutoConfiguration
public class ServeurJeu {

	public static void main(String[] args) {
		SpringApplication.run(ServeurJeu.class, args);

	}

}
