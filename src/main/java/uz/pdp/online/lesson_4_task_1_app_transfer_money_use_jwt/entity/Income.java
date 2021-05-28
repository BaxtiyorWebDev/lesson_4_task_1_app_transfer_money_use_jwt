package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Income {

    private Integer id;

    private Integer fromCardId;

    private Integer toCardId;

    private Double amount;

    private Date date;

}
