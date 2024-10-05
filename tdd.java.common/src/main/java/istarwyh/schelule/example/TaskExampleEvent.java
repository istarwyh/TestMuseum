package istarwyh.schelule.example;

import istarwyh.schelule.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author mac
 */
@Getter
@RequiredArgsConstructor
public class TaskExampleEvent {

    private final Task<?> task;
}
