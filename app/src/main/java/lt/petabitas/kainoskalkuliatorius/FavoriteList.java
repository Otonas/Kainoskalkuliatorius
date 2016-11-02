package lt.petabitas.kainoskalkuliatorius;


public class FavoriteList {

    //private variables
    int id;
    String pavadinimas;
    String rusis;
    String bePVM;
    String kaina;

    // constructor
    public FavoriteList(int id, String pavadinimas, String rusis, String bePVM, String kaina) {
        this.id = id;
        this.pavadinimas = pavadinimas;
        this.rusis = rusis;
        this.bePVM = bePVM;
        this.kaina = kaina;    }


    public FavoriteList() {

    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPavadinimas() {
        return this.pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public String getRusis() {
        return this.rusis;
    }

    public void setRusis(String rusis) {
        this.rusis = rusis;
    }

    public String getBePVM() {
        return this.bePVM;
    }

    public void setBePVM(String bePVM) {
        this.bePVM = bePVM;
    }

    public String getKaina() {
        return this.kaina;
    }

    public void setKaina(String kaina) {
        this.kaina = kaina;
    }
}



