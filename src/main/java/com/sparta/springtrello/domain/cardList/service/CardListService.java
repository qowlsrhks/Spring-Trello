package com.sparta.springtrello.domain.cardList.service;

import com.sparta.springtrello.domain.cardList.dto.CardListArrangeRequestDto;
import com.sparta.springtrello.domain.cardList.dto.CardListRequestDto;
import com.sparta.springtrello.domain.cardList.dto.CardListResponseDto;
import com.sparta.springtrello.domain.cardList.entity.CardList;
import com.sparta.springtrello.domain.cardList.repository.CardListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CardListService {
    private final CardListRepository listRepository;

    public CardListResponseDto createList(CardListRequestDto cardListRequestDto) {
        // next가 null인 어트리뷰트 찾아서 이어 붙이기.

        List<CardList> lastList = listRepository.findAll()
                .stream().filter(cl -> cl.getNextListId() == null)
                .toList();

        CardList cardList = new CardList();
        cardList.setListName(cardListRequestDto.getListName());

        if(!lastList.isEmpty()) {
            cardList.setPrevListId(lastList.get(0).getListId());
        }

        CardList savedCardList = listRepository.save(cardList);

        if(!lastList.isEmpty()) {
            lastList.get(0).setNextListId(savedCardList.getListId());
            listRepository.save(lastList.get(0));
        }

        return new CardListResponseDto(savedCardList);
    }

    public Long deleteList(Long id) {
        CardList cardList = listRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 리스트 id 입니다.")
        );

        // 이전, 다음거도 바꿔야 함.
        if(cardList.getNextListId() != null) {
            CardList nextCardList = listRepository.findById(cardList.getNextListId()).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 리스트 id 입니다.")
            );
            nextCardList.setPrevListId(cardList.getPrevListId());
            listRepository.save(nextCardList);
        }

        if(cardList.getPrevListId() != null) {
            CardList prevCardList = listRepository.findById(cardList.getPrevListId()).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 리스트 id 입니다.")
            );
            prevCardList.setNextListId(cardList.getNextListId());
            listRepository.save(prevCardList);
        }

        listRepository.deleteById(id);
        return id;
    }

    public CardListResponseDto updateList(Long id, CardListRequestDto cardListRequestDto) {
        CardList cardList = listRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 ID 입니다.")
        );

        cardList.setListName(cardListRequestDto.getListName());
        CardList savedCardList = listRepository.save(cardList);

        return new CardListResponseDto(savedCardList);
    }

    public Long arrangeList(CardListArrangeRequestDto requestDto) {
        // 리스트 조회해서 prev_id에 해당하는 어트리뷰트 찾고, 해당 어트리뷰트 뒤에 붙이기.
        // 카드 재배치 시, 다른 리스트로 옮길 경우, 해당 리스트 내 카드를 조회해서 원하는 위치 찾아야 함.
        Long prevListId = requestDto.getPrevListId();
        Long listId = requestDto.getListId();

        CardList cardList = listRepository.findById(listId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 ID 입니다.")
        );

        List<CardList> lists = listRepository.findAll();


        //이전 위치 앞뒤 노드 연결
        if(cardList.getPrevListId() != null) {
            CardList prevCardList = lists.stream()
                    .filter(cl -> cl.getListId().equals(cardList.getPrevListId())).findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("존재하지 않는 ID 입니다.")
                    );
            prevCardList.setNextListId(cardList.getNextListId());
            listRepository.save(prevCardList);
        }

        if(cardList.getNextListId() != null) {
            CardList nextCardList = lists.stream()
                    .filter(cl -> cl.getListId().equals(cardList.getNextListId())).findFirst()
                    .orElseThrow(
                            () -> new IllegalArgumentException("존재하지 않는 ID 입니다.")
                    );
            nextCardList.setPrevListId(cardList.getPrevListId());
            listRepository.save(nextCardList);
        }

        // 재배치 위치 앞뒤 노드 연결
        if(prevListId == null){
            cardList.setPrevListId(null);
            CardList nextCardList = lists.stream()
                    .filter(cl -> cl.getPrevListId() == null).findFirst().orElse(null);
            if(nextCardList == null) {
                cardList.setNextListId(null);
            }
            else {
                cardList.setNextListId(nextCardList.getListId());
                nextCardList.setPrevListId(cardList.getListId());
                listRepository.save(nextCardList);
            }
        }
        else {
            cardList.setPrevListId(prevListId);
            CardList prevCardList = lists.stream()
                    .filter(cl -> cl.getListId().equals(prevListId)).findFirst().orElse(null);

            if(prevCardList != null) {
                if(prevCardList.getNextListId() != null) {
                    CardList nextCardList = lists.stream()
                            .filter(cl -> cl.getListId().equals(prevCardList.getNextListId())).findFirst().orElse(null);
                    if(nextCardList != null) {
                        cardList.setNextListId(nextCardList.getListId());
                        nextCardList.setPrevListId(prevCardList.getListId());
                        listRepository.save(nextCardList);
                    }
                }
                prevCardList.setNextListId(cardList.getListId());
                listRepository.save(prevCardList);
            }
        }

        return listRepository.save(cardList).getListId();
    }

    public List<CardListResponseDto> viewAllList() {
        List<CardList> list = listRepository.findAll();
        List<CardListResponseDto> cardListResponseDtoList = new ArrayList<>();
        for (CardList cardList : list) {
            CardListResponseDto cardListResponseDto = new CardListResponseDto(cardList);
            cardListResponseDtoList.add(cardListResponseDto);
        }
        return cardListResponseDtoList;
    }

    public List<CardListResponseDto> viewAllListSortedByLinked() {
        List<CardList> lists = listRepository.findAll();
        CardList cardList = lists.stream()
                .filter(cl -> cl.getPrevListId() == null).findFirst().orElseThrow(
                        () -> new IllegalArgumentException("비어있는 보드입니다.")
                );

        Map<Long, CardList> cardListMap = lists.stream().collect(Collectors.toMap(CardList::getListId, cl -> cl));

        List<CardListResponseDto> responseDtoList = new ArrayList<>();

        responseDtoList.add(new CardListResponseDto(cardList));

        while(cardList.getNextListId() != null) {
            cardList = cardListMap.get(cardList.getNextListId());
            responseDtoList.add(new CardListResponseDto(cardList));
        }

        return responseDtoList;
    }
}
