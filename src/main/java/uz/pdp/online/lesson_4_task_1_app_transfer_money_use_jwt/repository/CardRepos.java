package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Card;

public interface CardRepos extends JpaRepository<Card, Integer> {
    Card findByNumber(String number);

    boolean existsByNumber(String number);
}
