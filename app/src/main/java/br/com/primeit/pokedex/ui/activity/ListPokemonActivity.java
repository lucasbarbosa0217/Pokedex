package br.com.primeit.pokedex.ui.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private static final int offset = 0;
    private static final int limit = 1025;
    private static ArrayList<Pokemon> pokemonArrayList = new ArrayList<>();
    private EditText searchEditText;
    private TabLayout tabLayout;
    public TabLayout.Tab tab1;
    private SwipeRefreshLayout swipeContainer;
    private Pokemon erroConexao = new Pokemon();

    private ImageButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_pokemon_activity);
        setTheme(R.style.Theme_Splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        createErrorPokemon();
        setSwipeRefreshLayout();
        setTabLayout();
        ImageButton deleteText = findViewById(R.id.deleteText);
        deleteText.setOnClickListener(view -> {
            tabLayout.selectTab(tabLayout.getTabAt(0));
            searchEditText.setText("");
        });
        setReloadButton();
        setSearchBar();

        searchPokemon(limit, offset);

    }

    private void setSwipeRefreshLayout() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);
        swipeContainer.setOnRefreshListener(() -> {
            tabLayout.selectTab(tabLayout.getTabAt(0));
            searchPokemon(limit, offset);
        });
    }

    private void createErrorPokemon() {
        erroConexao = new Pokemon();
        erroConexao.setName("Connection error");
        erroConexao.setNumber(1404);
        erroConexao.setUrl("https://cdn3.iconfinder.com/data/icons/wifi-2/460/connection-error-512.png");
    }

    private void setTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (hasConnectivity()) {
                    searchEditText.setText("");
                    int[][] intervals = {{0, 898}, {0, 151}, {151, 251}, {251, 386}, {386, 493}, {493, 649}, {649, 721}, {721, 809}, {809, 898}, {898, 905}, {905, 1025}};
                    int position = tab.getPosition();
                    if (position >= 0 && position < intervals.length) {
                        int start = intervals[position][0];
                        int end = intervals[position][1];
                        adapter.filterList(pokemonArrayList.subList(start, end));
                    }
                } else {
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
        searchEditText = findViewById(R.id.edittext);
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

    private void setReloadButton() {
        fab = findViewById(R.id.reloadList);
        fab.setOnClickListener(view -> {
            Intent savedIntent = new Intent(ListPokemonActivity.this, SavedListPokemonActivity.class);
            startActivity(savedIntent);
        });
    }


    public void searchPokemon(int limit, int offset) {
        PokemonService service = new PokedexRetrofit().getPokemonService();
        Call<ResponsePokemon> responseCall = service.searchAll(limit, offset);

        responseCall.enqueue(new Callback<ResponsePokemon>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePokemon> call, @NonNull retrofit2.Response<ResponsePokemon> response) {
                if (response.isSuccessful()) {
                    ResponsePokemon resposta = response.body();
                    pokemonArrayList = resposta.getResults();
                    adapter.clear();
                    adapter.update(pokemonArrayList);
                    configureRecyclerView();
                } else {
                    Toast.makeText(ListPokemonActivity.this, "Erro de Conexão", Toast.LENGTH_SHORT).show();
                }
                swipeContainer.setRefreshing(false);
            }
            @Override
            public void onFailure(@NonNull Call<ResponsePokemon> call, @NonNull Throwable t) {
                pokemonArrayList.clear();
                pokemonArrayList.add(erroConexao);
                adapter.clear();
                adapter.update(pokemonArrayList);
                configureRecyclerView();
                Toast.makeText(ListPokemonActivity.this, "Erro De Conexão", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void configureRecyclerView() {
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
            } else {
                Toast.makeText(ListPokemonActivity.this, "Erro de Conexão", Toast.LENGTH_SHORT).show();
            }
            setTheme(R.style.Theme_Pokedex);
        });

    }

    public boolean hasConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isPoke = true;
        if (pokemonArrayList.size() < 100) {
            isPoke = false;
        }
        if (activeNetwork != null & isPoke) {
            return true;
        } else {
            return false;
        }

    }

    private void filter(String text) {
        ArrayList<Pokemon> filteredList = new ArrayList<>();
        if (pokemonArrayList != null) {
            for (Pokemon p : pokemonArrayList) {
                if (p.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(p);
                }
            }
            adapter.filterList(filteredList);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

}



