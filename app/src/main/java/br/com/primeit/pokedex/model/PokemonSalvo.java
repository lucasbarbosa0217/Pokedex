package br.com.primeit.pokedex.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
@Entity
public class PokemonSalvo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String nomePokemon;
    private int numeroPokemon;
    private String urlFotoGrande;
    private String urlFotoPequena;
    private String desc;
    private String abilidades;
    private String genera;
    private ArrayList<String> tipos;
    private String altura;
    private String peso;

    public String getNomePokemon() {
        return nomePokemon;
    }

    public void setNomePokemon(String nomePokemon) {
        this.nomePokemon = nomePokemon;
    }

    public int getNumeroPokemon() {
        return numeroPokemon;
    }

    public void setNumeroPokemon(int numeroPokemon) {
        this.numeroPokemon = numeroPokemon;
    }

    public String getUrlFotoGrande() {
        return urlFotoGrande;
    }

    public void setUrlFotoGrande(String urlFotoGrande) {
        this.urlFotoGrande = urlFotoGrande;
    }

    public String getUrlFotoPequena() {
        return urlFotoPequena;
    }

    public void setUrlFotoPequena(String urlFotoPequena) {
        this.urlFotoPequena = urlFotoPequena;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAbilidades() {
        return abilidades;
    }

    public void setAbilidades(String abilidades) {
        this.abilidades = abilidades;
    }

    public String getGenera() {
        return genera;
    }

    public void setGenera(String genera) {
        this.genera = genera;
    }

    public ArrayList<String> getTipos() {
        return tipos;
    }

    public void setTipos(ArrayList<String> tipos) {
        this.tipos = tipos;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public long getId() {
        return id;
    }
}
