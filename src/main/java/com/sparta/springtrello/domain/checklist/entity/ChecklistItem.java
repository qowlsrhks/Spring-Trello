package com.sparta.springtrello.domain.checklist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springtrello.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "checklist_items")
public class ChecklistItem extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean completed;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;

    public ChecklistItem(String content, Checklist checklist) {
        this.content = content;
        this.completed = false;
        this.checklist = checklist;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
    }


    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public void setCompleted(boolean completed) {
        this.completed = !this.completed;
    }
}
