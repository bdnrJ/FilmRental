package MappingClasses;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class Uzytkownik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="imie")
    private String imie;

    @Column(name ="nazwisko")
    private String nazwisko;

    @Column(name ="Nr_tel")
    private String Nr_tel;

    @Column(name ="haslo")
    private String haslo;

    @Column(name ="isAdmin")
    private int isAdmin;

    public Uzytkownik(){

    }

    public Uzytkownik(int id, String imie, String nazwisko, String nr_tel) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        Nr_tel = nr_tel;
    }

    public int getId() {
        return id;
    }


    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getNr_tel() {
        return Nr_tel;
    }

    public void setNr_tel(String nr_tel) {
        Nr_tel = nr_tel;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    @Override
    public String toString() {
        return "Uzytkownik{" +
                "id=" + id +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", Nr_tel='" + Nr_tel + '\'' +
                ", haslo='" + haslo + '\'' +
                '}';
    }
}
