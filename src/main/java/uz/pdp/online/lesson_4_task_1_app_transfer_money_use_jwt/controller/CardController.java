package uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Card;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Income;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.entity.Outcome;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.payload.CardDto;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.security.JwtProvider;
import uz.pdp.online.lesson_4_task_1_app_transfer_money_use_jwt.service.MyAuthService;

import java.util.*;

@RestController
@RequestMapping("/api/controlCard")
public class CardController {

    @Autowired
    MyAuthService myAuthService;
    @Autowired
    JwtProvider jwtProvider;

    List<Card> cardsList = new ArrayList<>();
    List<Income> incomeList = new ArrayList<>();
    List<Outcome> outcomeList = new ArrayList<>();
    static int id = 1;

    @PostMapping
    public ResponseEntity<?> addCard(@RequestBody Card card) {
        UserDetails userDetails = myAuthService.loadUserByUsername(card.getUsername());
        if (userDetails != null) {
            boolean hasCard = false;
            for (Card itemCard : cardsList) {
                if (itemCard.getNumber().equals(card.getNumber()))
                    hasCard = true;
            }
            if (!hasCard) {
                Card addingCard = new Card(generateId(), card.getUsername(), card.getNumber(), card.getBalance(), card.getExpiredDate(), card.getActive());
                id = 1;
                cardsList.add(addingCard);
                return ResponseEntity.ok("Karta qo'shildi, id raqami: " + addingCard.getId());
            }
            return ResponseEntity.badRequest().body("Bunday karta mavjud, qaytadan urinib ko'ring");
        }
        return ResponseEntity.badRequest().body("Karta qo'shilmadi");
    }

    @GetMapping("/{id}")
    public Card getCard(@PathVariable Integer id) {
        for (Card card : cardsList) {
            if (card.getId().equals(id))
                return card;
        }
        return new Card();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Integer id) {
        for (Card card : cardsList) {
            if (card.getId().equals(id)) {
                cardsList.remove(card);
                return ResponseEntity.ok("Karta o'chirildi");
            }
        }
        return ResponseEntity.badRequest().body("Karta topilmadi");
    }

    public Integer generateId() {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for (Card card1 : cardsList) {
            treeMap.put(card1.getId(), null);
            treeMap.forEach((integer, s) -> {
                if (integer == card1.getId()) {
                    id = integer + 1;
                }
            });
        }
        return id;
    }

    public Integer generateIdForIncomeAndOutcome() {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for (Income income : incomeList) {
            treeMap.put(income.getId(), null);
            treeMap.forEach((integer, s) -> {
                if (integer == income.getId()) {
                    id = integer + 1;
                }
            });
        }
        return id;
    }

    @PostMapping("/transferMoney")
    public ResponseEntity<?> transferMoney(@RequestBody CardDto cardDto) {
        Card myCard = cardsList.get(cardDto.getMyCardId()-1);
        for (Card card : cardsList) {
            if (card.getNumber().equals(cardDto.getReceivedCardNumber())) {
                if (!myCard.getNumber().equals(cardDto.getReceivedCardNumber())) {
                    double realMoney = cardDto.getTransferSum() + (cardDto.getTransferSum() * 1 / 100);
                    if (myCard.getBalance() - realMoney >= 0) {
                        card.setBalance(cardDto.getTransferSum());
                        myCard.setBalance(myCard.getBalance() - realMoney);
                        Income income = new Income(generateIdForIncomeAndOutcome(),myCard.getId(),card.getId(),cardDto.getTransferSum(),new Date());
                        Outcome outcome = new Outcome(generateIdForIncomeAndOutcome(),myCard.getId(),card.getId(),cardDto.getTransferSum(),new Date(),realMoney-cardDto.getTransferSum());
                        incomeList.add(income);
                        outcomeList.add(outcome);
                        return ResponseEntity.ok("Pul o'tkazildi, sizning balansingiz: " + myCard.getBalance()+". To'lov tarixi: "+outcome);
                    } else {
                        return ResponseEntity.badRequest().body("Hisobingizda mablag' yetarli emas");
                    }
                }
                return ResponseEntity.badRequest().body("Siz o'zingizni kartangizga o'zingiz pul transfer qila olmaysiz");
            }
//            return ResponseEntity.badRequest().body("Siz pul yubormoqchi bo'lgan karta raqami MO dan topilmadi");
        }
        return ResponseEntity.badRequest().body("Kartalar MO umuman mavjud emas");
    }

}
