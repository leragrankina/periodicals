package ua.nure.grankina.periodicals.model.db.entity;

/**
 * Enitity for publication periodicy
 *
 * Created by Valeriia on 01.01.2017.
 */
public class Period extends NamedEntity{

    public Period(){ }

    public static Period valueOf(String s){
        Period p = new Period();
        p.setName(s);
        return p;
    }

}
