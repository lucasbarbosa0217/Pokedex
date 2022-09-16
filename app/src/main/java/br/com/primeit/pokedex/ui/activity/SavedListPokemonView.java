package br.com.primeit.pokedex.ui.activity;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Objects;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.database.BasePokemonTask;
import br.com.primeit.pokedex.database.BuscaPokemonTask;
import br.com.primeit.pokedex.database.DeletePokemonTask;
import br.com.primeit.pokedex.database.PokemonDatabase;
import br.com.primeit.pokedex.database.PokemonSalvoDao;
import br.com.primeit.pokedex.database.RecyclerView.PokemonSalvoAdapter;
import br.com.primeit.pokedex.database.SavePokemonTask;
import br.com.primeit.pokedex.model.PokemonSalvo;
import br.com.primeit.pokedex.model.info.PokemonInfo;

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

    public void deleteMon (PokemonSalvo pokemonSalvo){
        new DeletePokemonTask(dao, pokemonSalvo, adapter).execute();

    }


    public void allowDelete(MenuItem item, Context context) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        PokemonSalvo pokemonSalvo = (PokemonSalvo) adapter.getItem(menuInfo.position);
        deleteMon(pokemonSalvo);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(context.getFilesDir()  + "/saved_pokemon/Image-"+pokemonSalvo.getNumeroPokemon()+".png").getAbsoluteFile();
        File myDir2 = new File(context.getFilesDir()  + "/saved_pokemon/ImageTiny-"+pokemonSalvo.getNumeroPokemon()+".png").getAbsoluteFile();

        if(myDir.exists()){
            myDir.delete();
        }
        if(myDir2.exists()){
            myDir2.delete();
        }
    }
}
