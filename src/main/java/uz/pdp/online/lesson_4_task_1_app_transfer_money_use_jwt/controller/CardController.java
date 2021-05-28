package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Card;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Income;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Outcome;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.payload.CardDto;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.repository.CardRepos;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.repository.IncomeRepos;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.repository.OutcomeRepos;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.security.JwtProvider;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.service.MyAuthService;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/controlCard")
public class CardController {

    @Autowired
    MyAuthService myAuthService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CardRepos cardRepos;
    @Autowired
    IncomeRepos incomeRepos;
    @Autowired
    OutcomeRepos outcomeRepos;

    @PostMapping
    public ResponseEntity<?> addCard(@RequestBody Card card) {
        boolean existsByNumber = cardRepos.existsByNumber(card.getNumber());
        if (existsByNumber)
            return ResponseEntity.badRequest().body("Bunday karta mavjud");
        Card savedCard = cardRepos.save(card);
        return ResponseEntity.ok(savedCard);
    }

    @GetMapping("/{id}")
    public Card getCard(@PathVariable Integer id) {
        Optional<Card> optionalCard = cardRepos.findById(id);
        return optionalCard.orElseGet(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Integer id) {
        try {
            cardRepos.deleteById(id);
            return ResponseEntity.ok("Karta o'chirildi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Karta topilmadi");
        }
    }


    @PostMapping("/transferMoney")
    public ResponseEntity<?> transferMoney(@RequestBody CardDto cardDto) {
        Optional<Card> optionalCard = cardRepos.findById(cardDto.getMyCardId());
        Card cardByNumber = cardRepos.findByNumber(cardDto.getReceivedCardNumber());
        if (optionalCard.isPresent() && cardByNumber != null) {
            Card myCard = optionalCard.get();
            if (!myCard.getNumber().equals(cardByNumber.getNumber())) {
                double realMoney = cardDto.getTransferSum() + (cardDto.getTransferSum() * 1 / 100);
                if (myCard.getBalance() - realMoney >= 0) {
                    cardByNumber.setBalance(cardDto.getTransferSum());
                    myCard.setBalance(myCard.getBalance() - realMoney);
                    cardRepos.save(cardByNumber);
                    cardRepos.save(myCard);

                    Outcome outcome = new Outcome();
                    outcome.setFromCardId(myCard.getId());
                    outcome.setToCardId(cardByNumber.getId());
                    outcome.setAmount(cardDto.getTransferSum());
                    outcome.setDate(new Date());
                    outcome.setCommissionAmount(realMoney-cardDto.getTransferSum());
                    outcomeRepos.save(outcome);

                    Income income = new Income();
                    income.setFromCardId(myCard.getId());
                    income.setToCardId(cardByNumber.getId());
                    income.setAmount(cardDto.getTransferSum());
                    income.setDate(new Date());
                    incomeRepos.save(income);


                    return ResponseEntity.ok("Pul o'tkazildi, sizning balansingiz: " + myCard.getBalance()+". To'lov tarixi: "+outcome);
                }
                return ResponseEntity.badRequest().body("Sizning hisobingizda mablag' yetarli emas");
            }
            return ResponseEntity.badRequest().body("Karta o'ziga o'zi transfer qila olmaydi");
        }
        return ResponseEntity.badRequest().body("Karta ma'lumotlari notog'ri kiritildi.");
    }

}
