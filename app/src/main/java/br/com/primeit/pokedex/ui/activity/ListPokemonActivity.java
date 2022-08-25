package br.com.primeit.pokedex.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import br.com.primeit.pokedex.R;
import br.com.primeit.pokedex.model.Pokemon;
import br.com.primeit.pokedex.model.ResponsePokemon;
import br.com.primeit.pokedex.retrofit.PokedexRetrofit;
import br.com.primeit.pokedex.retrofit.service.PokemonService;
import br.com.primeit.pokedex.ui.recyclerview.adapter.ListPokemonAdapter;
import retrofit2.Call;
import retrofit2.Callback;

public class ListPokemonActivity extends AppCompatActivity {
    private final ListPokemonAdapter adapter = new ListPokemonAdapter(this);
    private int offset;
    private int limit;
    private ArrayList<Pokemon> pokemons;
    private EditText searchEditText;
    public ProgressDialog dialog;
    public TabLayout tabLayout;
    public TabLayout.Tab tab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_pokemon_activity);
       setTheme(R.style.Theme_Splash);

        Objects.requireNonNull(getSupportActionBar()).hide();
        offset = 0;
        limit = 898;
        searchPokemon(limit, offset);
        tabLayout = findViewById(R.id.tabLayout);
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        ImageButton deleteText = findViewById(R.id.deleteText);
        deleteText.setOnClickListener(view -> searchEditText.setText(""));
        ImageButton fab = findViewById(R.id.reloadList);
        setReloadButton(fab);
        setSearchBar();
        setTabLayout(tab1);

        FloatingActionButton openSaved = findViewById(R.id.openSavedAcitivy);
        openSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent savedIntent = new Intent(ListPokemonActivity.this, SavedListPokemonActivity.class);
                startActivity(savedIntent);
            }
        });
    }

    private void setTabLayout(TabLayout.Tab tab1) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(hasConnectivity()){
                switch(tab.getPosition()) {
                    case 0: searchEditText.setText("");adapter.filterList(pokemons.subList(0,898));break;
                    case 1: searchEditText.setText("");adapter.filterList(pokemons.subList(0,151));break;
                    case 2: searchEditText.setText("");adapter.filterList(pokemons.subList(151,251));break;
                    case 3: searchEditText.setText("");adapter.filterList(pokemons.subList(251,386));break;
                    case 4: searchEditText.setText("");adapter.filterList(pokemons.subList(386,493));break;
                    case 5: searchEditText.setText("");adapter.filterList(pokemons.subList(493,649));break;
                    case 6: searchEditText.setText("");adapter.filterList(pokemons.subList(649,721));break;
                    case 7: searchEditText.setText("");adapter.filterList(pokemons.subList(721,809));break;
                    case 8: searchEditText.setText("");adapter.filterList(pokemons.subList(809,898));break;


                }}else{
                    Toast.makeText(ListPokemonActivity.this, "Erro de Conexão", Toast.LENGTH_SHORT).show();
                    tabLayout.selectTab(tab1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private void setSearchBar() {
        searchEditText=findViewById(R.id.edittext);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void setReloadButton(ImageButton fab) {
        fab.setOnClickListener(view -> {

            StartLoadingScreen();
            searchPokemon(limit, offset);
        });
    }

    private void StartLoadingScreen() {
        dialog = new ProgressDialog(ListPokemonActivity.this);
        dialog.setMessage("Loading data...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }


    public void searchPokemon(int limit, int offset){
        PokemonService service = new PokedexRetrofit().getPokemonService();
        Call<ResponsePokemon> responseCall = service.searchAll(limit, offset);

        responseCall.enqueue(new Callback<ResponsePokemon>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePokemon> call, @NonNull retrofit2.Response<ResponsePokemon> response) {
                if (response.isSuccessful()){
                    ResponsePokemon resposta = response.body();
                    assert resposta != null;
                    pokemons = resposta.getResults();
                    adapter.update(pokemons);
                    configureRecyclerView();
                }else{
                    Toast.makeText(ListPokemonActivity.this, "Erro de Conexão", Toast.LENGTH_SHORT).show();
                }
                if(dialog != null){
                if(dialog.isShowing()){
                    dialog.hide();
                }}
            }
            @Override
            public void onFailure(@NonNull Call<ResponsePokemon> call, @NonNull Throwable t) {
                Toast.makeText(ListPokemonActivity.this, "Erro De Conexão", Toast.LENGTH_SHORT).show();
                if(dialog != null){
                    if(dialog.isShowing()){
                        dialog.hide();
                    }}
            }
        });
    }



    public void configureRecyclerView(){
        RecyclerView listPokemon = findViewById(R.id.saved_recycler);
        listPokemon.setAdapter(adapter);
        listPokemon.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listPokemon.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener((position, pokemon) -> {

            if (hasConnectivity()) {
                Intent intent = new Intent(ListPokemonActivity.this, PokemonInfoActivity.class);
                intent.putExtra("pokemon", pokemon);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);

            }else{
                Toast.makeText(ListPokemonActivity.this, "Erro de Conexão", Toast.LENGTH_SHORT).show();

            }
            setTheme(R.style.Theme_Pokedex);
        });

    }


    public boolean hasConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null;
    }

    private void filter(String text){
        ArrayList<Pokemon> filteredList=new ArrayList<>();
        if (pokemons!=null) {
            for (Pokemon p : pokemons) {
                if (p.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(p);
                }
            }
            if(text.equals("")){
                tabLayout.selectTab(tab1);
            }
            adapter.filterList(filteredList);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.update(pokemons);
        searchEditText.setText("");
    }

}