package br.com.primeit.pokedex.database;

import android.os.AsyncTask;

import java.util.List;

import br.com.primeit.pokedex.database.RecyclerView.PokemonSalvoAdapter;
import br.com.primeit.pokedex.model.PokemonSalvo;

public class BuscaPokemonTask extends AsyncTask<Void, Void, List<PokemonSalvo>>{

    private final PokemonSalvoDao pokemonSalvoDao;
    private final PokemonSalvoAdapter adapter;

    public BuscaPokemonTask(PokemonSalvoDao pokemonSalvoDao, PokemonSalvoAdapter adapter) {
        this.pokemonSalvoDao = pokemonSalvoDao;
        this.adapter = adapter;
    }


    @Override
    protected List<PokemonSalvo> doInBackground(Void... voids) {
        return pokemonSalvoDao.todos();
    }

    @Override
    protected void onPostExecute(List<PokemonSalvo> list) {
        super.onPostExecute(list);
        adapter.atualiza(list);
    }
}
