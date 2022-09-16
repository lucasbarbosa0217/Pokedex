package br.com.primeit.pokedex.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.primeit.pokedex.model.PokemonSalvo;
import br.com.primeit.pokedex.ui.activity.PokemonInfoActivity;
import br.com.primeit.pokedex.ui.activity.SavedPokemonView;

public class GetOnePokemonTask extends AsyncTask<Void, Void, List<PokemonSalvo>>{

    private final PokemonSalvoDao pokemonSalvoDao;
    private final int numeroPokemon;
    private final Context context;

    public GetOnePokemonTask(PokemonSalvoDao pokemonSalvoDao, int numeroPokemon,Context context) {
        this.pokemonSalvoDao = pokemonSalvoDao;
        this.numeroPokemon = numeroPokemon;
        this.context = context;
    }


    @Override
    protected List<PokemonSalvo> doInBackground(Void... voids) {
        return (List<PokemonSalvo>) pokemonSalvoDao.getPokemon();
    }

    @Override
    protected void onPostExecute(List<PokemonSalvo> list) {
        super.onPostExecute(list);
        SavedPokemonView.poke.clear();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getNumeroPokemon() == numeroPokemon){
                SavedPokemonView.poke.add(list.get(i));
            }
        }
        Toast.makeText(context, ""+SavedPokemonView.view, Toast.LENGTH_SHORT).show();
    }
}
