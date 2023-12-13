package com.ddbbackenddevchallenge.hitpoints.model.data;

import com.google.common.base.Preconditions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Health {
    
    private @Id String name;
    private int currentHitPoints;
    private int temporaryHitPoints;
    private int maxHitPoints;

    public Health() {
    }

    /**
     * Initializes Health object with only a character's maxHitPoints.
     * We're assuming that, given this information, a character is at full health with 0 temporary hit points
     * 
     * @param name
     * @param maxHitPoints
     */
    public Health(final String name, 
            final int maxHitPoints) {
        this(name, maxHitPoints, maxHitPoints, 0);
    }

    /**
     * Initializes Health object with only a character's maximum and current hitpoints.
     * We're assuming that, given this information, a character has 0 temporary hit points
     * 
     * @param name
     * @param maxHitPoints
     * @param currentHitPoints
     */
    public Health(final String name, 
            final int maxHitPoints, 
            final int currentHitPoints) {
        this(name, maxHitPoints, currentHitPoints, 0);
    }

    /**
     * Initializes Health object with all fields
     * Name cannot be null
     * Hit point values cannot be negative.
     * Maximum hit points must be greater than 0
     * Current HP cannot exceed MaxHP.
     * 
     * @param name
     * @param maxHitPoints
     * @param currentHitPoints
     * @param temporaryHitPoints
     */
    public Health(final String name, 
            final int maxHitPoints, 
            final int currentHitPoints, 
            final int temporaryHitPoints) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(maxHitPoints > 0, "Max hit points must be greater than 0: %s", maxHitPoints);
        Preconditions.checkArgument(currentHitPoints >= 0, "Current hit points cannot be negative: %s", currentHitPoints);
        Preconditions.checkArgument(temporaryHitPoints >= 0, "Temporary hit points cannot be negative: %s", temporaryHitPoints);
        Preconditions.checkArgument(currentHitPoints <= maxHitPoints, "Current hit points %s cannot exceed maximum hit points %s", 
            currentHitPoints, maxHitPoints);

        this.name = name;
        this.currentHitPoints = currentHitPoints;
        this.maxHitPoints = maxHitPoints;
        this.temporaryHitPoints = temporaryHitPoints;
    }

    public String getName() {
        return name;
    }



    public int getCurrentHitPoints() {
        return currentHitPoints;
    }



    public void setCurrentHitPoints(final int currentHitPoints) {
        Preconditions.checkArgument(currentHitPoints >= 0, "Current hit points cannot be negative: %s", currentHitPoints);
        Preconditions.checkArgument(currentHitPoints <= this.maxHitPoints, "Current hit points %s cannot exceed maximum hit points %s", 
            currentHitPoints, this.maxHitPoints);

        this.currentHitPoints = currentHitPoints;
    }



    public int getTemporaryHitPoints() {
        return temporaryHitPoints;
    }



    public void setTemporaryHitPoints(final int temporaryHitPoints) {
        Preconditions.checkArgument(temporaryHitPoints >= 0, "Temporary hit points cannot be negative: %s", temporaryHitPoints);

        this.temporaryHitPoints = temporaryHitPoints;
    }



    public int getMaxHitPoints() {
        return maxHitPoints;
    }



    public void setMaxHitPoints(final int maxHitPoints) {
        Preconditions.checkArgument(maxHitPoints > 0, "Max hit points must be greater than 0: %s", maxHitPoints);
        
        this.maxHitPoints = maxHitPoints;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + currentHitPoints;
        result = prime * result + temporaryHitPoints;
        result = prime * result + maxHitPoints;
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
        Health other = (Health) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (currentHitPoints != other.currentHitPoints)
            return false;
        if (temporaryHitPoints != other.temporaryHitPoints)
            return false;
        if (maxHitPoints != other.maxHitPoints)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Health [name=" + name + ", currentHitPoints=" + currentHitPoints + ", temporaryHitPoints="
                + temporaryHitPoints + ", maxHitPoints=" + maxHitPoints + "]";
    }
    
}
