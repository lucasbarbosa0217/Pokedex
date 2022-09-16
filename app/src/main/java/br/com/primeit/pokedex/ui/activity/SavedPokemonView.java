package br.com.primeit.pokedex.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.database.BasePokemonTask;
import br.com.primeit.pokedex.database.BuscaPokemonTask;
import br.com.primeit.pokedex.database.DeletePokemonTask;
import br.com.primeit.pokedex.database.GetOnePokemonTask;
import br.com.primeit.pokedex.database.PokemonDatabase;
import br.com.primeit.pokedex.database.PokemonSalvoDao;
import br.com.primeit.pokedex.database.RecyclerView.PokemonSalvoAdapter;
import br.com.primeit.pokedex.database.SavePokemonTask;
import br.com.primeit.pokedex.model.PokemonSalvo;

public class SavedPokemonView {
    private final PokemonSalvoDao dao;
    private final Context context;
    private int numeroPokemon;
    public static List<PokemonSalvo> poke= new ArrayList<>();
    public static Activity view;
    private BasePokemonTask.FinalizadaListener finalizadaListener = new BasePokemonTask.FinalizadaListener() {
        @Override
        public void quandoFinalizada() {

        }
    };

    public SavedPokemonView(Context context, int numero, Activity view) {
        this.context = context;
        this.numeroPokemon = numero;
        view = view;
        dao = PokemonDatabase.getInstance(context).getPokemonDao();
    }

    public static void configurarTela() {
        TextView nome = view.findViewById(R.id.PokemonInfoName);
        nome.setText(poke.get(1).getNomePokemon());
    }

    public void atualizaPokemon() {new GetOnePokemonTask(dao, numeroPokemon, context).execute();}
}
