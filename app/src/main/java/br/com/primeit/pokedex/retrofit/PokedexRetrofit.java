package br.com.primeit.pokedex.retrofit;

import br.com.primeit.pokedex.retrofit.service.PokemonService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokedexRetrofit {

    private static final String URL_BASE = "https://pokeapi.co/api/v2/";
    private final PokemonService pokemonService;

    public PokedexRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pokemonService = retrofit.create(PokemonService.class);
    }


    public PokemonService getPokemonService() {
        return pokemonService;
    }
}
