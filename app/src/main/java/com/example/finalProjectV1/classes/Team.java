package com.example.finalProjectV1.classes;

public class Team extends Competitor{
    protected ShortUser competitor2;


    public Team (){
        super();
        competitor2 = new ShortUser();
    }

    public Team(Competitor competitor, Competitor competitor1) {
        super(competitor);
        competitor2 =competitor1;
    }

    public ShortUser getCompetitor2() {
        return competitor2;
    }

    public void setCompetitor2(ShortUser competitor2) {
        this.competitor2 = competitor2;
    }
    public Competitor Pcopy(){
        Team copy =new Team();
        copy.competitor2 =new ShortUser(competitor2.getId(),competitor2.getName());
        copy.name=this.name;
        copy.userId=this.userId;

        return copy;
    }
    public Team(String name1, String id1, String name2, String id2) {
        super(name1, id1);
        this.competitor2 =new ShortUser(name2,id2);
    }

}
