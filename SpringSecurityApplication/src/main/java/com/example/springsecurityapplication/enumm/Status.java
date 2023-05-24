package com.example.springsecurityapplication.enumm;

public enum Status {
    // Принят, Оормлен, Ожидает, Получен
    Accepted("Принят"),
    Issued("Оформлен"),
    Waiting("Ожидает"),
    Received("Получен");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
