package MappingClasses;

import jakarta.persistence.*;

@Entity
@Table(name ="item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_item")
    private int id_item;

    @Column(name="id_film")
    private int id_film;

    @ManyToOne
    @JoinColumn(name="id_tranzakcji")
    private Tranzakcje tranzakcja;

    public Item(){}

    public Item(int id_film, Tranzakcje tranzakcja) {
        this.id_film = id_film;
        this.tranzakcja = tranzakcja;
    }

    public int getId_film() {
        return id_film;
    }

    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    public Tranzakcje getTranzakcja() {
        return tranzakcja;
    }

    public void setTranzakcja(Tranzakcje tranzakcja) {
        this.tranzakcja = tranzakcja;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id_item=" + id_item +
                ", id_film=" + id_film +
                ", tranzakcja=" + tranzakcja +
                '}';
    }
}
