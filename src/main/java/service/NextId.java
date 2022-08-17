package service;

public class NextId implements IdManager {

    private int id;


    public NextId () {

        this.id = 1;
    }

    @Override
    public int getId() {

        return id++;
    }
    @Override
    public void setId(int id) {

        this.id = id;
    }

}
