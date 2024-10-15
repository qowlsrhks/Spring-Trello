package com.sparta.springtrello.domain.card.service;

import com.sparta.springtrello.domain.card.dto.CardArrangeRequestDto;
import com.sparta.springtrello.domain.card.dto.CardRequestDto;
import com.sparta.springtrello.domain.card.dto.CardResponseDto;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.cardList.dto.CardListResponseDto;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.cardList.repository.CardListRepository;
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

    public CardResponseDto createCard(Long id, CardRequestDto requestDto) {
        CardList cardList = listRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리스트입니다.")
        );

        Card card = new Card();
        card.setCardName(requestDto.getCardName());
        card.setCardDescription(requestDto.getCardDescription());
        card.setClosingAt(requestDto.getClosingAt());
        card.setCardList(cardList);

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

    public Long arrangeCard(CardArrangeRequestDto requestDto) {
        Long prevCardId = requestDto.getPrevCardId();
        Long cardId = requestDto.getCardId();

        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 ID 입니다.")
        );

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

        return cardRepository.save(card).getCardId();
    }
}
