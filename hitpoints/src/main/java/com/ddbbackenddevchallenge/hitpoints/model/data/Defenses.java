package com.ddbbackenddevchallenge.hitpoints.model.data;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.ddbbackenddevchallenge.hitpoints.model.data.Player.Defense;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Defenses {
    private @Id String name;
    private List<DamageType> resistances;
    private List<DamageType> immunities;

    public Defenses() {
    }

    /**
     * @param name
     */
    public Defenses(final String name) {
        this(name, null);
    }

    /**
     * @param name
     * @param defenses
     */
    public Defenses(final String name, final List<Defense> defenses) {
        this.name = name;
        this.resistances = new ArrayList<>();
        this.immunities = new ArrayList<>();

        if (defenses != null) {
            for (final Defense defense : defenses) {
                if (defense.getDefense() == DefenseType.IMMUNITY) {
                    this.immunities.add(defense.getType());
                } else if (defense.getDefense() == DefenseType.RESISTANCE) {
                    this.resistances.add(defense.getType());
                } else {
                    throw new InvalidParameterException("Unrecognized defense type found");
                }
            }
        }
    }

    /**
     * @param damageType
     * @return
     */
    public DefenseType getDefenseForDamageType(final DamageType damageType) {
        if (immunities.contains(damageType)) {
            return DefenseType.IMMUNITY;
        } else if (resistances.contains(damageType)) {
            return DefenseType.RESISTANCE;
        }

        return DefenseType.NONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DamageType> getResistances() {
        return resistances;
    }

    public List<DamageType> getImmunities() {
        return immunities;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((resistances == null) ? 0 : resistances.hashCode());
        result = prime * result + ((immunities == null) ? 0 : immunities.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Defenses other = (Defenses) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (resistances == null) {
            if (other.resistances != null)
                return false;
        } else if (!resistances.equals(other.resistances))
            return false;
        if (immunities == null) {
            if (other.immunities != null)
                return false;
        } else if (!immunities.equals(other.immunities))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Defenses [name=" + name + ", resistances=" + resistances + ", immunities=" + immunities + "]";
    }

}
