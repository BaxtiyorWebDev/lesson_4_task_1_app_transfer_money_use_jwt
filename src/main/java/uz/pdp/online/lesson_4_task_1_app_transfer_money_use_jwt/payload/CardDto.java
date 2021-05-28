package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.payload;

import lombok.Data;

@Data
public class CardDto {
    private Integer myCardId;
    private Double transferSum;
    private String receivedCardNumber;
}
