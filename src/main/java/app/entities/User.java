package app.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user")
@Data

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userName;
    String password;

}
