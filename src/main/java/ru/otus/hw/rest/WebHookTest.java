package ru.otus.hw.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@Controller
public class WebHookTest {

    private Logger logger = LoggerFactory.getLogger(WebHookTest.class);

    @PutMapping("/api/webhooktest/{id}")
    public ResponseEntity<String> saveUser(@PathVariable("id") long id, @RequestBody String body) {
        logger.info("Get PUT request on 'api/webhooktest' with id: " + id + " body: " + body);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/api/webhooktest")
    public ResponseEntity<String> insertUser(@RequestBody String body) {
        logger.info("Get POST request on 'api/webhooktest' with body: " + body);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("ОК");
    }

    @DeleteMapping("/api/webhooktest/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        logger.info("Get DELETE request on 'api/webhooktest' with id: " + id);
        return ResponseEntity.ok("deleted");
    }
}
