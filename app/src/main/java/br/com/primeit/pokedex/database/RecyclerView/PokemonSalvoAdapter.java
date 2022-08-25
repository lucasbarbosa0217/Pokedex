package br.com.primeit.pokedex.database.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.primeit.pokedex.database.PokemonDatabase;
import br.com.primeit.pokedex.database.PokemonSalvoDao;
import br.com.primeit.pokedex.model.PokemonSalvo;

public class PokemonSalvoAdapter extends BaseAdapter {
    private final List<PokemonSalvo> pokemons = new ArrayList<>();
    private final Context context;
    private final PokemonSalvoDao dao;

    public PokemonSalvoAdapter(Context context) {
        this.context = context;
        dao = PokemonDatabase.getInstance(context).getPokemonDao();
    }


    @Override
    public int getCount() {
        return pokemons.size();
    }

    @Override
    public Object getItem(int i) {
        return pokemons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pokemons.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewCriada = criaView(viewGroup);
        PokemonSalvo pokemonDevolvido = pokemons.get(i);
     
        return null;
    }

    private View criaView(ViewGroup viewGroup) {
    return (null);
    }
}
