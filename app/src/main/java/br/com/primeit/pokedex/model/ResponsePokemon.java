package br.com.primeit.pokedex.model;

import java.util.ArrayList;

public class ResponsePokemon {
    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }

    private ArrayList<Pokemon> results;
}
