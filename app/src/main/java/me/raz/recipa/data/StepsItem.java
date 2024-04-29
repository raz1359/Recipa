package me.raz.recipa.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepsItem {

    private String stepNum;
    private String instruction;

    public StepsItem(String stepNum, String instruction) {
        this.stepNum = stepNum;
        this.instruction = instruction;
    }

}
