package istarwyh.schelule.example;

/**
 * @author mac
 */
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskExampleEventPublisher {
  private final ApplicationEventPublisher publisher;

  public void publishTaskEvent(TaskExampleEvent event) {
    publisher.publishEvent(event);
  }
}
