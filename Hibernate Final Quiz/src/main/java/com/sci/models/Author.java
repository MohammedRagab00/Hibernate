package com.sci.models;

import java.io.Serializable;
import java.util.List;
<<<<<<< HEAD:Template/src/main/java/com/sci/models/RoomType.java

import jakarta.persistence.*;

=======
import javax.persistence.*;
>>>>>>> 2761105d78135125d9e54ac15b7fdcf8d2ce19b0:Hibernate Final Quiz/src/main/java/com/sci/models/Author.java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "Author")
@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor

public class Author implements Serializable {

    private static final long serialVersionUID = 20L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id" , insertable = false, updatable = false)
    private List<Auth_Book> authAndBook;
}
