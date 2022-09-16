package br.com.primeit.pokedex.database.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.primeit.pokedex.R;
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
        vincula(viewCriada, pokemonDevolvido);
        return viewCriada;

    }

    private void vincula(View view, PokemonSalvo pokemon){
        TextView nomePokemon = view.findViewById(R.id.pokemon_name);
        TextView numberPokemon = view.findViewById(R.id.pokemon_number);
        ImageView imagePokemon = view.findViewById(R.id.pokemon_image);
        File myDir = new File(context.getFilesDir()  + "/saved_pokemon/ImageTiny-"+pokemon.getNumeroPokemon()+".png").getAbsoluteFile();

        Glide.with(context)
                .load(myDir)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePokemon);
        nomePokemon.setText(pokemon.getNomePokemon());
        numberPokemon.setText(""+pokemon.getNumeroPokemon());
    }

    private View criaView(ViewGroup viewGroup)
    {
    return LayoutInflater.from(context).inflate(R.layout.pokemon_list_item, viewGroup, false);
    }

    public void atualiza(List<PokemonSalvo> pokemons){
        this.pokemons.clear();
        this.pokemons.addAll(pokemons);
        notifyDataSetChanged();
    }

    public void remove(PokemonSalvo pokemon){
        pokemons.remove(pokemon);
        notifyDataSetChanged();
    }
}
