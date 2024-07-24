package com.example.restservice;

import java.time.DayOfWeek;
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
  public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
    var date = LocalDate.now();
    var dayOfWeek = date.getDayOfWeek();
    var serverVersion = Optional.ofNullable(System.getenv("APP_VERSION")).orElse("");
    var serverBuiltAt = Optional.ofNullable(System.getenv("APP_BUILT_AT")).orElse("");

    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
      return new Greeting(
          counter.incrementAndGet(), "It's the weekend!", serverVersion, serverBuiltAt);
    }
    if (dayOfWeek == DayOfWeek.FRIDAY) {
      return new Greeting(counter.incrementAndGet(), "It's Friday!", serverVersion, serverBuiltAt);
    }
    return new Greeting(
        counter.incrementAndGet(), "Not Friday yet...", serverVersion, serverBuiltAt);
  }
}
