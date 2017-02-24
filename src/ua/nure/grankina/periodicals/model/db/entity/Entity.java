package ua.nure.grankina.periodicals.model.db.entity;

/**
 * Parent for all entities with an id
 *
 * Created by Valeriia on 01.01.2017.
 */
public abstract class Entity {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
