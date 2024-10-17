package com.sparta.springtrello.domain.card.service;

import com.sparta.springtrello.domain.activity.common.ActivityLogger;
import com.sparta.springtrello.domain.card.dto.CardArrangeRequestDto;
import com.sparta.springtrello.domain.card.dto.CardRequestDto;
import com.sparta.springtrello.domain.card.dto.CardResponseDto;
import com.sparta.springtrello.domain.card.dto.CardUpdateRequestDto;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.cardList.repository.CardListRepository;
import com.sparta.springtrello.domain.common.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final CardListRepository listRepository;
    private final UserRepository userRepository;
    private final ActivityLogger activityLogger;

    public CardResponseDto createCard(Long id, CardRequestDto requestDto, AuthUser authUser) {
        // 멤버 권한 검증 -> 읽기 멤버인지만 체크하면 될듯
        // 멤버 정보 추가
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저 ID 입니다.")
        );

        List<User> users = new ArrayList<>();

        users.add(user);

        CardList cardList = listRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리스트입니다.")
        );
        Card card = new Card();
        card.setCardName(requestDto.getCardName());
        card.setCardDescription(requestDto.getCardDescription());
        card.setClosingAt(requestDto.getClosingAt());
        card.setCardList(cardList);
        card.setUsers(users);

        List<Card> cards = cardRepository.findAllByCardList(cardList);

        Card savedCard = cardRepository.save(card);

        if(!cards.isEmpty()) {
            Card lastCard = cards.stream().filter(c -> c.getNextCardId() == null).findFirst().orElse(null);

            if (lastCard != null) {
                lastCard.setNextCardId(savedCard.getCardId());
                savedCard.setPrevCardId(lastCard.getCardId());

                cardRepository.save(lastCard);
                cardRepository.save(savedCard);
            }
        }

        activityLogger.logCardCreated(card, user);
        return new CardResponseDto(savedCard);
    }

    public Long deleteCard(Long listId, Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 ID 입니다.")
        );

        CardList list = listRepository.findById(listId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리스트 ID 입니다.")
        );

        List<Card> cards = cardRepository.findAllByCardList(list);

        // 이전, 다음거도 바꿔야 함.
        if(card.getNextCardId() != null) {
            Card nextCard = cards.stream().filter(c -> c.getCardId().equals(card.getNextCardId())).findFirst().orElse(null);
            if(nextCard != null) {
                nextCard.setPrevCardId(card.getPrevCardId());
                cardRepository.save(nextCard);
            }
        }

        if(card.getPrevCardId() != null) {
            Card prevCard = cards.stream().filter(c -> c.getCardId().equals(card.getPrevCardId())).findFirst().orElse(null);
            if(prevCard != null) {
                prevCard.setNextCardId(card.getNextCardId());
                cardRepository.save(prevCard);
            }
        }

        cardRepository.deleteById(cardId);

        return cardId;
    }

    public List<CardResponseDto> viewAllCardByListId(Long id) {
        CardList cardList = listRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리스트 ID 입니다.")
        );
        List<Card> cards = cardRepository.findAllByCardList(cardList);

        Map<Long, Card> cardMap = cards.stream().collect(Collectors.toMap(Card::getCardId, c->c));

        Card card = cards.stream().filter(c -> c.getPrevCardId() == null).findFirst().orElseThrow(
                () -> new IllegalArgumentException("비어있는 리스트입니다.")
        );

        List<CardResponseDto> responseDtoList = new ArrayList<>();

        responseDtoList.add(new CardResponseDto(card));

        while(card.getNextCardId() != null) {
            card = cardMap.get(card.getNextCardId());
            responseDtoList.add(new CardResponseDto(card));
        }

        return responseDtoList;
    }

    public Long arrangeCard(CardArrangeRequestDto requestDto, String email) {
        Long prevCardId = requestDto.getPrevCardId();
        Long cardId = requestDto.getCardId();

        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 ID 입니다.")
        );

        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new IllegalArgumentException("권한이 없습니다."));

        CardList fromCardList = card.getCardList();
        CardList toCardList = listRepository.findById(requestDto.getToListId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리스트 ID 입니다.")
        );

        List<Card> cards = cardRepository.findAll();

        //이전 위치 앞뒤 노드 연결
        if(card.getPrevCardId() != null) {
            Card prevCard = cards.stream()
                    .filter(c -> c.getCardId().equals(card.getPrevCardId()))
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("존재하지 않는 ID 입니다.")
                    );
            prevCard.setNextCardId(card.getNextCardId());
            cardRepository.save(prevCard);
        }

        if(card.getNextCardId() != null) {
            Card nextCard = cards.stream()
                    .filter(cl -> cl.getCardId().equals(card.getNextCardId())).findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("존재하지 않는 ID 입니다.")
                    );
            nextCard.setPrevCardId(card.getPrevCardId());
            cardRepository.save(nextCard);
        }

        List<Card> toCards = cards.stream().filter(c -> c.getCardList().equals(toCardList)).toList();


        // 재배치 위치 앞뒤 노드 연결
        if(prevCardId == null){
            card.setPrevCardId(null);
            Card nextCard = toCards.stream()
                    .filter(c -> c.getPrevCardId() == null).findFirst().orElse(null);
            if(nextCard == null) {
                card.setNextCardId(null);
            }
            else {
                card.setNextCardId(nextCard.getCardId());
                nextCard.setPrevCardId(card.getCardId());
                cardRepository.save(nextCard);
            }
        }
        else {
            card.setPrevCardId(prevCardId);
            Card prevCard = toCards.stream()
                    .filter(cl -> cl.getCardId().equals(prevCardId)).findFirst().orElse(null);

            if(prevCard != null) {
                if(prevCard.getNextCardId() != null) {
                    Card nextCard = toCards.stream()
                            .filter(cl -> cl.getCardId().equals(prevCard.getNextCardId())).findFirst().orElse(null);
                    if(nextCard != null) {
                        card.setNextCardId(nextCard.getCardId());
                        nextCard.setPrevCardId(prevCard.getCardId());
                        cardRepository.save(nextCard);
                    }
                }
                prevCard.setNextCardId(card.getCardId());
                cardRepository.save(prevCard);
            }
        }
        card.setCardList(toCardList);
        activityLogger.logCardMoved(card, user, fromCardList.getListName(), toCardList.getListName());

        return cardRepository.save(card).getCardId();
    }

    public CardResponseDto checkCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 ID 입니다.")
        );
        card.setChecked(!card.isChecked());
        Card checkedCard = cardRepository.save(card);
        return new CardResponseDto(checkedCard);
    }

    public CardResponseDto updateCard(Long id, CardUpdateRequestDto requestDto, AuthUser authUser) {
        // 유저 검증
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 ID 입니다.")
        );

        Map<Long, User> userMap = userRepository.findAll().stream().collect(Collectors.toMap(User::getId, u->u));

        if(requestDto.getCardName() != null) card.setCardName(requestDto.getCardName());
        if(requestDto.getCardDescription() != null) card.setCardDescription(requestDto.getCardDescription());
        if(requestDto.getClosingAt() != null) card.setClosingAt(requestDto.getClosingAt());

        if(requestDto.getAddUsers() != null) {
            List<Long> userIdList = requestDto.getAddUsers();
            List<User> addUsers = card.getUsers();
            for (Long userId : userIdList) {
                addUsers.add(userMap.get(userId));
            }
            card.setUsers(addUsers);
        }

        if(requestDto.getRemoveUsers() != null) {
            List<Long> userIdList = requestDto.getRemoveUsers();
            List<User> removeUsers = card.getUsers();
            for (Long userId : userIdList) {
                removeUsers.remove(userMap.get(userId));
            }
            card.setUsers(removeUsers);
        }

        Card updatedCard = cardRepository.save(card);
        return new CardResponseDto(updatedCard);
    }

    @Transactional
    public CardResponseDto archiveCard(Long cardId, String email) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드 ID 입니다."));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        card.archive();
        if(!card.isArchived()) {
            Card archivedCard = cardRepository.save(card);
            activityLogger.logCardArchived(archivedCard, user);
            return new CardResponseDto(archivedCard);
        } else {
            return new CardResponseDto(card);
        }
    }

    @Transactional
    public CardResponseDto unarchiveCard(Long cardId, String email) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드 ID 입니다."));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        card.unarchive();
        if(card.isArchived()) {
            Card unarchivedCard = cardRepository.save(card);
            activityLogger.logCardUnarchived(unarchivedCard, user);
            return new CardResponseDto(unarchivedCard);
        }

        return new CardResponseDto(card);
    }

    // 카드 조회 시 아카이브 상태 고려
    public List<CardResponseDto> getActiveCardsByList(Long listId) {
        CardList cardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리스트 ID 입니다."));
        List<Card> activeCards = cardRepository.findByCardListAndArchiveFalse(cardList);
        return activeCards.stream().map(CardResponseDto::new).collect(Collectors.toList());
    }

    // 유저 정보 받고 검증
}
