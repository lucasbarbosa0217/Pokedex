package br.com.primeit.pokedex.database;

import android.os.AsyncTask;

import br.com.primeit.pokedex.model.PokemonSalvo;


public class SavePokemonTask extends BasePokemonTask {

    private final PokemonSalvoDao pokemonSalvoDao;
    private final PokemonSalvo pokemonSalvo;

    public SavePokemonTask(PokemonSalvoDao pokemonSalvoDao, PokemonSalvo pokemonSalvo, FinalizadaListener listener) {
        super(listener);

        this.pokemonSalvoDao = pokemonSalvoDao;
        this.pokemonSalvo = pokemonSalvo;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        int PokemonId = pokemonSalvoDao.salva(pokemonSalvo).intValue();
        return null;
    }
}