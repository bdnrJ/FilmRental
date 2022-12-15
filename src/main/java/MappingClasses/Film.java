package MappingClasses;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name ="film")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_filmu")
    private int id;

    @Column(name = "tytul")
    private String tytul;

    @Column(name = "time")
    private int czasTrwania;

    @Column(name = "lang")
    private String jezyk;

    @Column(name = "data_premiery")
    private Date dataPremiery;

    @Column(name = "kraj")
    private String kraj;

    public Film(String tytul, int czasTrwania, String jezyk, Date dataPremiery, String kraj) {
        this.tytul = tytul;
        this.czasTrwania = czasTrwania;
        this.jezyk = jezyk;
        this.dataPremiery = dataPremiery;
        this.kraj = kraj;
    }

    public Film(){

    }

    public int getId() {
        return id;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public int getCzasTrwania() {
        return czasTrwania;
    }

    public void setCzasTrwania(int czasTrwania) {
        this.czasTrwania = czasTrwania;
    }

    public String getJezyk() {
        return jezyk;
    }

    public void setJezyk(String jezyk) {
        this.jezyk = jezyk;
    }

    public Date getDataPremiery() {
        return dataPremiery;
    }

    public void setDataPremiery(Date dataPremiery) {
        this.dataPremiery = dataPremiery;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", tytul='" + tytul + '\'' +
                ", czasTrwania=" + czasTrwania +
                ", jezyk='" + jezyk + '\'' +
                ", dataPremiery=" + dataPremiery +
                ", kraj='" + kraj + '\'' +
                '}';
    }
}
