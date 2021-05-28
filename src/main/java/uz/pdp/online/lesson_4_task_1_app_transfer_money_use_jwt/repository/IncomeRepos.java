package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Income;

public interface IncomeRepos extends JpaRepository<Income, Integer> {
}
