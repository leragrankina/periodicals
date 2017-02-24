package ua.nure.grankina.periodicals.model.db.entity;

/**
 * Class for theme of a periodcal
 *
 * Created by Valeriia on 01.01.2017.
 */
public class Theme extends NamedEntity{

    private Theme(String name){
        super(name);
    }

    public Theme(){}

    public static Theme valueOf(String s){
        Theme t = new Theme(s);
        return t;
    }

    @Override
    public String toString() {
        return getId() + ": " + getName();
    }
}
