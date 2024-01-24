package com.example.myapplication;

public class stepsItem {

    String stepNum;
    String instruction;

    public stepsItem(String stepNum, String instruction) {
        this.stepNum = stepNum;
        this.instruction = instruction;
    }

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
