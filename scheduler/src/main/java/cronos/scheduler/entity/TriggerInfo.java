package cronos.scheduler.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TriggerInfo implements Serializable {
    private int triggerCount;
    private boolean isRunForever;
    private Long timeInterval;
    private Long initialOffSet;
    private String info;

}

