package br.com.primeit.pokedex.database;

import android.os.AsyncTask;

import br.com.primeit.pokedex.database.RecyclerView.PokemonSalvoAdapter;
import br.com.primeit.pokedex.model.PokemonSalvo;


public class DeletePokemonTask extends AsyncTask<Void, Void, Void> {

    private final PokemonSalvoDao pokemonSalvoDao;
    private final PokemonSalvoAdapter adapter;
    private final PokemonSalvo pokemonSalvo;

    public DeletePokemonTask(PokemonSalvoDao pokemonSalvoDao, PokemonSalvo pokemonSalvo, PokemonSalvoAdapter adapter) {

        this.pokemonSalvoDao = pokemonSalvoDao;
        this.pokemonSalvo = pokemonSalvo;
        this.adapter = adapter;
    }




    @Override
    protected Void doInBackground(Void... voids) {
        pokemonSalvoDao.remove(pokemonSalvo);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        adapter.remove(pokemonSalvo);
    }
}