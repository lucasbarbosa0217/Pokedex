package br.com.primeit.pokedex.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.database.BasePokemonTask;
import br.com.primeit.pokedex.database.BuscaPokemonTask;
import br.com.primeit.pokedex.database.PokemonDatabase;
import br.com.primeit.pokedex.database.PokemonSalvoDao;
import br.com.primeit.pokedex.database.RecyclerView.PokemonSalvoAdapter;
import br.com.primeit.pokedex.database.SavePokemonTask;
import br.com.primeit.pokedex.model.PokemonSalvo;

public class SavedListPokemonView {
    private final PokemonSalvoAdapter adapter;
    private final PokemonSalvoDao dao;
    private final Context context;
    private BasePokemonTask.FinalizadaListener finalizadaListener = new BasePokemonTask.FinalizadaListener() {
        @Override
        public void quandoFinalizada() {

        }
    };

    public SavedListPokemonView(Context context) {
        this.context = context;
        this.adapter = new PokemonSalvoAdapter(this.context);
        dao = PokemonDatabase.getInstance(context).getPokemonDao();
    }

    public void atualizaPokemon() {new BuscaPokemonTask(dao, adapter).execute();}


    public void configuraAdapter(ListView listaPokemon) {listaPokemon.setAdapter(adapter);}

    public void salvaMon (PokemonSalvo pokemon){
        new SavePokemonTask(dao, pokemon, finalizadaListener).execute();
    }

}
