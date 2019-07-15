package tr.com.turbulence6th.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.turbulence6th.security.entity.Country;
import tr.com.turbulence6th.security.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class InjectionController {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InjectionController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = '" + username + "' AND password = '"
                + password + "'", (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }).stream()
            .findAny()
            .map(u -> "Successful login as " + u.getUsername())
            .orElse("Login failed");
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

    @GetMapping("/user/read")
    public List<String> read(@RequestParam String doc) throws IOException {
        Process process = Runtime.getRuntime().exec("find", doc.split(" "));
        return lines(process.getInputStream());
    }

    private List<String> lines(InputStream is) {
        return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.toList());
    }
}
