package com.example.mybookshopapp.data.payments;

import java.time.LocalDateTime;

public class PaymentPayload {
    private Integer sum;
    private LocalDateTime time;

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
