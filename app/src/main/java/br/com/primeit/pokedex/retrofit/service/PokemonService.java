package br.com.primeit.pokedex.retrofit.service;

import br.com.primeit.pokedex.model.PokemonDesc;
import br.com.primeit.pokedex.model.ResponsePokemon;
import br.com.primeit.pokedex.model.PokemonInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonService {

    @GET("pokemon/")
    Call<ResponsePokemon> searchAll(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{id}")
    Call<PokemonInfo> searchInfo(@Path("id") int id);

    @GET("pokemon-species/{id}")
    Call<PokemonDesc> searchDesc(@Path("id") int id);
}
