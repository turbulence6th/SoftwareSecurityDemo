package tr.com.turbulence6th.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.turbulence6th.security.entity.Country;
import tr.com.turbulence6th.security.entity.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class InjectionController {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InjectionController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        Optional<User> result = jdbcTemplate.query("SELECT * FROM users WHERE username = '" + username + "' AND password = '"
                + password + "'", (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }).stream()
            .findAny();
        String body;
        if (result.isPresent()) {
            body = "Successful login as " + result.get().getUsername();
            response.addCookie(new Cookie("token", UUID.randomUUID().toString()));
        } else {
            body = "Login failed";
        }

        return body;
    }

    @GetMapping("/country/search")
    public List<Country> search(@RequestParam(defaultValue = "") String criteria) {
        return jdbcTemplate.query("SELECT * FROM country WHERE name like '%" + criteria + "%'", (rs, rowNum) -> {
            Country country = new Country();
            country.setId(rs.getInt("id"));
            country.setName(rs.getString("name"));
            return country;
        });
    }
}
