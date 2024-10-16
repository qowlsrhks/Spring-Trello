package com.sparta.springtrello.domain.checklist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "checklists")
public class Checklist extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> items = new ArrayList<>();

    public Checklist(String title, Card card) {
        this.title = title;
        this.card = card;
    }

    public Checklist(Long checklistId) {
        this.id = checklistId;
    }

    public void updateName(String title) {
        this.title = title;
    }

    public void addItem(ChecklistItem item) {
        items.add(item);
        item.setChecklist(this);
    }

    public void removeItem(ChecklistItem item) {
        items.remove(item);
        item.setChecklist(null);
    }
}
