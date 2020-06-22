package it.unimib.bicap.model;

public class Post {
    private boolean terminata;

    public Post(boolean terminata){
        this.terminata = terminata;
    }

    public boolean isTerminata() {
        return terminata;
    }

    public void setTerminata(boolean terminata) {
        this.terminata = terminata;
    }
}
