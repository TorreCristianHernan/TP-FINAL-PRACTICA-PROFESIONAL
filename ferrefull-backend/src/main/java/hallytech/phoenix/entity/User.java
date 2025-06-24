package hallytech.phoenix.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntityAudit {
    private String email;
//    @JsonIgnore
    private String password;
    private String roles;
    private String firstName;
    private String lastName;
    private String type;
    private String phone;
}
