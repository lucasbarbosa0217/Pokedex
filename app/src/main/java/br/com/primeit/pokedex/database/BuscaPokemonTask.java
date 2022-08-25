package br.com.primeit.pokedex.database;

import android.os.AsyncTask;

import java.util.List;

import br.com.primeit.pokedex.model.PokemonSalvo;

public class BuscaPokemonTask extends AsyncTask<Void, Void, List<PokemonSalvo>>{

    private final PokemonSalvoDao pokemonSalvoDao;

    public BuscaPokemonTask(PokemonSalvoDao pokemonSalvoDao) {
        this.pokemonSalvoDao = pokemonSalvoDao;
    }


    @Override
    protected List<PokemonSalvo> doInBackground(Void... voids) {
        return pokemonSalvoDao.todos();
    }

    @Override
    protected void onPostExecute(List<PokemonSalvo> list) {
        super.onPostExecute(list);
    }
}
