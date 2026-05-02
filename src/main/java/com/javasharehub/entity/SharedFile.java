package com.javasharehub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shared_files")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SharedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String storedName;

    private String fileType;
    private Long fileSize;

    @Column(nullable = false)
    private String status; // PUBLIC / PRIVATE

    private String category;

    @Column(nullable = false)
    private Integer downloadCount = 0;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<PrivateLink> privateLinks;
}