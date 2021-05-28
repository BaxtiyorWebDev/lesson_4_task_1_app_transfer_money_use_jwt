package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Card {

    private Integer id;

    private String username;

    private String number;

    private Double balance;

    private String expiredDate;

    private Boolean active;

}
