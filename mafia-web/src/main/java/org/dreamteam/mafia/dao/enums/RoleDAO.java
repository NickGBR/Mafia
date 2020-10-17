/*package org.dreamteam.mafia.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dreamteam.mafia.dao.converter.RoleConverter;
import org.dreamteam.mafia.dao.converter.RoleEnum;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")

public class RoleDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "role", nullable = false)
    @Convert(converter = RoleConverter.class)
    private RoleEnum role;

    @Override
    public String toString() {
        return "Role{" + role +
                '}';
    }

    public RoleDAO(RoleEnum role) {
        this.role = role;
    }
}*/
