package com.sparta.springtrello.domain.card.service;

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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
}
