package ua.nure.grankina.periodicals.model.db.entity;

import org.apache.log4j.Logger;

/**
 * Parent for all entities with an id and a name
 *
 * Created by Valeriia on 11.01.2017.
 */
public class NamedEntity extends Entity{
    private String name;
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.model.db.entity.NamedEntity.class);

    protected NamedEntity(String name){this.name = name;}
    public NamedEntity(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntity that = (NamedEntity) o;
        log.debug("Comparing --> " + that + " to " + name);
        log.debug(String.format("Comparing --> %d to %d", getId(), that.getId()));
        return name.equals(that.name) && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return name.hashCode() + getId().intValue();
    }

    @Override
    public String toString() {
        return name;
    }

}
