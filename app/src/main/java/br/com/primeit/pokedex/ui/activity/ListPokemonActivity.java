package br.com.primeit.pokedex.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private static ArrayList<Pokemon> pokemons;
    private EditText searchEditText;
    private TabLayout tabLayout;
    public TabLayout.Tab tab1;
    private SwipeRefreshLayout swipeContainer;
    private Pokemon erroConexao = new Pokemon();

    private boolean canResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_pokemon_activity);
       setTheme(R.style.Theme_Splash);
        pokemons = new ArrayList<>();
        erroConexao = new Pokemon();
        erroConexao.setName("Conection error");
        erroConexao.setNumber(1404);
        erroConexao.setUrl("https://cdn3.iconfinder.com/data/icons/wifi-2/460/connection-error-512.png");

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               searchPokemon(limit, offset);

            }
        });

        Objects.requireNonNull(getSupportActionBar()).hide();
        offset = 0;
        limit = 1025;


        searchPokemon(limit, offset);

        tabLayout =findViewById(R.id.tabLayout);
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        ImageButton deleteText = findViewById(R.id.deleteText);
        deleteText.setOnClickListener(view -> searchEditText.setText(""));
        ImageButton fab = findViewById(R.id.reloadList);
        setReloadButton(fab);
        setSearchBar();
        setTabLayout(tabLayout);
    }

    private void setTabLayout(TabLayout tab) {
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
                    case 9: searchEditText.setText("");adapter.filterList(pokemons.subList(898,905));break;
                    case 10: searchEditText.setText("");adapter.filterList(pokemons.subList(905,1024));break;


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
            Intent savedIntent = new Intent(ListPokemonActivity.this, SavedListPokemonActivity.class);
            startActivity(savedIntent);
        });
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
                    pokemons.clear();
                    pokemons = resposta.getResults();
                    adapter.clear();
                    adapter.update(pokemons);
                    configureRecyclerView();
                    canResume = true;
                }else{
                    Toast.makeText(ListPokemonActivity.this, "Erro de Conexão", Toast.LENGTH_SHORT).show();
                }
                swipeContainer.setRefreshing(false);
            }
            @Override
            public void onFailure(@NonNull Call<ResponsePokemon> call, @NonNull Throwable t) {
                if(pokemons.size()<200){
                pokemons.clear();
                pokemons.add(erroConexao);
                adapter.clear();
                adapter.update(pokemons);
                configureRecyclerView();}
                Toast.makeText(ListPokemonActivity.this, "Erro De Conexão", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
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
        boolean isPoke = true;
        if( pokemons.size() < 100){
            isPoke = false;
        }
        if(activeNetwork != null & isPoke){return true;}
        else{return false;}

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
        if(pokemons!=null){
        adapter.update(pokemons);}
        searchEditText.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canResume) {
            Toast.makeText(ListPokemonActivity.this, ""+tabSelecionada, Toast.LENGTH_SHORT).show();

            switch(tabSelecionada) {
                case 0: searchEditText.setText("");adapter.filterList(pokemons.subList(0,898));break;
                case 1: searchEditText.setText("");adapter.filterList(pokemons.subList(0,151));break;
                case 2: searchEditText.setText("");adapter.filterList(pokemons.subList(151,251));break;
                case 3: searchEditText.setText("");adapter.filterList(pokemons.subList(251,386));break;
                case 4: searchEditText.setText("");adapter.filterList(pokemons.subList(386,493));break;
                case 5: searchEditText.setText("");adapter.filterList(pokemons.subList(493,649));break;
                case 6: searchEditText.setText("");adapter.filterList(pokemons.subList(649,721));break;
                case 7: searchEditText.setText("");adapter.filterList(pokemons.subList(721,809));break;
                case 8: searchEditText.setText("");adapter.filterList(pokemons.subList(809,898));break;
                case 9: searchEditText.setText("");adapter.filterList(pokemons.subList(898,905));break;
                case 10: searchEditText.setText("");adapter.filterList(pokemons.subList(905,1024));break;


            }
        }
    }

    @Override
    protected void onPause() {
        tabSelecionada = tabLayout.getSelectedTabPosition();

        super.onPause();
    }

    int tabSelecionada = 0;
}



