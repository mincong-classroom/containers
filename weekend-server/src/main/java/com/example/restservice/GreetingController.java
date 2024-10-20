package com.example.restservice;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class GreetingController {

  private final AtomicLong counter = new AtomicLong();

  @GetMapping("/")
  public Greeting greeting(@RequestParam(value = "date", required = false) String userDate) {
    var serverVersion = Optional.ofNullable(System.getenv("APP_VERSION")).orElse("");
    var serverBuiltAt = Optional.ofNullable(System.getenv("APP_BUILT_AT")).orElse("");
    var serverTeam = Optional.ofNullable(System.getenv("APP_TEAM")).orElse("");
    var serverAuthors = Optional.ofNullable(System.getenv("APP_AUTHORS")).orElse("");

    var date = Optional.ofNullable(userDate)
        .map(v -> v.isEmpty() ? LocalDate.now() : LocalDate.parse(v))
        .orElseGet(LocalDate::now);

    var content = switch (date.getDayOfWeek()) {
      case THURSDAY -> "Soon...";
      case FRIDAY -> "Almost, but not yet.";
      case SATURDAY, SUNDAY -> "It's the weekend!";
      default -> "No.";
    };

    return new Greeting(
        counter.incrementAndGet(),
        content,
        serverVersion,
        serverBuiltAt,
        serverTeam,
        serverAuthors);
  }
}
