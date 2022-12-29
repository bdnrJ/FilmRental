package MappingClasses;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name="tranzakcje")
public class Tranzakcje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private int id;

    @Column(name ="id_uzytkownika")
    private int id_uzytkownika;

    @Column(name ="data_tranzakcji")
    private Date data_tranzakcji;

    @Column(name ="data_zwrotu")
    private Date data_zwrotu;

    @Column(name ="koszt")
    private int koszt;

    @Column(name ="isDone")
    private boolean isDone;

    @Column(name ="probyKontaktu")
    private int probyKontaktu;

    @Column(name="filmyCounter")
    private int filmyCounter;

    @OneToMany(mappedBy = "tranzakcja", targetEntity= MappingClasses.Item.class, cascade = CascadeType.ALL)
    private Set<Item> items;

    public Tranzakcje() {
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public int getFilmyCounter() {
        return filmyCounter;
    }

    public void setFilmyCounter(int filmyCounter) {
        this.filmyCounter = filmyCounter;
    }

    public int getId_uzytkownika() {
        return id_uzytkownika;
    }

    public void setId_uzytkownika(int id_uzytkownika) {
        this.id_uzytkownika = id_uzytkownika;
    }


    public Date getData_tranzakcji() {
        return data_tranzakcji;
    }

    public void setData_tranzakcji(Date data_tranzakcji) {
        this.data_tranzakcji = data_tranzakcji;
    }

    public Date getData_zwrotu() {
        return data_zwrotu;
    }

    public void setData_zwrotu(Date data_zwrotu) {
        this.data_zwrotu = data_zwrotu;
    }

    public int getKoszt() {
        return koszt;
    }

    public void setKoszt(int koszt) {
        this.koszt = koszt;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getProbyKontaktu() {
        return probyKontaktu;
    }

    public void setProbyKontaktu(int probyKontaktu) {
        this.probyKontaktu = probyKontaktu;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Tranzakcje{" +
                "id=" + id +
                ", id_uzytkownika=" + id_uzytkownika +
                ", data_tranzakcji=" + data_tranzakcji +
                ", data_zwrotu=" + data_zwrotu +
                ", koszt=" + koszt +
                ", isDone=" + isDone +
                ", probyKontaktu=" + probyKontaktu +
                ", filmyCounter=" + filmyCounter +
                '}';
    }
}
